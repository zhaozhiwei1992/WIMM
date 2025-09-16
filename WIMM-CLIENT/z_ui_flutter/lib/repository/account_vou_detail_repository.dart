import 'package:sqflite/sqflite.dart';
import 'package:z_ui_flutter/model/account_vou_detail.dart';
import 'package:z_ui_flutter/repository/database_helper.dart';

extension AccountVouDetailRepository on DatabaseHelper {
  Future<void> insertAcctVouDetail(AcctVouDetail acctvoudetail) async {
    final db = await DatabaseHelper.database;
    await db!.insert('acctvoudetail', acctvoudetail.toMap(), conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<List<AcctVouDetail>> getAcctVouDetails() async {
    final db = await DatabaseHelper.database;
    final List<Map<String, dynamic>> maps = await db!.query('acctvoudetail');
    return maps.map((e) => AcctVouDetail.fromMap(e)).toList();
  }

  Future<void> updateAcctVouDetail(AcctVouDetail acctvoudetail) async {
    final db = await DatabaseHelper.database;
    await db!.update('acctvoudetail', acctvoudetail.toMap(), where: 'id = ?', whereArgs: [acctvoudetail.id]);
  }

  Future<void> deleteAcctVouDetail(int id) async {
    final db = await DatabaseHelper.database;
    await db!.delete('acctvoudetail', where: 'id = ?', whereArgs: [id]);
  }
}