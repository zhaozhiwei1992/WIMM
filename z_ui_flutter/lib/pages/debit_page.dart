import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:z_ui_flutter/model/account_cls.dart';
import 'package:z_ui_flutter/resources/app_resources.dart';
import 'package:z_ui_flutter/widgets/indicator.dart';
import 'package:z_ui_flutter/resources/app_colors.dart';
import 'dart:convert';
import 'dart:async';
import 'package:flutter/services.dart';

class DebitPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => DebitPageState();
}

class DebitPageState extends State {
  int touchedIndex = -1;

  var colors = [
    AppColors.colorBlue,
    AppColors.colorYellow,
    AppColors.colorOrange,
    AppColors.colorGreen,
    AppColors.colorPurple,
    AppColors.colorPink,
    AppColors.colorRed,
    AppColors.colorCyan,
    AppColors.colorTeal,
    AppColors.colorMint,
    AppColors.colorLime,
  ];

  List<AccountCls> _accountcls = [];
  List<AccountCls> debitAccounts = [];
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
      debitAccounts = _accountcls[3].children;
      count = debitAccounts.length;
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
                text: debitAccounts[i].name,
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
