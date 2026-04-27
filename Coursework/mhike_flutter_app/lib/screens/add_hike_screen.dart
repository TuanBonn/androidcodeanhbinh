import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../helpers/database_helper.dart';
import '../models/hike.dart';

class AddHikeScreen extends StatefulWidget {
  final Hike? hike;

  const AddHikeScreen({super.key, this.hike});

  @override
  State<AddHikeScreen> createState() => _AddHikeScreenState();
}

class _AddHikeScreenState extends State<AddHikeScreen> {
  final _formKey = GlobalKey<FormState>();

  final _nameController = TextEditingController();
  final _locationController = TextEditingController();
  final _dateController = TextEditingController();
  final _lengthController = TextEditingController();
  final _descController = TextEditingController();
  final _weatherController = TextEditingController();
  final _trailController = TextEditingController();

  String _parkingValue = 'Yes';
  String _difficultyValue = 'Medium';
  final List<String> _difficulties = ['Easy', 'Medium', 'Hard', 'Very Hard'];

  @override
  void initState() {
    super.initState();

    if (widget.hike != null) {
      _nameController.text = widget.hike!.name;
      _locationController.text = widget.hike!.location;
      _dateController.text = widget.hike!.date;
      _lengthController.text = widget.hike!.length;
      _descController.text = widget.hike!.description;
      _weatherController.text = widget.hike!.weather;
      _trailController.text = widget.hike!.trailCondition;
      _parkingValue = widget.hike!.parkingAvailable;

      if (_difficulties.contains(widget.hike!.difficulty)) {
        _difficultyValue = widget.hike!.difficulty;
      }
    }
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime(2100),
      builder: (context, child) {
        return Theme(
          data: Theme.of(context).copyWith(
            colorScheme: const ColorScheme.light(
              primary: Colors.teal,
              onPrimary: Colors.white,
              onSurface: Colors.black,
            ),
          ),
          child: child!,
        );
      },
    );
    if (picked != null) {
      setState(() {
        _dateController.text = DateFormat('dd/MM/yyyy').format(picked);
      });
    }
  }

  void _saveHike() async {
    if (_formKey.currentState!.validate()) {
      Hike hike = Hike(
        id: widget.hike?.id,
        name: _nameController.text,
        location: _locationController.text,
        date: _dateController.text,
        parkingAvailable: _parkingValue,
        length: _lengthController.text,
        difficulty: _difficultyValue,
        description: _descController.text,
        weather: _weatherController.text,
        trailCondition: _trailController.text,
      );

      if (widget.hike == null) {
        await DatabaseHelper().insertHike(hike);
      } else {
        await DatabaseHelper().updateHike(hike);
      }

      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(widget.hike == null ? 'Hike added successfully!' : 'Hike updated!'),
          behavior: SnackBarBehavior.floating,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
          backgroundColor: Colors.teal,
        ),
      );
      Navigator.pop(context, true);
    }
  }

  InputDecoration _inputStyle(String label, {IconData? icon}) {
    return InputDecoration(
      labelText: label,
      prefixIcon: icon != null ? Icon(icon, color: Colors.teal) : null,
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: BorderSide(color: Colors.grey.shade300),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: const BorderSide(color: Colors.teal, width: 2),
      ),
      filled: true,
      fillColor: Colors.white,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.hike == null ? 'Plan New Hike' : 'Edit Hike Details', style: const TextStyle(fontWeight: FontWeight.w600)),
      ),
      body: GestureDetector(
        onTap: () => FocusScope.of(context).unfocus(),
        child: Form(
          key: _formKey,
          child: ListView(
            padding: const EdgeInsets.all(20.0),
            children: [
              const Text('Basic Information', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.teal)),
              const SizedBox(height: 16),
              TextFormField(
                controller: _nameController,
                decoration: _inputStyle('Name of Hike *', icon: Icons.landscape),
                validator: (value) => value == null || value.isEmpty ? 'Please enter name' : null,
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _locationController,
                decoration: _inputStyle('Location *', icon: Icons.location_on),
                validator: (value) => value == null || value.isEmpty ? 'Please enter location' : null,
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _dateController,
                readOnly: true,
                onTap: () => _selectDate(context),
                decoration: _inputStyle('Date of Hike *', icon: Icons.calendar_today),
                validator: (value) => value == null || value.isEmpty ? 'Please select date' : null,
              ),
              const SizedBox(height: 24),
              const Text('Hike Details', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.teal)),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    flex: 1,
                    child: TextFormField(
                      controller: _lengthController,
                      keyboardType: TextInputType.number,
                      decoration: _inputStyle('Length (km) *'),
                      validator: (value) => value == null || value.isEmpty ? 'Required' : null,
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    flex: 1,
                    child: DropdownButtonFormField<String>(
                      value: _difficultyValue,
                      decoration: _inputStyle('Difficulty'),
                      items: _difficulties.map((String value) => DropdownMenuItem<String>(value: value, child: Text(value))).toList(),
                      onChanged: (newValue) => setState(() => _difficultyValue = newValue!),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              Container(
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  border: Border.all(color: Colors.grey.shade300),
                ),
                padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                child: Row(
                  children: [
                    const Icon(Icons.local_parking, color: Colors.teal),
                    const SizedBox(width: 12),
                    const Text('Parking Available?', style: TextStyle(fontSize: 16)),
                    const Spacer(),
                    Radio<String>(value: 'Yes', groupValue: _parkingValue, activeColor: Colors.teal, onChanged: (val) => setState(() => _parkingValue = val!)),
                    const Text('Yes'),
                    Radio<String>(value: 'No', groupValue: _parkingValue, activeColor: Colors.teal, onChanged: (val) => setState(() => _parkingValue = val!)),
                    const Text('No'),
                  ],
                ),
              ),
              const SizedBox(height: 24),
              const Text('Additional Info', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.teal)),
              const SizedBox(height: 16),
              TextFormField(
                controller: _weatherController,
                decoration: _inputStyle('Expected Weather', icon: Icons.cloud),
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _trailController,
                decoration: _inputStyle('Trail Condition', icon: Icons.directions_walk),
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _descController,
                maxLines: 4,
                decoration: _inputStyle('Description / Notes', icon: Icons.notes),
              ),
              const SizedBox(height: 30),
              ElevatedButton(
                onPressed: _saveHike,
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  backgroundColor: widget.hike == null ? Colors.teal : Colors.orange.shade700,
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  elevation: 2,
                ),
                child: Text(
                  widget.hike == null ? 'SAVE HIKE' : 'UPDATE HIKE',
                  style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, letterSpacing: 1),
                ),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
}