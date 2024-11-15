import 'package:flutter/material.dart';

class TreeNode {
  final String id;
  final String label;
  final List<TreeNode> children;

  TreeNode({required this.id, required this.label, this.children = const []});
}

class MyTreeView extends StatelessWidget {
  final List<TreeNode> nodes;

  MyTreeView({required this.nodes});

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: nodes.length,
      itemBuilder: (context, index) {
        final node = nodes[index];
        return ExpansionTile(
          title: Text(node.label),
          children: node.children.map((child) {
            return ListTile(
              title: Text(child.label),
              onTap: () {
                // Handle node tap
                print('Tapped on ${child.label}');
              },
            );
          }).toList(),
        );
      },
    );
  }
}