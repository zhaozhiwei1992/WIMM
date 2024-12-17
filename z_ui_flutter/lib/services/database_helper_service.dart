import 'package:z_ui_flutter/repository/database_helper.dart';

class DatabaseService {
  static final DatabaseHelper _databaseHelper = DatabaseHelper();

  // 提供一个静态方法来获取数据库实例
  static DatabaseHelper get databaseHelper => _databaseHelper;
}