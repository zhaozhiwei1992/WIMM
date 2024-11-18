// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'database.dart';

// ignore_for_file: type=lint
class $UserTable extends User with TableInfo<$UserTable, UserData> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $UserTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _idMeta = const VerificationMeta('id');
  @override
  late final GeneratedColumn<int> id = GeneratedColumn<int>(
      'id', aliasedName, false,
      hasAutoIncrement: true,
      type: DriftSqlType.int,
      requiredDuringInsert: false,
      defaultConstraints:
          GeneratedColumn.constraintIsAlways('PRIMARY KEY AUTOINCREMENT'));
  static const VerificationMeta _nameMeta = const VerificationMeta('name');
  @override
  late final GeneratedColumn<String> name = GeneratedColumn<String>(
      'name', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _loginMeta = const VerificationMeta('login');
  @override
  late final GeneratedColumn<String> login = GeneratedColumn<String>(
      'login', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  @override
  List<GeneratedColumn> get $columns => [id, name, login];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'user';
  @override
  VerificationContext validateIntegrity(Insertable<UserData> instance,
      {bool isInserting = false}) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('id')) {
      context.handle(_idMeta, id.isAcceptableOrUnknown(data['id']!, _idMeta));
    }
    if (data.containsKey('name')) {
      context.handle(
          _nameMeta, name.isAcceptableOrUnknown(data['name']!, _nameMeta));
    } else if (isInserting) {
      context.missing(_nameMeta);
    }
    if (data.containsKey('login')) {
      context.handle(
          _loginMeta, login.isAcceptableOrUnknown(data['login']!, _loginMeta));
    } else if (isInserting) {
      context.missing(_loginMeta);
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {id};
  @override
  UserData map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return UserData(
      id: attachedDatabase.typeMapping
          .read(DriftSqlType.int, data['${effectivePrefix}id'])!,
      name: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}name'])!,
      login: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}login'])!,
    );
  }

  @override
  $UserTable createAlias(String alias) {
    return $UserTable(attachedDatabase, alias);
  }
}

