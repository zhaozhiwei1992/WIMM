import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:z_ui_flutter/model/account_cls.dart';
import 'package:z_ui_flutter/resources/app_resources.dart';
import 'package:z_ui_flutter/widgets/indicator.dart';
import 'dart:convert';
import 'dart:async';
import 'package:flutter/services.dart';

class CreditPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => CreditPageState();
}

class CreditPageState extends State {
  int touchedIndex = -1;

  var colors = [
    Colors.red,
    Colors.blue,
    Colors.green,
    Colors.yellow,
  ];

  List<AccountCls> _accountcls = [];
  List<AccountCls> creditAccounts = [];
  int count = -1;

  @override
  void initState() {
    super.initState();
    _loadAccounts();
    //TODO 获取统计信息
  }

  Future<void> _loadAccounts() async {
    final jsonString = await rootBundle.loadString('assets/data/account_cls.json');
    final jsonList = json.decode(jsonString) as List;
    setState(() {
      _accountcls = jsonList.map((accountJson) => AccountCls.fromJson(accountJson)).toList();
      creditAccounts = _accountcls[2].children;
      count = creditAccounts.length;
    });
  }

  @override
  Widget build(BuildContext context) {
    return AspectRatio(
      aspectRatio: 1.3,
      child: Column(
        children: <Widget>[
          const SizedBox(
            height: 28,
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: List.generate(count, (i) {
              return Indicator(
                color: colors[i],
                text: creditAccounts[i].name,
                isSquare: false,
                size: touchedIndex == 0 ? 18 : 16,
                textColor: touchedIndex == i
                    ? AppColors.mainTextColor1
                    : AppColors.mainTextColor3,
              );
            })
          ),
          const SizedBox(
            height: 18,
          ),
          Expanded(
            child: AspectRatio(
              aspectRatio: 1,
              child: PieChart(
                PieChartData(
                  pieTouchData: PieTouchData(
                    touchCallback: (FlTouchEvent event, pieTouchResponse) {
                      setState(() {
                        if (!event.isInterestedForInteractions ||
                            pieTouchResponse == null ||
                            pieTouchResponse.touchedSection == null) {
                          touchedIndex = -1;
                          return;
                        }
                        touchedIndex = pieTouchResponse
                            .touchedSection!.touchedSectionIndex;
                      });
                    },
                  ),
                  startDegreeOffset: 180,
                  borderData: FlBorderData(
                    show: false,
                  ),
                  sectionsSpace: 1,
                  centerSpaceRadius: 0,
                  sections: showingSections(),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  List<PieChartSectionData> showingSections() {
    return List.generate(count, (i) {
      final isTouched = i == touchedIndex;
      return PieChartSectionData(
        color: colors[i],
        value: 25,
        title: '',
        radius: 80,
        titlePositionPercentageOffset: 0.55,
        borderSide: isTouched
            ? BorderSide(color: colors[i], width: 6)
            : BorderSide(color: colors[i].withOpacity(0)),
      );
    });
  }
}
