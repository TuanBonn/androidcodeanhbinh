import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import '../models/hike.dart';
import '../models/observation.dart';

class DatabaseHelper {
  static final DatabaseHelper _instance = DatabaseHelper._internal();
  static Database? _database;

  factory DatabaseHelper() {
    return _instance;
  }

  DatabaseHelper._internal();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDatabase();
    return _database!;
  }

  Future<Database> _initDatabase() async {
    String path = join(await getDatabasesPath(), 'mhike_hybrid_v2.db');
    return await openDatabase(
      path,
      version: 1,
      onCreate: _onCreate,
      onConfigure: _onConfigure,
    );
  }

  Future<void> _onConfigure(Database db) async {
    await db.execute('PRAGMA foreign_keys = ON');
  }

  Future<void> _onCreate(Database db, int version) async {
    await db.execute('''
      CREATE TABLE hikes(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT, location TEXT, date TEXT, parking_available TEXT,
        length TEXT, difficulty TEXT, description TEXT, weather TEXT, trail_condition TEXT
      )
    ''');

    await db.execute('''
      CREATE TABLE observations(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        hike_id INTEGER,
        observation TEXT,
        time TEXT,
        comments TEXT,
        FOREIGN KEY(hike_id) REFERENCES hikes(id) ON DELETE CASCADE
      )
    ''');
  }

  Future<int> insertHike(Hike hike) async {
    Database db = await database;
    return await db.insert('hikes', hike.toMap(), conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<int> updateHike(Hike hike) async {
    Database db = await database;
    return await db.update('hikes', hike.toMap(), where: 'id = ?', whereArgs: [hike.id]);
  }

  Future<List<Hike>> getHikes() async {
    Database db = await database;
    final List<Map<String, dynamic>> maps = await db.query('hikes', orderBy: 'id DESC');
    return List.generate(maps.length, (i) => Hike.fromMap(maps[i]));
  }

  Future<List<Hike>> searchHikes(String keyword) async {
    Database db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      'hikes',
      where: 'name LIKE ?',
      whereArgs: ['%$keyword%'],
      orderBy: 'id DESC',
    );
    return List.generate(maps.length, (i) => Hike.fromMap(maps[i]));
  }

  Future<void> deleteHike(int id) async {
    Database db = await database;
    await db.delete('hikes', where: 'id = ?', whereArgs: [id]);
  }

  Future<void> deleteAllHikes() async {
    Database db = await database;
    await db.delete('hikes');
  }

  Future<int> insertObservation(Observation obs) async {
    Database db = await database;
    return await db.insert('observations', obs.toMap());
  }

  Future<List<Observation>> getObservations(int hikeId) async {
    Database db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      'observations',
      where: 'hike_id = ?',
      whereArgs: [hikeId],
      orderBy: 'id DESC',
    );
    return List.generate(maps.length, (i) => Observation.fromMap(maps[i]));
  }

  Future<void> deleteObservation(int id) async {
    Database db = await database;
    await db.delete('observations', where: 'id = ?', whereArgs: [id]);
  }

  Future<int> updateObservation(Observation observation) async {
    Database db = await database;
    return await db.update(
      'observations',
      observation.toMap(),
      where: 'id = ?',
      whereArgs: [observation.id],
    );
  }

}