class UserData extends DataClass implements Insertable<UserData> {
  final int id;
  final String name;
  final String login;
  const UserData({required this.id, required this.name, required this.login});
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['id'] = Variable<int>(id);
    map['name'] = Variable<String>(name);
    map['login'] = Variable<String>(login);
    return map;
  }

  UserCompanion toCompanion(bool nullToAbsent) {
    return UserCompanion(
      id: Value(id),
      name: Value(name),
      login: Value(login),
    );
  }

  factory UserData.fromJson(Map<String, dynamic> json,
      {ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return UserData(
      id: serializer.fromJson<int>(json['id']),
      name: serializer.fromJson<String>(json['name']),
      login: serializer.fromJson<String>(json['login']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'id': serializer.toJson<int>(id),
      'name': serializer.toJson<String>(name),
      'login': serializer.toJson<String>(login),
    };
  }

  UserData copyWith({int? id, String? name, String? login}) => UserData(
        id: id ?? this.id,
        name: name ?? this.name,
        login: login ?? this.login,
      );
  UserData copyWithCompanion(UserCompanion data) {
    return UserData(
      id: data.id.present ? data.id.value : this.id,
      name: data.name.present ? data.name.value : this.name,
      login: data.login.present ? data.login.value : this.login,
    );
  }

  @override
  String toString() {
    return (StringBuffer('UserData(')
          ..write('id: $id, ')
          ..write('name: $name, ')
          ..write('login: $login')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode => Object.hash(id, name, login);
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is UserData &&
          other.id == this.id &&
          other.name == this.name &&
          other.login == this.login);
}

class UserCompanion extends UpdateCompanion<UserData> {
  final Value<int> id;
  final Value<String> name;
  final Value<String> login;
  const UserCompanion({
    this.id = const Value.absent(),
    this.name = const Value.absent(),
    this.login = const Value.absent(),
  });
  UserCompanion.insert({
    this.id = const Value.absent(),
    required String name,
    required String login,
  })  : name = Value(name),
        login = Value(login);
  static Insertable<UserData> custom({
    Expression<int>? id,
    Expression<String>? name,
    Expression<String>? login,
  }) {
    return RawValuesInsertable({
      if (id != null) 'id': id,
      if (name != null) 'name': name,
      if (login != null) 'login': login,
    });
  }

  UserCompanion copyWith(
      {Value<int>? id, Value<String>? name, Value<String>? login}) {
    return UserCompanion(
      id: id ?? this.id,
      name: name ?? this.name,
      login: login ?? this.login,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (id.present) {
      map['id'] = Variable<int>(id.value);
    }
    if (name.present) {
      map['name'] = Variable<String>(name.value);
    }
    if (login.present) {
      map['login'] = Variable<String>(login.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('UserCompanion(')
          ..write('id: $id, ')
          ..write('name: $name, ')
          ..write('login: $login')
          ..write(')'))
        .toString();
  }
}

class $AcctVouDetailTable extends AcctVouDetail
    with TableInfo<$AcctVouDetailTable, AcctVouDetailData> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $AcctVouDetailTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _idMeta = const VerificationMeta('id');
  @override
  late final GeneratedColumn<int> id = GeneratedColumn<int>(
      'id', aliasedName, false,
      hasAutoIncrement: true,
      type: DriftSqlType.int,
      requiredDuringInsert: false,
      defaultConstraints:
          GeneratedColumn.constraintIsAlways('PRIMARY KEY AUTOINCREMENT'));
  static const VerificationMeta _voucherNoMeta =
      const VerificationMeta('voucherNo');
  @override
  late final GeneratedColumn<String> voucherNo = GeneratedColumn<String>(
      'voucher_no', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _acctClsCodeMeta =
      const VerificationMeta('acctClsCode');
  @override
  late final GeneratedColumn<String> acctClsCode = GeneratedColumn<String>(
      'acct_cls_code', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _acctClsNameMeta =
      const VerificationMeta('acctClsName');
  @override
  late final GeneratedColumn<String> acctClsName = GeneratedColumn<String>(
      'acct_cls_name', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _amtMeta = const VerificationMeta('amt');
  @override
  late final GeneratedColumn<double> amt = GeneratedColumn<double>(
      'amount', aliasedName, false,
      type: DriftSqlType.double, requiredDuringInsert: true);
  static const VerificationMeta _drCrMeta = const VerificationMeta('drCr');
  @override
  late final GeneratedColumn<int> drCr = GeneratedColumn<int>(
      'dr_cr', aliasedName, false,
      type: DriftSqlType.int, requiredDuringInsert: true);
  static const VerificationMeta _remarkMeta = const VerificationMeta('remark');
  @override
  late final GeneratedColumn<String> remark = GeneratedColumn<String>(
      'remark', aliasedName, false,
      type: DriftSqlType.string, requiredDuringInsert: true);
  static const VerificationMeta _createdByMeta =
      const VerificationMeta('createdBy');
  @override
  late final GeneratedColumn<String> createdBy = GeneratedColumn<String>(
      'created_by', aliasedName, false,
      type: DriftSqlType.string,
      requiredDuringInsert: true,
      $customConstraints: 'DEFAULT ');
  static const VerificationMeta _createdDateMeta =
      const VerificationMeta('createdDate');
  @override
  late final GeneratedColumn<DateTime> createdDate = GeneratedColumn<DateTime>(
      'created_date', aliasedName, false,
      type: DriftSqlType.dateTime,
      requiredDuringInsert: false,
      $customConstraints: 'DEFAULT CURRENT_TIMESTAMP',
      defaultValue: const CustomExpression('CURRENT_TIMESTAMP'));
  static const VerificationMeta _lastModifiedByMeta =
      const VerificationMeta('lastModifiedBy');
  @override
  late final GeneratedColumn<String> lastModifiedBy = GeneratedColumn<String>(
      'last_modified_by', aliasedName, false,
      type: DriftSqlType.string,
      requiredDuringInsert: true,
      $customConstraints: 'DEFAULT ');
  static const VerificationMeta _lastModifiedDateMeta =
      const VerificationMeta('lastModifiedDate');
  @override
  late final GeneratedColumn<DateTime> lastModifiedDate =
      GeneratedColumn<DateTime>('last_modified_date', aliasedName, false,
          type: DriftSqlType.dateTime,
          requiredDuringInsert: false,
          $customConstraints: 'DEFAULT CURRENT_TIMESTAMP',
          defaultValue: const CustomExpression('CURRENT_TIMESTAMP'));
  @override
  List<GeneratedColumn> get $columns => [
        id,
        voucherNo,
        acctClsCode,
        acctClsName,
        amt,
        drCr,
        remark,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate
      ];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'acct_vou_detail';
  @override
  VerificationContext validateIntegrity(Insertable<AcctVouDetailData> instance,
      {bool isInserting = false}) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('id')) {
      context.handle(_idMeta, id.isAcceptableOrUnknown(data['id']!, _idMeta));
    }
    if (data.containsKey('voucher_no')) {
      context.handle(_voucherNoMeta,
          voucherNo.isAcceptableOrUnknown(data['voucher_no']!, _voucherNoMeta));
    } else if (isInserting) {
      context.missing(_voucherNoMeta);
    }
    if (data.containsKey('acct_cls_code')) {
      context.handle(
          _acctClsCodeMeta,
          acctClsCode.isAcceptableOrUnknown(
              data['acct_cls_code']!, _acctClsCodeMeta));
    } else if (isInserting) {
      context.missing(_acctClsCodeMeta);
    }
    if (data.containsKey('acct_cls_name')) {
      context.handle(
          _acctClsNameMeta,
          acctClsName.isAcceptableOrUnknown(
              data['acct_cls_name']!, _acctClsNameMeta));
    } else if (isInserting) {
      context.missing(_acctClsNameMeta);
    }
    if (data.containsKey('amount')) {
      context.handle(
          _amtMeta, amt.isAcceptableOrUnknown(data['amount']!, _amtMeta));
    } else if (isInserting) {
      context.missing(_amtMeta);
    }
    if (data.containsKey('dr_cr')) {
      context.handle(
          _drCrMeta, drCr.isAcceptableOrUnknown(data['dr_cr']!, _drCrMeta));
    } else if (isInserting) {
      context.missing(_drCrMeta);
    }
    if (data.containsKey('remark')) {
      context.handle(_remarkMeta,
          remark.isAcceptableOrUnknown(data['remark']!, _remarkMeta));
    } else if (isInserting) {
      context.missing(_remarkMeta);
    }
    if (data.containsKey('created_by')) {
      context.handle(_createdByMeta,
          createdBy.isAcceptableOrUnknown(data['created_by']!, _createdByMeta));
    } else if (isInserting) {
      context.missing(_createdByMeta);
    }
    if (data.containsKey('created_date')) {
      context.handle(
          _createdDateMeta,
          createdDate.isAcceptableOrUnknown(
              data['created_date']!, _createdDateMeta));
    }
    if (data.containsKey('last_modified_by')) {
      context.handle(
          _lastModifiedByMeta,
          lastModifiedBy.isAcceptableOrUnknown(
              data['last_modified_by']!, _lastModifiedByMeta));
    } else if (isInserting) {
      context.missing(_lastModifiedByMeta);
    }
    if (data.containsKey('last_modified_date')) {
      context.handle(
          _lastModifiedDateMeta,
          lastModifiedDate.isAcceptableOrUnknown(
              data['last_modified_date']!, _lastModifiedDateMeta));
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {id};
  @override
  AcctVouDetailData map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return AcctVouDetailData(
      id: attachedDatabase.typeMapping
          .read(DriftSqlType.int, data['${effectivePrefix}id'])!,
      voucherNo: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}voucher_no'])!,
      acctClsCode: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}acct_cls_code'])!,
      acctClsName: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}acct_cls_name'])!,
      amt: attachedDatabase.typeMapping
          .read(DriftSqlType.double, data['${effectivePrefix}amount'])!,
      drCr: attachedDatabase.typeMapping
          .read(DriftSqlType.int, data['${effectivePrefix}dr_cr'])!,
      remark: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}remark'])!,
      createdBy: attachedDatabase.typeMapping
          .read(DriftSqlType.string, data['${effectivePrefix}created_by'])!,
      createdDate: attachedDatabase.typeMapping
          .read(DriftSqlType.dateTime, data['${effectivePrefix}created_date'])!,
      lastModifiedBy: attachedDatabase.typeMapping.read(
          DriftSqlType.string, data['${effectivePrefix}last_modified_by'])!,
      lastModifiedDate: attachedDatabase.typeMapping.read(
          DriftSqlType.dateTime, data['${effectivePrefix}last_modified_date'])!,
    );
  }

  @override
  $AcctVouDetailTable createAlias(String alias) {
    return $AcctVouDetailTable(attachedDatabase, alias);
  }
}

class AcctVouDetailData extends DataClass
    implements Insertable<AcctVouDetailData> {
  final int id;
  final String voucherNo;
  final String acctClsCode;
  final String acctClsName;
  final double amt;
  final int drCr;
  final String remark;
  final String createdBy;
  final DateTime createdDate;
  final String lastModifiedBy;
  final DateTime lastModifiedDate;
  const AcctVouDetailData(
      {required this.id,
      required this.voucherNo,
      required this.acctClsCode,
      required this.acctClsName,
      required this.amt,
      required this.drCr,
      required this.remark,
      required this.createdBy,
      required this.createdDate,
      required this.lastModifiedBy,
      required this.lastModifiedDate});
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['id'] = Variable<int>(id);
    map['voucher_no'] = Variable<String>(voucherNo);
    map['acct_cls_code'] = Variable<String>(acctClsCode);
    map['acct_cls_name'] = Variable<String>(acctClsName);
    map['amount'] = Variable<double>(amt);
    map['dr_cr'] = Variable<int>(drCr);
    map['remark'] = Variable<String>(remark);
    map['created_by'] = Variable<String>(createdBy);
    map['created_date'] = Variable<DateTime>(createdDate);
    map['last_modified_by'] = Variable<String>(lastModifiedBy);
    map['last_modified_date'] = Variable<DateTime>(lastModifiedDate);
    return map;
  }

  AcctVouDetailCompanion toCompanion(bool nullToAbsent) {
    return AcctVouDetailCompanion(
      id: Value(id),
      voucherNo: Value(voucherNo),
      acctClsCode: Value(acctClsCode),
      acctClsName: Value(acctClsName),
      amt: Value(amt),
      drCr: Value(drCr),
      remark: Value(remark),
      createdBy: Value(createdBy),
      createdDate: Value(createdDate),
      lastModifiedBy: Value(lastModifiedBy),
      lastModifiedDate: Value(lastModifiedDate),
    );
  }

  factory AcctVouDetailData.fromJson(Map<String, dynamic> json,
      {ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return AcctVouDetailData(
      id: serializer.fromJson<int>(json['id']),
      voucherNo: serializer.fromJson<String>(json['voucherNo']),
      acctClsCode: serializer.fromJson<String>(json['acctClsCode']),
      acctClsName: serializer.fromJson<String>(json['acctClsName']),
      amt: serializer.fromJson<double>(json['amt']),
      drCr: serializer.fromJson<int>(json['drCr']),
      remark: serializer.fromJson<String>(json['remark']),
      createdBy: serializer.fromJson<String>(json['createdBy']),
      createdDate: serializer.fromJson<DateTime>(json['createdDate']),
      lastModifiedBy: serializer.fromJson<String>(json['lastModifiedBy']),
      lastModifiedDate: serializer.fromJson<DateTime>(json['lastModifiedDate']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'id': serializer.toJson<int>(id),
      'voucherNo': serializer.toJson<String>(voucherNo),
      'acctClsCode': serializer.toJson<String>(acctClsCode),
      'acctClsName': serializer.toJson<String>(acctClsName),
      'amt': serializer.toJson<double>(amt),
      'drCr': serializer.toJson<int>(drCr),
      'remark': serializer.toJson<String>(remark),
      'createdBy': serializer.toJson<String>(createdBy),
      'createdDate': serializer.toJson<DateTime>(createdDate),
      'lastModifiedBy': serializer.toJson<String>(lastModifiedBy),
      'lastModifiedDate': serializer.toJson<DateTime>(lastModifiedDate),
    };
  }

  AcctVouDetailData copyWith(
          {int? id,
          String? voucherNo,
          String? acctClsCode,
          String? acctClsName,
          double? amt,
          int? drCr,
          String? remark,
          String? createdBy,
          DateTime? createdDate,
          String? lastModifiedBy,
          DateTime? lastModifiedDate}) =>
      AcctVouDetailData(
        id: id ?? this.id,
        voucherNo: voucherNo ?? this.voucherNo,
        acctClsCode: acctClsCode ?? this.acctClsCode,
        acctClsName: acctClsName ?? this.acctClsName,
        amt: amt ?? this.amt,
        drCr: drCr ?? this.drCr,
        remark: remark ?? this.remark,
        createdBy: createdBy ?? this.createdBy,
        createdDate: createdDate ?? this.createdDate,
        lastModifiedBy: lastModifiedBy ?? this.lastModifiedBy,
        lastModifiedDate: lastModifiedDate ?? this.lastModifiedDate,
      );
  AcctVouDetailData copyWithCompanion(AcctVouDetailCompanion data) {
    return AcctVouDetailData(
      id: data.id.present ? data.id.value : this.id,
      voucherNo: data.voucherNo.present ? data.voucherNo.value : this.voucherNo,
      acctClsCode:
          data.acctClsCode.present ? data.acctClsCode.value : this.acctClsCode,
      acctClsName:
          data.acctClsName.present ? data.acctClsName.value : this.acctClsName,
      amt: data.amt.present ? data.amt.value : this.amt,
      drCr: data.drCr.present ? data.drCr.value : this.drCr,
      remark: data.remark.present ? data.remark.value : this.remark,
      createdBy: data.createdBy.present ? data.createdBy.value : this.createdBy,
      createdDate:
          data.createdDate.present ? data.createdDate.value : this.createdDate,
      lastModifiedBy: data.lastModifiedBy.present
          ? data.lastModifiedBy.value
          : this.lastModifiedBy,
      lastModifiedDate: data.lastModifiedDate.present
          ? data.lastModifiedDate.value
          : this.lastModifiedDate,
    );
  }

  @override
  String toString() {
    return (StringBuffer('AcctVouDetailData(')
          ..write('id: $id, ')
          ..write('voucherNo: $voucherNo, ')
          ..write('acctClsCode: $acctClsCode, ')
          ..write('acctClsName: $acctClsName, ')
          ..write('amt: $amt, ')
          ..write('drCr: $drCr, ')
          ..write('remark: $remark, ')
          ..write('createdBy: $createdBy, ')
          ..write('createdDate: $createdDate, ')
          ..write('lastModifiedBy: $lastModifiedBy, ')
          ..write('lastModifiedDate: $lastModifiedDate')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode => Object.hash(id, voucherNo, acctClsCode, acctClsName, amt,
      drCr, remark, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is AcctVouDetailData &&
          other.id == this.id &&
          other.voucherNo == this.voucherNo &&
          other.acctClsCode == this.acctClsCode &&
          other.acctClsName == this.acctClsName &&
          other.amt == this.amt &&
          other.drCr == this.drCr &&
          other.remark == this.remark &&
          other.createdBy == this.createdBy &&
          other.createdDate == this.createdDate &&
          other.lastModifiedBy == this.lastModifiedBy &&
          other.lastModifiedDate == this.lastModifiedDate);
}

class AcctVouDetailCompanion extends UpdateCompanion<AcctVouDetailData> {
  final Value<int> id;
  final Value<String> voucherNo;
  final Value<String> acctClsCode;
  final Value<String> acctClsName;
  final Value<double> amt;
  final Value<int> drCr;
  final Value<String> remark;
  final Value<String> createdBy;
  final Value<DateTime> createdDate;
  final Value<String> lastModifiedBy;
  final Value<DateTime> lastModifiedDate;
  const AcctVouDetailCompanion({
    this.id = const Value.absent(),
    this.voucherNo = const Value.absent(),
    this.acctClsCode = const Value.absent(),
    this.acctClsName = const Value.absent(),
    this.amt = const Value.absent(),
    this.drCr = const Value.absent(),
    this.remark = const Value.absent(),
    this.createdBy = const Value.absent(),
    this.createdDate = const Value.absent(),
    this.lastModifiedBy = const Value.absent(),
    this.lastModifiedDate = const Value.absent(),
  });
  AcctVouDetailCompanion.insert({
    this.id = const Value.absent(),
    required String voucherNo,
    required String acctClsCode,
    required String acctClsName,
    required double amt,
    required int drCr,
    required String remark,
    required String createdBy,
    this.createdDate = const Value.absent(),
    required String lastModifiedBy,
    this.lastModifiedDate = const Value.absent(),
  })  : voucherNo = Value(voucherNo),
        acctClsCode = Value(acctClsCode),
        acctClsName = Value(acctClsName),
        amt = Value(amt),
        drCr = Value(drCr),
        remark = Value(remark),
        createdBy = Value(createdBy),
        lastModifiedBy = Value(lastModifiedBy);
  static Insertable<AcctVouDetailData> custom({
    Expression<int>? id,
    Expression<String>? voucherNo,
    Expression<String>? acctClsCode,
    Expression<String>? acctClsName,
    Expression<double>? amt,
    Expression<int>? drCr,
    Expression<String>? remark,
    Expression<String>? createdBy,
    Expression<DateTime>? createdDate,
    Expression<String>? lastModifiedBy,
    Expression<DateTime>? lastModifiedDate,
  }) {
    return RawValuesInsertable({
      if (id != null) 'id': id,
      if (voucherNo != null) 'voucher_no': voucherNo,
      if (acctClsCode != null) 'acct_cls_code': acctClsCode,
      if (acctClsName != null) 'acct_cls_name': acctClsName,
      if (amt != null) 'amount': amt,
      if (drCr != null) 'dr_cr': drCr,
      if (remark != null) 'remark': remark,
      if (createdBy != null) 'created_by': createdBy,
      if (createdDate != null) 'created_date': createdDate,
      if (lastModifiedBy != null) 'last_modified_by': lastModifiedBy,
      if (lastModifiedDate != null) 'last_modified_date': lastModifiedDate,
    });
  }

  AcctVouDetailCompanion copyWith(
      {Value<int>? id,
      Value<String>? voucherNo,
      Value<String>? acctClsCode,
      Value<String>? acctClsName,
      Value<double>? amt,
      Value<int>? drCr,
      Value<String>? remark,
      Value<String>? createdBy,
      Value<DateTime>? createdDate,
      Value<String>? lastModifiedBy,
      Value<DateTime>? lastModifiedDate}) {
    return AcctVouDetailCompanion(
      id: id ?? this.id,
      voucherNo: voucherNo ?? this.voucherNo,
      acctClsCode: acctClsCode ?? this.acctClsCode,
      acctClsName: acctClsName ?? this.acctClsName,
      amt: amt ?? this.amt,
      drCr: drCr ?? this.drCr,
      remark: remark ?? this.remark,
      createdBy: createdBy ?? this.createdBy,
      createdDate: createdDate ?? this.createdDate,
      lastModifiedBy: lastModifiedBy ?? this.lastModifiedBy,
      lastModifiedDate: lastModifiedDate ?? this.lastModifiedDate,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (id.present) {
      map['id'] = Variable<int>(id.value);
    }
    if (voucherNo.present) {
      map['voucher_no'] = Variable<String>(voucherNo.value);
    }
    if (acctClsCode.present) {
      map['acct_cls_code'] = Variable<String>(acctClsCode.value);
    }
    if (acctClsName.present) {
      map['acct_cls_name'] = Variable<String>(acctClsName.value);
    }
    if (amt.present) {
      map['amount'] = Variable<double>(amt.value);
    }
    if (drCr.present) {
      map['dr_cr'] = Variable<int>(drCr.value);
    }
    if (remark.present) {
      map['remark'] = Variable<String>(remark.value);
    }
    if (createdBy.present) {
      map['created_by'] = Variable<String>(createdBy.value);
    }
    if (createdDate.present) {
      map['created_date'] = Variable<DateTime>(createdDate.value);
    }
    if (lastModifiedBy.present) {
      map['last_modified_by'] = Variable<String>(lastModifiedBy.value);
    }
    if (lastModifiedDate.present) {
      map['last_modified_date'] = Variable<DateTime>(lastModifiedDate.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('AcctVouDetailCompanion(')
          ..write('id: $id, ')
          ..write('voucherNo: $voucherNo, ')
          ..write('acctClsCode: $acctClsCode, ')
          ..write('acctClsName: $acctClsName, ')
          ..write('amt: $amt, ')
          ..write('drCr: $drCr, ')
          ..write('remark: $remark, ')
          ..write('createdBy: $createdBy, ')
          ..write('createdDate: $createdDate, ')
          ..write('lastModifiedBy: $lastModifiedBy, ')
          ..write('lastModifiedDate: $lastModifiedDate')
          ..write(')'))
        .toString();
  }
}

abstract class _$MyDatabase extends GeneratedDatabase {
  _$MyDatabase(QueryExecutor e) : super(e);
  $MyDatabaseManager get managers => $MyDatabaseManager(this);
  late final $UserTable user = $UserTable(this);
  late final $AcctVouDetailTable acctVouDetail = $AcctVouDetailTable(this);
  @override
  Iterable<TableInfo<Table, Object?>> get allTables =>
      allSchemaEntities.whereType<TableInfo<Table, Object?>>();
  @override
  List<DatabaseSchemaEntity> get allSchemaEntities => [user, acctVouDetail];
}

typedef $$UserTableCreateCompanionBuilder = UserCompanion Function({
  Value<int> id,
  required String name,
  required String login,
});
typedef $$UserTableUpdateCompanionBuilder = UserCompanion Function({
  Value<int> id,
  Value<String> name,
  Value<String> login,
});

class $$UserTableFilterComposer extends Composer<_$MyDatabase, $UserTable> {
  $$UserTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get id => $composableBuilder(
      column: $table.id, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get name => $composableBuilder(
      column: $table.name, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get login => $composableBuilder(
      column: $table.login, builder: (column) => ColumnFilters(column));
}

class $$UserTableOrderingComposer extends Composer<_$MyDatabase, $UserTable> {
  $$UserTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get id => $composableBuilder(
      column: $table.id, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get name => $composableBuilder(
      column: $table.name, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get login => $composableBuilder(
      column: $table.login, builder: (column) => ColumnOrderings(column));
}

class $$UserTableAnnotationComposer extends Composer<_$MyDatabase, $UserTable> {
  $$UserTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get id =>
      $composableBuilder(column: $table.id, builder: (column) => column);

  GeneratedColumn<String> get name =>
      $composableBuilder(column: $table.name, builder: (column) => column);

  GeneratedColumn<String> get login =>
      $composableBuilder(column: $table.login, builder: (column) => column);
}

class $$UserTableTableManager extends RootTableManager<
    _$MyDatabase,
    $UserTable,
    UserData,
    $$UserTableFilterComposer,
    $$UserTableOrderingComposer,
    $$UserTableAnnotationComposer,
    $$UserTableCreateCompanionBuilder,
    $$UserTableUpdateCompanionBuilder,
    (UserData, BaseReferences<_$MyDatabase, $UserTable, UserData>),
    UserData,
    PrefetchHooks Function()> {
  $$UserTableTableManager(_$MyDatabase db, $UserTable table)
      : super(TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$UserTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$UserTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$UserTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback: ({
            Value<int> id = const Value.absent(),
            Value<String> name = const Value.absent(),
            Value<String> login = const Value.absent(),
          }) =>
              UserCompanion(
            id: id,
            name: name,
            login: login,
          ),
          createCompanionCallback: ({
            Value<int> id = const Value.absent(),
            required String name,
            required String login,
          }) =>
              UserCompanion.insert(
            id: id,
            name: name,
            login: login,
          ),
          withReferenceMapper: (p0) => p0
              .map((e) => (e.readTable(table), BaseReferences(db, table, e)))
              .toList(),
          prefetchHooksCallback: null,
        ));
}

typedef $$UserTableProcessedTableManager = ProcessedTableManager<
    _$MyDatabase,
    $UserTable,
    UserData,
    $$UserTableFilterComposer,
    $$UserTableOrderingComposer,
    $$UserTableAnnotationComposer,
    $$UserTableCreateCompanionBuilder,
    $$UserTableUpdateCompanionBuilder,
    (UserData, BaseReferences<_$MyDatabase, $UserTable, UserData>),
    UserData,
    PrefetchHooks Function()>;
typedef $$AcctVouDetailTableCreateCompanionBuilder = AcctVouDetailCompanion
    Function({
  Value<int> id,
  required String voucherNo,
  required String acctClsCode,
  required String acctClsName,
  required double amt,
  required int drCr,
  required String remark,
  required String createdBy,
  Value<DateTime> createdDate,
  required String lastModifiedBy,
  Value<DateTime> lastModifiedDate,
});
typedef $$AcctVouDetailTableUpdateCompanionBuilder = AcctVouDetailCompanion
    Function({
  Value<int> id,
  Value<String> voucherNo,
  Value<String> acctClsCode,
  Value<String> acctClsName,
  Value<double> amt,
  Value<int> drCr,
  Value<String> remark,
  Value<String> createdBy,
  Value<DateTime> createdDate,
  Value<String> lastModifiedBy,
  Value<DateTime> lastModifiedDate,
});

class $$AcctVouDetailTableFilterComposer
    extends Composer<_$MyDatabase, $AcctVouDetailTable> {
  $$AcctVouDetailTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get id => $composableBuilder(
      column: $table.id, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get voucherNo => $composableBuilder(
      column: $table.voucherNo, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get acctClsCode => $composableBuilder(
      column: $table.acctClsCode, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get acctClsName => $composableBuilder(
      column: $table.acctClsName, builder: (column) => ColumnFilters(column));

  ColumnFilters<double> get amt => $composableBuilder(
      column: $table.amt, builder: (column) => ColumnFilters(column));

  ColumnFilters<int> get drCr => $composableBuilder(
      column: $table.drCr, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get remark => $composableBuilder(
      column: $table.remark, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get createdBy => $composableBuilder(
      column: $table.createdBy, builder: (column) => ColumnFilters(column));

  ColumnFilters<DateTime> get createdDate => $composableBuilder(
      column: $table.createdDate, builder: (column) => ColumnFilters(column));

  ColumnFilters<String> get lastModifiedBy => $composableBuilder(
      column: $table.lastModifiedBy,
      builder: (column) => ColumnFilters(column));

  ColumnFilters<DateTime> get lastModifiedDate => $composableBuilder(
      column: $table.lastModifiedDate,
      builder: (column) => ColumnFilters(column));
}

class $$AcctVouDetailTableOrderingComposer
    extends Composer<_$MyDatabase, $AcctVouDetailTable> {
  $$AcctVouDetailTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get id => $composableBuilder(
      column: $table.id, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get voucherNo => $composableBuilder(
      column: $table.voucherNo, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get acctClsCode => $composableBuilder(
      column: $table.acctClsCode, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get acctClsName => $composableBuilder(
      column: $table.acctClsName, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<double> get amt => $composableBuilder(
      column: $table.amt, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<int> get drCr => $composableBuilder(
      column: $table.drCr, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get remark => $composableBuilder(
      column: $table.remark, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get createdBy => $composableBuilder(
      column: $table.createdBy, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<DateTime> get createdDate => $composableBuilder(
      column: $table.createdDate, builder: (column) => ColumnOrderings(column));

  ColumnOrderings<String> get lastModifiedBy => $composableBuilder(
      column: $table.lastModifiedBy,
      builder: (column) => ColumnOrderings(column));

  ColumnOrderings<DateTime> get lastModifiedDate => $composableBuilder(
      column: $table.lastModifiedDate,
      builder: (column) => ColumnOrderings(column));
}

class $$AcctVouDetailTableAnnotationComposer
    extends Composer<_$MyDatabase, $AcctVouDetailTable> {
  $$AcctVouDetailTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get id =>
      $composableBuilder(column: $table.id, builder: (column) => column);

  GeneratedColumn<String> get voucherNo =>
      $composableBuilder(column: $table.voucherNo, builder: (column) => column);

  GeneratedColumn<String> get acctClsCode => $composableBuilder(
      column: $table.acctClsCode, builder: (column) => column);

  GeneratedColumn<String> get acctClsName => $composableBuilder(
      column: $table.acctClsName, builder: (column) => column);

  GeneratedColumn<double> get amt =>
      $composableBuilder(column: $table.amt, builder: (column) => column);

  GeneratedColumn<int> get drCr =>
      $composableBuilder(column: $table.drCr, builder: (column) => column);

  GeneratedColumn<String> get remark =>
      $composableBuilder(column: $table.remark, builder: (column) => column);

  GeneratedColumn<String> get createdBy =>
      $composableBuilder(column: $table.createdBy, builder: (column) => column);

  GeneratedColumn<DateTime> get createdDate => $composableBuilder(
      column: $table.createdDate, builder: (column) => column);

  GeneratedColumn<String> get lastModifiedBy => $composableBuilder(
      column: $table.lastModifiedBy, builder: (column) => column);

  GeneratedColumn<DateTime> get lastModifiedDate => $composableBuilder(
      column: $table.lastModifiedDate, builder: (column) => column);
}

class $$AcctVouDetailTableTableManager extends RootTableManager<
    _$MyDatabase,
    $AcctVouDetailTable,
    AcctVouDetailData,
    $$AcctVouDetailTableFilterComposer,
    $$AcctVouDetailTableOrderingComposer,
    $$AcctVouDetailTableAnnotationComposer,
    $$AcctVouDetailTableCreateCompanionBuilder,
    $$AcctVouDetailTableUpdateCompanionBuilder,
    (
      AcctVouDetailData,
      BaseReferences<_$MyDatabase, $AcctVouDetailTable, AcctVouDetailData>
    ),
    AcctVouDetailData,
    PrefetchHooks Function()> {
  $$AcctVouDetailTableTableManager(_$MyDatabase db, $AcctVouDetailTable table)
      : super(TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$AcctVouDetailTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$AcctVouDetailTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$AcctVouDetailTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback: ({
            Value<int> id = const Value.absent(),
            Value<String> voucherNo = const Value.absent(),
            Value<String> acctClsCode = const Value.absent(),
            Value<String> acctClsName = const Value.absent(),
            Value<double> amt = const Value.absent(),
            Value<int> drCr = const Value.absent(),
            Value<String> remark = const Value.absent(),
            Value<String> createdBy = const Value.absent(),
            Value<DateTime> createdDate = const Value.absent(),
            Value<String> lastModifiedBy = const Value.absent(),
            Value<DateTime> lastModifiedDate = const Value.absent(),
          }) =>
              AcctVouDetailCompanion(
            id: id,
            voucherNo: voucherNo,
            acctClsCode: acctClsCode,
            acctClsName: acctClsName,
            amt: amt,
            drCr: drCr,
            remark: remark,
            createdBy: createdBy,
            createdDate: createdDate,
            lastModifiedBy: lastModifiedBy,
            lastModifiedDate: lastModifiedDate,
          ),
          createCompanionCallback: ({
            Value<int> id = const Value.absent(),
            required String voucherNo,
            required String acctClsCode,
            required String acctClsName,
            required double amt,
            required int drCr,
            required String remark,
            required String createdBy,
            Value<DateTime> createdDate = const Value.absent(),
            required String lastModifiedBy,
            Value<DateTime> lastModifiedDate = const Value.absent(),
          }) =>
              AcctVouDetailCompanion.insert(
            id: id,
            voucherNo: voucherNo,
            acctClsCode: acctClsCode,
            acctClsName: acctClsName,
            amt: amt,
            drCr: drCr,
            remark: remark,
            createdBy: createdBy,
            createdDate: createdDate,
            lastModifiedBy: lastModifiedBy,
            lastModifiedDate: lastModifiedDate,
          ),
          withReferenceMapper: (p0) => p0
              .map((e) => (e.readTable(table), BaseReferences(db, table, e)))
              .toList(),
          prefetchHooksCallback: null,
        ));
}

typedef $$AcctVouDetailTableProcessedTableManager = ProcessedTableManager<
    _$MyDatabase,
    $AcctVouDetailTable,
    AcctVouDetailData,
    $$AcctVouDetailTableFilterComposer,
    $$AcctVouDetailTableOrderingComposer,
    $$AcctVouDetailTableAnnotationComposer,
    $$AcctVouDetailTableCreateCompanionBuilder,
    $$AcctVouDetailTableUpdateCompanionBuilder,
    (
      AcctVouDetailData,
      BaseReferences<_$MyDatabase, $AcctVouDetailTable, AcctVouDetailData>
    ),
    AcctVouDetailData,
    PrefetchHooks Function()>;

class $MyDatabaseManager {
  final _$MyDatabase _db;
  $MyDatabaseManager(this._db);
  $$UserTableTableManager get user => $$UserTableTableManager(_db, _db.user);
  $$AcctVouDetailTableTableManager get acctVouDetail =>
      $$AcctVouDetailTableTableManager(_db, _db.acctVouDetail);
}
