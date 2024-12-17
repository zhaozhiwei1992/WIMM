import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:z_ui_flutter/model/account_cls.dart';
import 'package:z_ui_flutter/model/account_vou_detail.dart';
import 'package:z_ui_flutter/repository/account_vou_detail_repository.dart';
import 'package:z_ui_flutter/services/database_helper_service.dart';

class AccountFormPage extends StatefulWidget {
  @override
  _AccountFormPageState createState() => _AccountFormPageState();
}

class _AccountFormPageState extends State<AccountFormPage> {
  final _formKey = GlobalKey<FormState>();
  String _creditAccount = '';
  String _debitAccount = '';
  // late List<AccountCls> _accountcls;
  List<AccountCls>? _accountcls;

  @override
  void initState() {
    super.initState();
    _loadAccounts();
  }

  Future<void> _loadAccounts() async {
    final jsonString = await rootBundle.loadString('assets/data/account_cls.json');
    final jsonList = json.decode(jsonString) as List;
    setState(() {
      _accountcls = jsonList.map((accountJson) => AccountCls.fromJson(accountJson)).toList();
    });
  }

  // Widget _buildTreeView(String value) {
  //   return TreeView(
  //     sections: [
  //       TreeViewSection(
  //         header: Text('选择科目'),
  //         nodes: _accountcls.map((account) {
  //           return TreeViewNode(
  //             id: account.id,
  //             content: account.name,
  //             children: account.children.map((child) => TreeViewNode(
  //               id: child.id,
  //               content: child.name,
  //             )).toList(),
  //           );
  //         }).toList(),
  //       ],
  //       onNodeTap: (node) {
  //         setState(() {
  //           if (node.parent == null) {
  //             _creditAccount = node.content;
  //           } else if (node.parent?.parent == null) {
  //             _debitAccount = node.content;
  //           }
  //         });
  //       },
  //     )
  //   )
  // }

  void _submitForm() {
    if (_formKey.currentState!.validate()) {
      // 处理表单提交逻辑
      print('贷方: $_creditAccount, 借方: $_debitAccount');

      final dbHelper = DatabaseService.databaseHelper;

      dbHelper.insertAcctVouDetail(AcctVouDetail(
        id: '1',
        voucherNo: '1',
        acctClsCode: '1',
        acctClsName: '1',
        amt: '1',
        drCr: '1',
        remark: '1',
        createdBy: '1',
        createdDate: '1',
        lastModifiedBy: '1',
      ));

      // 清空表单字段
      _creditAccount = '';
      _debitAccount = '';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('记账'),
      ),
      body: Form(
        key: _formKey,
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              DropdownButtonFormField<String>(
                value: _creditAccount.isEmpty ? null : _creditAccount,
                decoration: InputDecoration(labelText: '贷方科目'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '请选择贷方科目';
                  }
                  return null;
                },
                onChanged: (value) {
                  setState(() {
                    _creditAccount = value ?? '';
                  });
                },
                items: _accountcls?.map((account) {
                  return DropdownMenuItem<String>(
                    value: account.name,
                    child: Text(account.name),
                  );
                }).toList(),
              ),
              DropdownButtonFormField<String>(
                value: _debitAccount.isEmpty ? null : _debitAccount,
                decoration: InputDecoration(labelText: '借方科目'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '请选择借方科目';
                  }
                  return null;
                },
                onChanged: (value) {
                  setState(() {
                    _debitAccount = value ?? '';
                  });
                },
                items: _accountcls?.map((account) {
                  return DropdownMenuItem<String>(
                    value: account.name,
                    child: Text(account.name),
                  );
                }).toList(),
              ),
              TextFormField(
                decoration: InputDecoration(labelText: '金额'),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || value.isEmpty || double.tryParse(value) == null) {
                    return '请输入有效的金额';
                  }
                  return null;
                },
              ),
              TextFormField(
                decoration: InputDecoration(labelText: '摘要'),
              ),
              ElevatedButton(
                onPressed: _submitForm,
                child: Text('提交'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

