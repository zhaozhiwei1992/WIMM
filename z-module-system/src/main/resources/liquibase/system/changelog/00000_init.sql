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
(id, created_by, created_date, last_modified_by, last_modified_date, activated, avatar, login, name, password_hash)
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
(4, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:dashboard-filled', TRUE, '系统监控',4, 0, TRUE, '/monitor', '#', '0', 'monitor:'),
(5, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:file-text-filled', TRUE, '动态报表',5, 0, TRUE, '/report', '#', '0', 'log:'),
(6, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:file-text-filled', TRUE, '日志查看',6, 0, TRUE, '/log', '#', '0', 'log:'),
(7, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:ellipsis', TRUE, '家庭记账', 7, 0, TRUE, '/acct', '#', '0', 'acct:'),
-- 二级菜单
(11, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '工作台', 2, 1, TRUE, 'workplace', 'views/Dashboard/Workplace', '1', 'dashboard:workplace:view'),
(15, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '用户管理', 1, 2, TRUE, 'user', 'views/system/User/Index', '1', 'system:user:view'),
(16, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '角色管理', 2, 2, TRUE, 'role', 'views/system/Role/Index', '1', 'system:role:view'),
(17, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '菜单管理', 3, 2, TRUE, 'menu', 'views/system/Menu/Index', '1', 'system:menu:view'),
(23, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '会计科目维护', 1, 7, TRUE, 'acct-cls', 'views/acct/AccountCls/Index', '1', 'acct:AccountCls:view'),
(24, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '记个账', 2, 7, TRUE, 'acct', 'views/acct/Account/components/AddOrUpdate', '1', 'acct:Account:view'),
(25, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '记账查看', 2, 7, TRUE, 'acct', 'views/acct/Account/Index', '1', 'acct:Account:view'),
(50, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '登录日志', 1, 6, TRUE, 'login', 'views/system/LoginLog/Index', '1', 'log:login:view'),
(51, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '接口请求日志', 2, 6, TRUE, 'request', 'views/system/RequestLog/Index', '1', 'log:request:view'),
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

CREATE TABLE if not exists `acct_account_cls` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50)  DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50)  DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `code` varchar(255)  DEFAULT NULL,
  `icon` varchar(255)  DEFAULT NULL,
  `is_enabled` int DEFAULT NULL,
  `is_leaf` int DEFAULT NULL,
  `is_standard` int DEFAULT NULL,
  `level_no` int DEFAULT NULL,
  `name` varchar(255)  DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `set_year` varchar(255)  DEFAULT NULL,
  `order_num` int DEFAULT NULL,
  PRIMARY KEY (`id`)
)

delete from acct_account_cls ;

INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(1, 'system', '2024-10-29 06:28:56.580549', 'system', '2024-10-29 06:28:56.580549', '001', '', NULL, 0, 1, 1, '资产', 0, '', '2024', 1);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(2, 'system', '2024-10-29 06:29:22.313988', 'system', '2024-10-29 06:29:22.313988', '002', '', NULL, 0, 1, 1, '负债', 0, '', '2024', 2);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(3, 'system', '2024-10-29 06:29:52.894484', 'system', '2024-10-29 06:29:52.894484', '003', '', NULL, 0, 1, 1, '收入', 0, '', '2024', 3);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(4, 'system', '2024-10-29 06:30:09.178208', 'system', '2024-10-29 06:30:09.178208', '004', '', NULL, 0, 1, 1, '支出', 0, '', '2024', 4);
-- 资产类二级科目
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(6, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '001001', '', NULL, 1, 1, 2, '现金', 1, '', '2024', 1);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(7, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '001002', '', 1, 1, 1, 2, '银行', 1, '', '2024', 2);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(8, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '001003', '', 1, 1, 1, 2, '投资', 1, '', '2024', 3);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(9, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '001004', '', 1, 1, 1, 2, '固定资产', 1, '', '2024', 4);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(10, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '001005', '', 1, 1, 1, 2, '其他资产', 1, '', '2024', 5);

-- 负债类二级科目
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(11, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '002001', '', 1, 1, 1, 2, '贷款', 2, '', '2024', 1);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(12, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '002002', '', 1, 1, 1, 2, '信用卡', 2, '', '2024', 2);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(13, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '002003', '', 1, 1, 1, 2, '抵押', 2, '', '2024', 3);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(14, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '002004', '', 1, 1, 1, 2, '其他负债', 2, '', '2024', 4);

-- 收入类二级科目
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(15, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '003001', '', 1, 1, 1, 2, '工资', 3, '', '2024', 1);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(16, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '003002', '', 1, 1, 1, 2, '投资', 3, '', '2024', 2);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(17, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '003003', '', 1, 1, 1, 2, '其他收入', 3, '', '2024', 3);

-- 支出类二级科目
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(18, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004001', '', 1, 1, 1, 2, '衣服', 4, '', '2024', 1);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(19, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004002', '', 1, 1, 1, 2, '食品', 4, '', '2024', 2);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(20, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004003', '', 1, 1, 1, 2, '住房', 4, '', '2024', 3);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(21, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004004', '', 1, 1, 1, 2, '交通', 4, '', '2024', 4);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(22, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004005', '', 1, 1, 1, 2, '娱乐', 4, '', '2024', 5);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(23, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004006', '', 1, 1, 1, 2, '健身', 4, '', '2024', 6);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(24, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004007', '', 1, 1, 1, 2, '保险', 4, '', '2024', 7);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(25, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004008', '', 1, 1, 1, 2, '学习提升', 4, '', '2024', 8);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(26, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004009', '', 1, 1, 1, 2, '日常用品', 4, '', '2024', 9);
INSERT INTO acct_account_cls (id, created_by, created_date, last_modified_by, last_modified_date, code, icon, is_enabled, is_leaf, is_standard, level_no, name, parent_id, remark, set_year, order_num) VALUES(27, 'system', '2024-10-29 06:34:17.264138', 'system', '2024-10-29 06:34:17.264138', '004010', '', 1, 1, 1, 2, '其他费用', 4, '', '2024', 10);