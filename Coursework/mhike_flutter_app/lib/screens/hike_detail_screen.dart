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
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        title: const Text('Delete Observation'),
        content: const Text('Are you sure you want to delete this observation?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            style: ElevatedButton.styleFrom(backgroundColor: Colors.redAccent, foregroundColor: Colors.white),
            onPressed: () async {
              await DatabaseHelper().deleteObservation(id);
              if (mounted) Navigator.pop(ctx);
              _refreshObsList();
              if (mounted) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Observation deleted'), backgroundColor: Colors.teal),
                );
              }
            },
            child: const Text('Delete'),
          ),
        ],
      ),
    );
  }

  Widget _buildInfoRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, size: 22, color: Colors.teal.shade700),
          const SizedBox(width: 12),
          SizedBox(
            width: 100,
            child: Text(label, style: const TextStyle(fontWeight: FontWeight.w600, color: Colors.blueGrey, fontSize: 15)),
          ),
          Expanded(
              child: Text(
                value.isNotEmpty ? value : 'N/A',
                style: const TextStyle(fontSize: 15, color: Colors.black87),
              )
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.hike.name, style: const TextStyle(fontWeight: FontWeight.w600)),
      ),
      body: CustomScrollView(
        slivers: [
          SliverToBoxAdapter(
            child: Card(
              margin: const EdgeInsets.all(16),
              elevation: 3,
              shadowColor: Colors.teal.withOpacity(0.2),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
              child: Padding(
                padding: const EdgeInsets.all(20.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        const Icon(Icons.info_outline, color: Colors.teal),
                        const SizedBox(width: 8),
                        const Text('Trip Details', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.teal)),
                      ],
                    ),
                    const Divider(height: 24, thickness: 1),
                    _buildInfoRow(Icons.location_on, 'Location', widget.hike.location),
                    _buildInfoRow(Icons.calendar_today, 'Date', widget.hike.date),
                    _buildInfoRow(Icons.local_parking, 'Parking', widget.hike.parkingAvailable),
                    _buildInfoRow(Icons.straighten, 'Length', '${widget.hike.length} km'),
                    _buildInfoRow(Icons.terrain, 'Difficulty', widget.hike.difficulty),
                    _buildInfoRow(Icons.cloud, 'Weather', widget.hike.weather),

                    if (widget.hike.description.isNotEmpty) ...[
                      const Divider(height: 24, thickness: 1),
                      const Text('Description / Notes', style: TextStyle(fontWeight: FontWeight.w600, color: Colors.blueGrey)),
                      const SizedBox(height: 8),
                      Text(
                        widget.hike.description,
                        style: const TextStyle(fontStyle: FontStyle.italic, color: Colors.black87, height: 1.4),
                      ),
                    ]
                  ],
                ),
              ),
            ),
          ),
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
              child: Row(
                children: [
                  const Icon(Icons.camera_alt, color: Colors.orange),
                  const SizedBox(width: 8),
                  const Text('Observations', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.black87)),
                ],
              ),
            ),
          ),
          FutureBuilder<List<Observation>>(
            future: _obsListFuture,
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const SliverToBoxAdapter(child: Center(child: CircularProgressIndicator()));
              } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                return const SliverToBoxAdapter(
                    child: Padding(
                      padding: EdgeInsets.all(32.0),
                      child: Center(
                        child: Text('No observations yet. Tap the button below to add one!',
                            textAlign: TextAlign.center,
                            style: TextStyle(color: Colors.grey, fontStyle: FontStyle.italic)
                        ),
                      ),
                    )
                );
              }
              return SliverList(
                delegate: SliverChildBuilderDelegate(
                      (context, index) {
                    final obs = snapshot.data![index];
                    return Card(
                      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
                      elevation: 1,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12), side: BorderSide(color: Colors.orange.shade200)),
                      color: Colors.orange.shade50,
                      child: Padding(
                        padding: const EdgeInsets.all(4.0),
                        child: ListTile(
                          leading: CircleAvatar(
                            backgroundColor: Colors.orange.shade200,
                            child: const Icon(Icons.visibility, color: Colors.deepOrange),
                          ),
                          title: Text(obs.observation, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                          subtitle: Padding(
                            padding: const EdgeInsets.only(top: 6.0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  children: [
                                    const Icon(Icons.access_time, size: 14, color: Colors.grey),
                                    const SizedBox(width: 4),
                                    Text(obs.time, style: const TextStyle(color: Colors.grey)),
                                  ],
                                ),
                                if (obs.comments.isNotEmpty) ...[
                                  const SizedBox(height: 4),
                                  Text(obs.comments, style: const TextStyle(color: Colors.black87)),
                                ]
                              ],
                            ),
                          ),
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
                      ),
                    );
                  },
                  childCount: snapshot.data!.length,
                ),
              );
            },
          ),
          const SliverToBoxAdapter(child: SizedBox(height: 80)),
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
        label: const Text('Add Observation', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        icon: const Icon(Icons.add_a_photo, color: Colors.white),
        backgroundColor: Colors.orange.shade700,
      ),
    );
  }
}