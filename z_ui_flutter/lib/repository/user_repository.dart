import 'package:sqflite/sqflite.dart';
import 'package:z_ui_flutter/model/user.dart';
import 'package:z_ui_flutter/repository/database_helper.dart';

extension UserRepository on DatabaseHelper {
  Future<void> insertUser(User user) async {
    final db = await DatabaseHelper.database;
    await db!.insert('user', user.toMap(), conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<List<User>> getUsers() async {
    final db = await DatabaseHelper.database;
    final List<Map<String, dynamic>> maps = await db!.query('user');
    return maps.map((e) => User.fromMap(e)).toList();
  }

  Future<void> updateUser(User user) async {
    final db = await DatabaseHelper.database;
    await db!.update('user', user.toMap(), where: 'id = ?', whereArgs: [user.id]);
  }

  Future<void> deleteUser(int id) async {
    final db = await DatabaseHelper.database;
    await db!.delete('user', where: 'id = ?', whereArgs: [id]);
  }
}