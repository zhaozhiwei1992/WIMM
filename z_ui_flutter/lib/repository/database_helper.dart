import 'package:sqflite/sqflite.dart';
import 'package:path_provider/path_provider.dart';
import 'package:z_ui_flutter/model/account_vou_detail.dart';

class DatabaseHelper {
  static Database? _database;
  static final _databaseName = "wimm.db";
  static final _databaseVersion = 1;

  static Future<Database?> get database async {
    if (_database != null) return _database;

    // 路径指向应用的文档目录
    final directory = await getApplicationDocumentsDirectory();
    final path = directory.path + '$_databaseName';

    _database = await openDatabase(
      path,
      version: _databaseVersion,
      onCreate: (db, version) async {
        // 创建用户表
        await db.execute('''
          CREATE TABLE user (
            id TEXT PRIMARY KEY,
            name TEXT NOT NULL,
            login TEXT NOT NULL
          )
        ''');

        // 创建会计凭证明细表
        await db.execute('''
          CREATE TABLE acct_vou_detail (
            id TEXT PRIMARY KEY,
            voucher_no TEXT NOT NULL,
            acct_cls_code TEXT NOT NULL,
            acct_cls_name TEXT NOT NULL,
            amt REAL NOT NULL,
            dr_cr INTEGER NOT NULL,
            remark TEXT,
            created_by TEXT NOT NULL,
            created_date TEXT NOT NULL,
            last_modified_by TEXT NOT NULL
          )
        ''');
      },
    );
    return _database;
  }

}