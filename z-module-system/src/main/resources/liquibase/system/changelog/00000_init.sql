--liquibase formatted sql
--changeset zzw:00000

-- 判断表不存在先创建
-- 用户表
CREATE TABLE if not exists sys_user (
	id bigint primary key auto_increment,
	created_by varchar(50) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(50) NULL,
	last_modified_date timestamp NULL,
	activated bool NOT NULL,
	image_url varchar(256) NULL,
	login varchar(50) NOT NULL,
	name varchar(50) NULL,
	password_hash varchar(60) NOT NULL
);

CREATE TABLE if not exists sys_authority (
	id bigint primary key auto_increment,
	created_by varchar(50) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(50) NULL,
	last_modified_date timestamp NULL,
	code varchar(50) NOT NULL,
	name varchar(50) NOT NULL
);

CREATE TABLE if not exists sys_user_authority (
	id bigint primary key auto_increment,
	created_by varchar(50) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(50) NULL,
	last_modified_date timestamp NULL,
	user_id int8 NOT NULL,
	role_id int8 NOT NULL
);

CREATE TABLE if not exists sys_menu (
	id bigint primary key auto_increment,
	created_by varchar(50) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(50) NULL,
	last_modified_date timestamp NULL,
	config varchar(255) NULL,
	enabled bool NULL,
	icon_cls varchar(255) NULL,
	keep_alive bool NULL,
	name varchar(255) NOT NULL,
	order_num int4 NULL,
	parent_id int8 NOT NULL,
	require_auth bool NULL,
	url varchar(255) NOT NULL,
	component varchar(255) NOT NULL,
    menu_type  int(1)                default 1       comment '菜单类型（1菜单 2按钮）',
    permission_code  varchar(100)     default null     comment '权限标识'
);


CREATE TABLE if not exists sys_param (
	id bigint primary key auto_increment,
	created_by varchar(50) NULL,
	created_date timestamp NULL,
	last_modified_by varchar(50) NULL,
	last_modified_date timestamp NULL,
	code varchar(255) NULL,
	enable bool NULL,
	name varchar(255) NULL,
	remark varchar(255) NULL,
	value varchar(255) NULL
);

-- 权限
CREATE TABLE if not exists sys_permissions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code varchar(64)     not null                    comment '权限编码',
  name VARCHAR(255) NOT NULL UNIQUE,
  type int(1) NOT NULL                            comment '权限类型,1:数据权限',
  created_by varchar(50) NULL,
  created_date timestamp NULL,
  last_modified_by varchar(50) NULL,
  last_modified_date timestamp NULL,
  description VARCHAR(255)
);

delete from sys_permissions;
INSERT INTO sys_permissions (
  code, name, type, created_by, created_date, last_modified_by, last_modified_date, description
) VALUES
('ALL_DATA', '全部数据权限', 1, 'system', NOW(), 'system', NOW(), '访问所有数据的权限'),
('CUSTOM_DATA', '自定义数据权限', 1, 'system', NOW(), 'system', NOW(), '根据自定义规则访问数据的权限'),
('DEPT_DATA', '本部门数据权限', 1, 'system', NOW(), 'system', NOW(), '只能访问本部门数据的权限'),
('DEPT_SUB_DATA', '本部门及以下数据权限', 1, 'system', NOW(), 'system', NOW(), '可以访问本部门及下属部门数据的权限');

-- ----------------------------
-- 用户角色初始化
-- ----------------------------
delete from sys_user;
INSERT INTO sys_user
(id, created_by, created_date, last_modified_by, last_modified_date, activated, image_url, login, name, password_hash)
VALUES
(1, 'system', '2022-07-14 11:25:56.594', 'system', '2022-07-27 09:05:07.221', true, NULL, 'admin', '系统管理员', '$2a$10$Uaq/uIj3D5VZ4Y5.I7MTB.pMXka6FKuCNy4A.ZnnRk9GshwYxBQZG');

-- 初始化序列
alter table sys_user auto_increment=2;

delete from sys_authority;
-- 初始化角色数据
INSERT INTO sys_authority
(created_by, created_date, last_modified_by, last_modified_date, code, name)
VALUES
('system', NOW(), 'system', NOW(), 'ROLE_ADMIN', '管理员'),
('system', NOW(), 'system', NOW(), 'ROLE_USER', '用户');

delete from sys_user_authority;
INSERT INTO sys_user_authority
(user_id, role_id)
VALUES(1, 1);

-- ----------------------------
-- 菜单初始化
-- ----------------------------
delete from sys_menu;

