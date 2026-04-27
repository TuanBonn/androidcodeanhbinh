class Hike {
  final int? id;
  final String name;
  final String location;
  final String date;
  final String parkingAvailable;
  final String length;
  final String difficulty;
  final String description;
  final String weather;
  final String trailCondition;

  Hike({
    this.id,
    required this.name,
    required this.location,
    required this.date,
    required this.parkingAvailable,
    required this.length,
    required this.difficulty,
    required this.description,
    required this.weather,
    required this.trailCondition,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'location': location,
      'date': date,
      'parking_available': parkingAvailable,
      'length': length,
      'difficulty': difficulty,
      'description': description,
      'weather': weather,
      'trail_condition': trailCondition,
    };
  }


  factory Hike.fromMap(Map<String, dynamic> map) {
    return Hike(
      id: map['id'],
      name: map['name'],
      location: map['location'],
      date: map['date'],
      parkingAvailable: map['parking_available'],
      length: map['length'],
      difficulty: map['difficulty'],
      description: map['description'],
      weather: map['weather'],

      trailCondition: map['trail_condition'],
    );
  }
}