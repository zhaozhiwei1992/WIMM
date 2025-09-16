class User {
  final String id;
  final String name;
  final String login;

  User({required this.id, required this.name, required this.login});

  Map<String, Object?> toMap() {
    return {
      'id': id,
      'name': name,
      'login': login,
    };
  }

  // toString
  @override
  String toString() {
    return 'User{id: $id, name: $name, login: $login}';
  }

  factory User.fromMap(Map<String, dynamic> map) {
    return User(
      id: map['id'] as String,
      name: map['name'] as String,
      login: map['login'] as String,
    );
  }
}