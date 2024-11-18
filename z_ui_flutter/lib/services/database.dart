import 'dart:io';

import 'package:drift/drift.dart';
import 'package:drift/native.dart';
import 'package:path_provider/path_provider.dart';
import 'package:path/path.dart' as p;

part 'database.g.dart';

// 用户表
class User extends Table {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get name => text()();
  TextColumn get login => text()();
}

// 会计凭证明细表
class AcctVouDetail extends Table {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get voucherNo => text()();
  TextColumn get acctClsCode => text()();
  TextColumn get acctClsName => text()();
  RealColumn get amt => real().named('amount')();
  IntColumn get drCr => integer()();
  TextColumn get remark => text()();
  TextColumn get createdBy => text().customConstraint('DEFAULT ' '')();
  DateTimeColumn get createdDate =>
      dateTime().customConstraint('DEFAULT CURRENT_TIMESTAMP')();
  TextColumn get lastModifiedBy => text().customConstraint('DEFAULT ' '')();
  DateTimeColumn get lastModifiedDate =>
      dateTime().customConstraint('DEFAULT CURRENT_TIMESTAMP')();
}

@DriftDatabase(tables: [User, AcctVouDetail], )
class MyDatabase extends _$MyDatabase {
  MyDatabase() : super(_openConnection());

  @override
  int get schemaVersion => 1;
}

QueryExecutor _openConnection() {
  return LazyDatabase(() async {
    // final dbFolder = await getApplicationDocumentsDirectory();
    // final file = File('${dbFolder.path}/database.db');
    // return await databaseFactory.open(file);
    final path = (Platform.isMacOS || Platform.isIOS)
        ? await getApplicationDocumentsDirectory()
        : await getApplicationSupportDirectory();
    //这是数据库文件。
    final dbFile = p.join(path.path, 'databases', 'app.db');

    return NativeDatabase(
      File(dbFile),
      setup: (db) {
        //这个语法检查数据库是否已经加密
        // final result = db.select('pragma cipher_version');
        // if (result.isEmpty) {
        //   throw UnsupportedError(
        //     'this database needs to run with sqlcipher, but that library is '
        //     'not available!',
        //   );
        // }else{
        //   print("数据库已加密");
        // }

        //数据库的密码。
        // final escapedKey = _encryptionPassword;
        //通过密码打开数据库。
        // db.execute("pragma key = '$escapedKey'");
      },
    );
  });
}

// User Dao
// class UserDao extends DatabaseAccessor<MyDatabase> {
//   final MyDatabase db;

//   UserDao(this.db);

//   // User相关操作...
// }

// // AcctVouDetail Dao
// class AcctVouDetailDao extends DatabaseAccessor<MyDatabase> {
//   final MyDatabase db;

//   AcctVouDetailDao(this.db);

//   // AcctVouDetail相关操作...
// }
