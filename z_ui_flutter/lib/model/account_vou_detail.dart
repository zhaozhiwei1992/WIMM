class AcctVouDetail {
  final String id;
  final String voucherNo;
  final String acctClsCode;
  final String acctClsName;
  final String amt;
  final String drCr;
  final String remark;
  final String createdBy;
  final String createdDate;
  final String lastModifiedBy;

  AcctVouDetail(
      {required this.id,
      required this.voucherNo,
      required this.acctClsCode,
      required this.acctClsName,
      required this.amt,
      required this.drCr,
      required this.remark,
      required this.createdBy,
      required this.createdDate,
      required this.lastModifiedBy});

  // toString
  @override
  String toString() {
    return 'AcctVouDetail{id: $id, voucherNo: $voucherNo, acctClsCode: $acctClsCode, acctClsName: $acctClsName, amt: $amt, drCr: $drCr, remark: $remark, createdBy: $createdBy, createdDate: $createdDate, lastModifiedBy: $lastModifiedBy}';
  }

  // toMap
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'voucherNo': voucherNo,
      'acctClsCode': acctClsCode,
      'acctClsName': acctClsName,
      'amt': amt,
      'drCr': drCr,
      'remark': remark,
      'createdBy': createdBy,
    };
  }

  // fromMap
  factory AcctVouDetail.fromMap(Map<String, dynamic> map) {
    return AcctVouDetail(
      id: map['id'],
      voucherNo: map['voucherNo'],
      acctClsCode: map['acctClsCode'],
      acctClsName: map['acctClsName'],
      amt: map['amt'],
      drCr: map['drCr'],
      remark: map['remark'],
      createdBy: map['createdBy'],
      createdDate: map['createdDate'],
      lastModifiedBy: map['lastModifiedBy'],
    );
  }
}
