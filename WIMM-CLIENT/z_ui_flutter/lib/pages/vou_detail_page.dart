
import 'package:flutter/material.dart';

class VouDetailPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: TextButton(
          onPressed: () {
            print('分录明细页面，支持导出');
          },
          child: Text('Acct'),
        ),
      ),
    );
  }
}