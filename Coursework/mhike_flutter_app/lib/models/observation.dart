class Observation {
  final int? id;
  final int hikeId;
  final String observation;
  final String time;
  final String comments;

  Observation({
    this.id,
    required this.hikeId,
    required this.observation,
    required this.time,
    required this.comments,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'hike_id': hikeId,
      'observation': observation,
      'time': time,
      'comments': comments,
    };
  }

  factory Observation.fromMap(Map<String, dynamic> map) {
    return Observation(
      id: map['id'],
      hikeId: map['hike_id'],
      observation: map['observation'],
      time: map['time'],
      comments: map['comments'],
    );
  }
}