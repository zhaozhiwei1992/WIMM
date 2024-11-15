

import 'package:flutter/material.dart';

class DebitPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: TextButton(
          onPressed: () {
            print('借方图标页面');
          },
          child: Text('Home'),
        ),
      ),
    );
  }
}