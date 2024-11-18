import 'package:flutter/material.dart';
import 'package:z_ui_flutter/pages/account_page.dart';
import 'package:z_ui_flutter/pages/credit_page.dart';
import 'package:z_ui_flutter/pages/debit_page.dart';
import 'package:z_ui_flutter/pages/login_page.dart';
import 'package:z_ui_flutter/pages/vou_detail_page.dart';

class HomePage extends StatefulWidget {
  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  var selectedIndex = 0;
  @override
  Widget build(BuildContext context) {
    Widget page;
    switch (selectedIndex) {
      case 0:
        page = AccountFormPage();
        break;
      case 1:
        page = CreditPage();
        break;
      case 2:
        page = DebitPage();
        break;
      case 3:
        page = VouDetailPage();
        break;
      case 4:
        page  = LoginPage();
        break;
      default:
        throw UnimplementedError('no widget for $selectedIndex');
    }

    return LayoutBuilder(
      builder: (context, constraints) {
        return Scaffold(
          body: Row(
            children: [
              SafeArea(
                child: NavigationRail(
                  //显示图标旁边的标签
                  extended: constraints.maxWidth >= 600,
                  destinations: [
                    NavigationRailDestination(
                      icon: Icon(Icons.account_balance_wallet),
                      label: Text('记账'),
                    ),
                    NavigationRailDestination(
                      icon: Icon(Icons.money),
                      label: Text('收入情况'),
                    ),
                    NavigationRailDestination(
                      icon: Icon(Icons.monetization_on),
                      label: Text('支出情况'),
                    ),
                    NavigationRailDestination(
                      icon: Icon(Icons.description),
                      label: Text('明细分录'),
                    ),
                    NavigationRailDestination(
                      icon: Icon(Icons.lock),
                      label: Text('注册登录'),
                    ),
                  ],
                  selectedIndex: selectedIndex,
                  onDestinationSelected: (value) {
                    // print('selected: $value');
                    setState(() {
                      selectedIndex = value;
                    });
                  },
                ),
              ),
              Expanded(
                child: Container(
                  color: Theme.of(context).colorScheme.primaryContainer,
                  child: page,
                ),
              ),
            ],
          ),
        );
      }
    );
  }
}