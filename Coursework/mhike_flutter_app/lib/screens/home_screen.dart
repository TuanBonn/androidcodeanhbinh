import 'package:flutter/material.dart';
import '../helpers/database_helper.dart';
import '../models/hike.dart';
import 'add_hike_screen.dart';
import 'hike_detail_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late Future<List<Hike>> _hikeListFuture;
  final TextEditingController _searchController = TextEditingController();
  bool _isSearching = false;

  @override
  void initState() {
    super.initState();
    _refreshHikeList();
  }

  void _refreshHikeList([String? query]) {
    setState(() {
      if (query != null && query.isNotEmpty) {
        _hikeListFuture = DatabaseHelper().searchHikes(query);
      } else {
        _hikeListFuture = DatabaseHelper().getHikes();
      }
    });
  }

  void _deleteHike(int id) async {
    await DatabaseHelper().deleteHike(id);
    _refreshHikeList(_searchController.text);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: _isSearching
            ? TextField(
          controller: _searchController,
          autofocus: true,
          style: const TextStyle(color: Colors.white, fontSize: 18),
          decoration: const InputDecoration(
            hintText: 'Search hike...',
            hintStyle: TextStyle(color: Colors.white70),
            border: InputBorder.none,
          ),
          onChanged: (value) => _refreshHikeList(value),
        )
            : const Text('M-Hike Explore', style: TextStyle(fontWeight: FontWeight.w600)),
        actions: [
          IconButton(
            icon: Icon(_isSearching ? Icons.close : Icons.search),
            onPressed: () {
              setState(() {
                _isSearching = !_isSearching;
                if (!_isSearching) {
                  _searchController.clear();
                  _refreshHikeList();
                }
              });
            },
          ),
          if (!_isSearching)
            IconButton(
              icon: const Icon(Icons.delete_sweep_rounded),
              tooltip: 'Delete All',
              onPressed: () async {
                await DatabaseHelper().deleteAllHikes();
                _refreshHikeList();
              },
            )
        ],
      ),
      body: FutureBuilder<List<Hike>>(
        future: _hikeListFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator(color: Colors.teal));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    padding: const EdgeInsets.all(24),
                    decoration: BoxDecoration(
                      color: Colors.teal.withOpacity(0.1),
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(Icons.landscape_rounded, size: 80, color: Colors.teal),
                  ),
                  const SizedBox(height: 16),
                  const Text(
                    'No hikes found.',
                    style: TextStyle(fontSize: 20, color: Colors.blueGrey, fontWeight: FontWeight.w500),
                  ),
                  const SizedBox(height: 8),
                  const Text(
                    'Tap + to add a new adventure!',
                    style: TextStyle(fontSize: 16, color: Colors.grey),
                  ),
                ],
              ),
            );
          }

          final hikes = snapshot.data!;
          return ListView.builder(
            padding: const EdgeInsets.only(top: 12, bottom: 80),
            itemCount: hikes.length,
            itemBuilder: (context, index) {
              final hike = hikes[index];
              return Card(
                margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                elevation: 2,
                shadowColor: Colors.teal.withOpacity(0.2),
                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                child: InkWell(
                  borderRadius: BorderRadius.circular(16),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => HikeDetailScreen(hike: hike)),
                    );
                  },
                  child: Padding(
                    padding: const EdgeInsets.all(12.0),
                    child: Row(
                      children: [
                        Container(
                          width: 60,
                          height: 60,
                          decoration: BoxDecoration(
                            color: Colors.teal.shade50,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: const Icon(Icons.terrain_rounded, color: Colors.teal, size: 32),
                        ),
                        const SizedBox(width: 16),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                hike.name,
                                style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18, color: Colors.black87),
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              ),
                              const SizedBox(height: 6),
                              Row(
                                children: [
                                  const Icon(Icons.location_on, size: 14, color: Colors.grey),
                                  const SizedBox(width: 4),
                                  Expanded(
                                    child: Text(
                                      hike.location,
                                      style: const TextStyle(color: Colors.grey, fontSize: 14),
                                      maxLines: 1,
                                      overflow: TextOverflow.ellipsis,
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 4),
                              Row(
                                children: [
                                  const Icon(Icons.calendar_today_rounded, size: 14, color: Colors.grey),
                                  const SizedBox(width: 4),
                                  Text(hike.date, style: const TextStyle(color: Colors.grey, fontSize: 14)),
                                ],
                              ),
                            ],
                          ),
                        ),

                        Column(
                          children: [
                            IconButton(
                              icon: const Icon(Icons.edit_rounded, color: Colors.teal),
                              visualDensity: VisualDensity.compact,
                              onPressed: () async {
                                final result = await Navigator.push(
                                  context,
                                  MaterialPageRoute(builder: (context) => AddHikeScreen(hike: hike)),
                                );
                                if (result == true) _refreshHikeList(_searchController.text);
                              },
                            ),
                            IconButton(
                              icon: const Icon(Icons.delete_outline_rounded, color: Colors.redAccent),
                              visualDensity: VisualDensity.compact,
                              onPressed: () {
                                showDialog(
                                  context: context,
                                  builder: (ctx) => AlertDialog(
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                                    title: const Text('Confirm Delete'),
                                    content: Text('Are you sure you want to delete "${hike.name}"?'),
                                    actions: [
                                      TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
                                      ElevatedButton(
                                        style: ElevatedButton.styleFrom(backgroundColor: Colors.redAccent, foregroundColor: Colors.white),
                                        onPressed: () {
                                          _deleteHike(hike.id!);
                                          Navigator.pop(ctx);
                                        },
                                        child: const Text('Delete'),
                                      ),
                                    ],
                                  ),
                                );
                              },
                            ),
                          ],
                        )
                      ],
                    ),
                  ),
                ),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          final result = await Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => const AddHikeScreen()),
          );
          if (result == true) _refreshHikeList();
        },
        backgroundColor: Colors.teal,
        icon: const Icon(Icons.add, color: Colors.white),
        label: const Text('New Hike', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
      ),
    );
  }
}