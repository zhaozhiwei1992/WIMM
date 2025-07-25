# 项目描述

一个前后端分离的后台管理系统， 基于springboot3.3.3+vue3.3.4+mysql8， 代码结构清晰，注释友好，使用最新的前后端开发技术，适合想要尝鲜的道友。

项目参考了jhipster的优秀代码设计，ruoyi项目的目录接口， 分模块开发， 感谢以上开源项目。

目前处于开发阶段，有问题提issue，后续会继续完善。如果动手能力强的朋友，可以直接参与开发，一起完善这个项目。

# 体验地址

http://43.143.194.245:8091/index.html 

# 项目特点

代码注释友好， 实现简单， 便于阅读

前后端分离，使用 token 认证

支持RBAC权限模型， 支持菜单及按钮权限控制， 动态显示及后端严格权限控制

支持角色+菜单的数据权限控制， 按照规则配置好后，程序自动控制

前端采用 Vue3.x + element-plus-admin， 数据绑定代码简单，提高开发效率

使用 liquibase 进行数据库版本控制， 空库只需创建数据库然后启动服务即可

使用 quartz 定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能

使用 swagger 查看或测试后端接口

使用 jpa 做数据库操作， 理论上支持大部分关系数据库

同步支持移动端小程序

# 功能模块

## 基础数据

- [x] 用户管理

- [x] 角色管理

## 菜单管理

- [x] 菜单管理

## 系统管理

- [x] 功能权限

- [x] 数据权限

- [x] 缓存管理

- [x] 定时任务

- [x] 系统参数

## 审计查询

## 系统监控

- [x] 日志管理

- [x] 资源监控

- [x] 服务状态

- [x] 缓存状态

## 首页显示

## 国际化

- [x] 国际化支持

# 安装要求
1. java 21+
2. springboot 3.3.3.RELEASE
3. mysql 8+
5. npm 8+
6. node 20+

# 安装步骤

1. git clone 当前项目到你喜欢的目录
2. 用你喜欢的 ide 引入该项目，并加载好依赖
3. 创建好数据库 database， 默认 WIMM
5. 启动项目: 后端运行com.z.server.BootStrapServerApplication， 前端进入z-ui-admin-vue3目录， 执行pnpm run dev(先构建)
6. 访问http://localhost:4000， 登录查看我们的成果， 用户/密码:admin/admin

# 部署

## 前后端分离部署

## 前后端集中部署
```sh
mvn clean package -Psingle
```

# 版本控制

该项目使用 git 进行版本管理。您可以在 tags 参看当前可用版本。

# 参考

# 版权说明
