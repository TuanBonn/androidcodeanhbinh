import 'package:flutter/material.dart';
import '../helpers/database_helper.dart';
import '../models/hike.dart';
import '../models/observation.dart';
import 'add_observation_screen.dart';

class HikeDetailScreen extends StatefulWidget {
  final Hike hike;
  const HikeDetailScreen({super.key, required this.hike});

  @override
  State<HikeDetailScreen> createState() => _HikeDetailScreenState();
}

class _HikeDetailScreenState extends State<HikeDetailScreen> {
  late Future<List<Observation>> _obsListFuture;

  @override
  void initState() {
    super.initState();
    _refreshObsList();
  }

  void _refreshObsList() {
    setState(() {
      _obsListFuture = DatabaseHelper().getObservations(widget.hike.id!);
    });
  }


  void _confirmDeleteObservation(int id) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete Observation'),
        content: const Text('Are you sure you want to delete this observation?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Cancel'),
          ),
          TextButton(
            onPressed: () async {
              await DatabaseHelper().deleteObservation(id);
              if (mounted) Navigator.pop(ctx);
              _refreshObsList();
              if (mounted) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Observation deleted')),
                );
              }
            },
            child: const Text('Delete', style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );
  }

  Widget _buildInfoRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4.0),
      child: Row(
        children: [
          Icon(icon, size: 20, color: Colors.blueGrey),
          const SizedBox(width: 10),
          Text('$label: ', style: const TextStyle(fontWeight: FontWeight.bold)),
          Expanded(child: Text(value, style: const TextStyle(fontSize: 16))),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(widget.hike.name), backgroundColor: Colors.tealAccent),
      body: Column(
        children: [

          Card(
            margin: const EdgeInsets.all(10),
            elevation: 4,
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildInfoRow(Icons.location_on, 'Location', widget.hike.location),
                  _buildInfoRow(Icons.calendar_today, 'Date', widget.hike.date),
                  _buildInfoRow(Icons.local_parking, 'Parking', widget.hike.parkingAvailable),
                  _buildInfoRow(Icons.straighten, 'Length', '${widget.hike.length} km'),
                  _buildInfoRow(Icons.terrain, 'Difficulty', widget.hike.difficulty),
                  _buildInfoRow(Icons.cloud, 'Weather', widget.hike.weather),
                  const Divider(),
                  Text('Description:', style: TextStyle(color: Colors.grey[600])),
                  Text(widget.hike.description, style: const TextStyle(fontStyle: FontStyle.italic)),
                ],
              ),
            ),
          ),


          const Padding(
            padding: EdgeInsets.symmetric(vertical: 10),
            child: Text('Observations', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          Expanded(
            child: FutureBuilder<List<Observation>>(
              future: _obsListFuture,
              builder: (context, snapshot) {
                if (!snapshot.hasData || snapshot.data!.isEmpty) {
                  return const Center(child: Text('No observations yet.'));
                }
                return ListView.builder(
                  itemCount: snapshot.data!.length,
                  itemBuilder: (context, index) {
                    final obs = snapshot.data![index];
                    return Card(
                      margin: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                      color: Colors.orange[50],
                      child: ListTile(
                        leading: const Icon(Icons.visibility, color: Colors.orange),
                        title: Text(obs.observation, style: const TextStyle(fontWeight: FontWeight.bold)),
                        subtitle: Text('${obs.time}\n${obs.comments}'),
                        isThreeLine: true,
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            IconButton(
                              icon: const Icon(Icons.edit, color: Colors.blue),
                              onPressed: () async {

                                final result = await Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) => AddObservationScreen(
                                      hikeId: widget.hike.id!,
                                      observation: obs,
                                    ),
                                  ),
                                );
                                if (result == true) _refreshObsList();
                              },
                            ),
                            IconButton(
                              icon: const Icon(Icons.delete, color: Colors.red),
                              onPressed: () => _confirmDeleteObservation(obs.id!),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          final result = await Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => AddObservationScreen(hikeId: widget.hike.id!)),
          );
          if (result == true) _refreshObsList();
        },
        label: const Text('Add Observation'),
        icon: const Icon(Icons.add_a_photo),
        backgroundColor: Colors.orange,
      ),
    );
  }
}