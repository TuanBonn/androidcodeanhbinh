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
        SnackBar(content: Text(widget.observation == null ? 'Observation added!' : 'Observation updated!')),
      );
      Navigator.pop(context, true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.observation == null ? 'Add Observation' : 'Edit Observation'),
        backgroundColor: Colors.orangeAccent,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _obsController,
                decoration: const InputDecoration(labelText: 'Observation *', border: OutlineInputBorder()),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _timeController,
                decoration: const InputDecoration(labelText: 'Time *', border: OutlineInputBorder()),
                validator: (value) => value!.isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _commentsController,
                decoration: const InputDecoration(labelText: 'Comments (Optional)', border: OutlineInputBorder()),
                maxLines: 3,
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton.icon(
                  onPressed: _saveObservation,
                  icon: const Icon(Icons.save),
                  label: Text(widget.observation == null ? 'SAVE OBSERVATION' : 'UPDATE OBSERVATION'),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 15),
                    backgroundColor: Colors.green,
                    foregroundColor: Colors.white,
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}