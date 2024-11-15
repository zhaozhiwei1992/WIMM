class AccountCls {
  final String id;
  final String name;
  final List<AccountCls> children;

  AccountCls({required this.id, required this.name, this.children = const []});

  factory AccountCls.fromJson(Map<String, dynamic> json) {
    return AccountCls(
      id: json['id'],
      name: json['label'],
      children: (json['children'] as List?)
          ?.map((child) => AccountCls.fromJson(child))
          .toList() ?? [],
    );
  }
}