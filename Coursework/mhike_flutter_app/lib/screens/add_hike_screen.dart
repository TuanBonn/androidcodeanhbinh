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
        SnackBar(content: Text(widget.hike == null ? 'Hike added!' : 'Hike updated!')),
      );
      Navigator.pop(context, true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.hike == null ? 'Add New Hike' : 'Edit Hike'),
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(labelText: 'Name of Hike *', border: OutlineInputBorder()),
                validator: (value) => value == null || value.isEmpty ? 'Please enter name' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _locationController,
                decoration: const InputDecoration(labelText: 'Location *', border: OutlineInputBorder()),
                validator: (value) => value == null || value.isEmpty ? 'Please enter location' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _dateController,
                readOnly: true,
                onTap: () => _selectDate(context),
                decoration: const InputDecoration(labelText: 'Date of Hike *', border: OutlineInputBorder(), suffixIcon: Icon(Icons.calendar_today)),
                validator: (value) => value == null || value.isEmpty ? 'Please select date' : null,
              ),
              const SizedBox(height: 10),
              const Text('Parking Available?', style: TextStyle(fontSize: 16)),
              Row(
                children: [
                  Expanded(child: RadioListTile<String>(title: const Text('Yes'), value: 'Yes', groupValue: _parkingValue, onChanged: (value) => setState(() => _parkingValue = value!))),
                  Expanded(child: RadioListTile<String>(title: const Text('No'), value: 'No', groupValue: _parkingValue, onChanged: (value) => setState(() => _parkingValue = value!))),
                ],
              ),
              TextFormField(
                controller: _lengthController,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(labelText: 'Length (km) *', border: OutlineInputBorder()),
                validator: (value) => value == null || value.isEmpty ? 'Please enter length' : null,
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<String>(
                value: _difficultyValue,
                decoration: const InputDecoration(labelText: 'Difficulty Level', border: OutlineInputBorder()),
                items: _difficulties.map((String value) => DropdownMenuItem<String>(value: value, child: Text(value))).toList(),
                onChanged: (newValue) => setState(() => _difficultyValue = newValue!),
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _descController,
                maxLines: 3,
                decoration: const InputDecoration(labelText: 'Description', border: OutlineInputBorder()),
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _weatherController,
                decoration: const InputDecoration(labelText: 'Weather', border: OutlineInputBorder()),
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _trailController,
                decoration: const InputDecoration(labelText: 'Trail Condition', border: OutlineInputBorder()),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: _saveHike,
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 15),
                  backgroundColor: widget.hike == null ? Colors.blue : Colors.orange,
                  foregroundColor: Colors.white,
                ),
                child: Text(widget.hike == null ? 'SAVE HIKE' : 'UPDATE HIKE', style: const TextStyle(fontSize: 18)),
              ),
            ],
          ),
        ),
      ),
    );
  }
}