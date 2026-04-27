import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../helpers/database_helper.dart';
import '../models/observation.dart';

class AddObservationScreen extends StatefulWidget {
  final int hikeId;
  final Observation? observation;

  const AddObservationScreen({
    super.key,
    required this.hikeId,
    this.observation
  });

  @override
  State<AddObservationScreen> createState() => _AddObservationScreenState();
}

class _AddObservationScreenState extends State<AddObservationScreen> {
  final _formKey = GlobalKey<FormState>();
  final _obsController = TextEditingController();
  final _timeController = TextEditingController();
  final _commentsController = TextEditingController();

  @override
  void initState() {
    super.initState();

    if (widget.observation != null) {
      _obsController.text = widget.observation!.observation;
      _timeController.text = widget.observation!.time;
      _commentsController.text = widget.observation!.comments;
    } else {
      _timeController.text = DateFormat('dd/MM/yyyy HH:mm').format(DateTime.now());
    }
  }

  void _saveObservation() async {
    if (_formKey.currentState!.validate()) {
      Observation obs = Observation(
        id: widget.observation?.id,
        hikeId: widget.hikeId,
        observation: _obsController.text,
        time: _timeController.text,
        comments: _commentsController.text,
      );

      if (widget.observation == null) {
        await DatabaseHelper().insertObservation(obs);
      } else {
        await DatabaseHelper().updateObservation(obs);
      }

      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(widget.observation == null ? 'Observation added!' : 'Observation updated!'),
          backgroundColor: Colors.teal,
          behavior: SnackBarBehavior.floating,
        ),
      );
      Navigator.pop(context, true);
    }
  }

  InputDecoration _inputStyle(String label, {IconData? icon}) {
    return InputDecoration(
      labelText: label,
      prefixIcon: icon != null ? Icon(icon, color: Colors.orange) : null,
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: BorderSide(color: Colors.grey.shade300),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(12),
        borderSide: BorderSide(color: Colors.orange.shade400, width: 2),
      ),
      filled: true,
      fillColor: Colors.white,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.observation == null ? 'Log Observation' : 'Edit Observation', style: const TextStyle(fontWeight: FontWeight.w600)),
        backgroundColor: Colors.orange.shade700,
        foregroundColor: Colors.white,
      ),
      body: GestureDetector(
        onTap: () => FocusScope.of(context).unfocus(),
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Form(
            key: _formKey,
            child: ListView(
              children: [
                const Text('What did you see?', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.deepOrange)),
                const SizedBox(height: 16),
                TextFormField(
                  controller: _obsController,
                  decoration: _inputStyle('Observation (e.g. Animal, Plant) *', icon: Icons.remove_red_eye),
                  validator: (value) => value!.isEmpty ? 'This field is required' : null,
                ),
                const SizedBox(height: 16),
                TextFormField(
                  controller: _timeController,
                  decoration: _inputStyle('Time *', icon: Icons.access_time_filled),
                  validator: (value) => value!.isEmpty ? 'This field is required' : null,
                ),
                const SizedBox(height: 16),
                TextFormField(
                  controller: _commentsController,
                  decoration: _inputStyle('Comments / Notes (Optional)', icon: Icons.comment),
                  maxLines: 4,
                ),
                const SizedBox(height: 32),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton.icon(
                    onPressed: _saveObservation,
                    icon: const Icon(Icons.save),
                    label: Text(
                      widget.observation == null ? 'SAVE OBSERVATION' : 'UPDATE OBSERVATION',
                      style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, letterSpacing: 1),
                    ),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(vertical: 16),
                      backgroundColor: Colors.orange.shade700,
                      foregroundColor: Colors.white,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      elevation: 2,
                    ),
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}