INSERT INTO sys_menu (
  id, created_by, created_date, last_modified_by, last_modified_date, config, enabled, icon_cls, keep_alive, name, order_num, parent_id, require_auth, url, component, menu_type, permission_code
) VALUES
-- 一级菜单
(1, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:home-filled', TRUE, '首页',1, 0, TRUE, '/dashboard', '#', '0', 'dashboard:'),
(2, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:setting-filled', TRUE, '系统管理',2, 0, TRUE, '/system', '#', '0', 'system:'),
(3, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:code-outlined', TRUE, '开发者工具',3, 0, TRUE, '/tool', '#', '0', 'tool:'),
(4, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:dashboard-filled', TRUE, '系统监控',4, 0, TRUE, '/monitor', '#', '0', 'monitor:'),
(5, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:file-text-filled', TRUE, '动态报表',5, 0, TRUE, '/report', '#', '0', 'log:'),
(6, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:file-text-filled', TRUE, '日志查看',6, 0, TRUE, '/log', '#', '0', 'log:'),
(7, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:ellipsis', TRUE, '家庭记账', 7, 0, TRUE, '/acct', '#', '0', 'acct:'),
-- 二级菜单
(11, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '工作台', 2, 1, TRUE, 'workplace', 'views/Dashboard/Workplace', '1', 'dashboard:workplace:view'),
(15, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '用户管理', 1, 2, TRUE, 'user', 'views/system/User/Index', '1', 'system:user:view'),
(16, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '角色管理', 2, 2, TRUE, 'role', 'views/system/Role/Index', '1', 'system:role:view'),
(17, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '菜单管理', 3, 2, TRUE, 'menu', 'views/system/Menu/Index', '1', 'system:menu:view'),
(20, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '系统参数配置', 6, 2, TRUE, 'params', 'views/system/Param/Index', '1', 'system:params:view'),
(21, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '基础要素维护', 7, 2, TRUE, 'ele-union', 'views/system/EleUnion/Index', '1', 'system:ele-union:view'),
(22, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '定时任务管理', 8, 2, TRUE, 'task', 'views/framework/job/Index', '1', 'system:task:view'),
(23, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '会计科目维护', 1, 7, TRUE, 'acct-cls', 'views/acct/cls/Index', '1', 'acct:cls:view'),
(24, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '记个账', 2, 7, TRUE, 'acct', 'views/acct/acct/Index', '1', 'acct:acct:view'),
(50, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '登录日志', 1, 6, TRUE, 'login', 'views/system/LoginLog/Index', '1', 'log:login:view'),
(51, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '接口请求日志', 2, 6, TRUE, 'request', 'views/system/RequestLog/Index', '1', 'log:request:view'),
(52, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '定时任务日志', 3, 6, TRUE, 'task', 'views/framework/job/TaskLog/Index', '1', 'log:task:view'),
(91, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '服务监控',2, 4, TRUE, 'server', 'views/framework/monitor/ServerInfoIndex', '1', 'monitor:server:view'),
(92, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '缓存监控',3, 4, TRUE, 'cache', 'views/framework/monitor/CacheListIndex', '1', 'monitor:cache:view');


-- 用户管理按钮
INSERT INTO sys_menu (
  id, created_by, created_date, last_modified_by, last_modified_date, config, enabled, icon_cls, keep_alive, name, order_num, parent_id, require_auth, url, component, menu_type, permission_code
) VALUES
(201, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '新增', 1, 15, TRUE, 'system/user/add', '#', '2', 'system:user:add'),
(202, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '编辑', 2, 15, TRUE, 'system/user/edit', '#', '2', 'system:user:edit'),
(203, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '详情', 3, 15, TRUE, 'system/user/detail', '#', '2', 'system:user:detail'),
(204, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '删除', 4, 15, TRUE, 'system/user/delete', '#', '2', 'system:user:delete');

-- 角色管理按钮
INSERT INTO sys_menu (
  id, created_by, created_date, last_modified_by, last_modified_date, config, enabled, icon_cls, keep_alive, name, order_num, parent_id, require_auth, url, component, menu_type, permission_code
) VALUES
(251, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '新增', 1, 16, TRUE, 'system/role/add', '#', '2', 'system:role:add'),
(252, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '编辑', 2, 16, TRUE, 'system/role/edit', '#', '2', 'system:role:edit'),
(253, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '详情', 3, 16, TRUE, 'system/role/detail', '#', '2', 'system:role:detail'),
(254, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '删除', 4, 16, TRUE, 'system/role/delete', '#', '2', 'system:role:delete');
