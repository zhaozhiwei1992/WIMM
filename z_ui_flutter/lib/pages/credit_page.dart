

import 'package:flutter/material.dart';

class CreditPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: TextButton(
          onPressed: () {
            print('贷方图标页面');
          },
          child: Text('Home'),
        ),
      ),
    );
  }
}