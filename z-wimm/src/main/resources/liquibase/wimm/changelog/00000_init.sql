--liquibase formatted sql
--changeset zzw:00000

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

-- ----------------------------
-- 菜单初始化
-- ----------------------------
delete from sys_menu where id between 1000 and 1500;

INSERT INTO sys_menu (
  id, created_by, created_date, last_modified_by, last_modified_date, config, enabled, icon_cls, keep_alive, name, order_num, parent_id, require_auth, url, component, menu_type, permission_code
) VALUES
-- 一级菜单
(1000, 'system', NOW(), 'system', NOW(), NULL, TRUE, 'ant-design:ellipsis', TRUE, '家庭记账', 9, 0, TRUE, '/acct', '#', '0', 'acct:'),
-- 二级菜单
(1002, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '会计科目维护', 1, 1000, TRUE, 'acct-cls', 'views/acct/AccountCls/Index', '1', 'acct:AccountCls:view'),
(1003, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '记个账', 2, 1000, TRUE, 'acct-add', 'views/acct/Account/components/AddOrUpdate', '1', 'acct:Account:view'),
(1004, 'system', NOW(), 'system', NOW(), NULL, TRUE, NULL, TRUE, '记账查看', 2, 1000, TRUE, 'acct', 'views/acct/Account/Index', '1', 'acct:Account:view')

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
);

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