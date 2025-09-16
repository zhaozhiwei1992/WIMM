import type { UserLoginType, UserType } from './types';
import { loginService } from '@/config/db/login';

export const loginApi = (data: UserLoginType): Promise<UserType>  => {
    return loginService.login(data);
};

export const loginByWxApi = (code: string): Promise<UserType>  => {
    // 本地模式下，微信登录直接使用默认账号
    return loginService.login({ username: 'admin', password: '123456' });
};

export const loginByPhoneApi = (phonenum: string): Promise<UserType>  => {
    // 本地模式下，手机号登录直接使用默认账号
    return loginService.login({ username: 'admin', password: '123456' });
};

export var loginOutApi = function () {
    // 本地模式下，登出不需要请求服务器
    return Promise.resolve({ code: 200, data: 'success' });
};

export var getUserListApi = function (_a) {
    var params = _a.params;
    // 本地模式下，返回模拟数据
    return Promise.resolve({
        code: 200,
        data: {
            list: [
                {
                    id: 1,
                    username: 'admin',
                    role: 'admin',
                    roleId: '1',
                    status: 1
                }
            ],
            total: 1
        }
    });
};

export var getAdminRoleApi = function (params) {
    // 本地模式下，返回模拟数据
    return Promise.resolve({
        code: 200,
        data: {
            list: [
                {
                    id: '1',
                    name: '管理员',
                    code: 'admin',
                    status: 1
                }
            ],
            total: 1
        }
    });
};

export var getTestRoleApi = function (params) {
    // 本地模式下，返回模拟数据
    return Promise.resolve({
        code: 200,
        data: {
            list: [
                {
                    id: '1',
                    name: '管理员',
                    code: 'admin',
                    status: 1
                }
            ],
            total: 1
        }
    });
};

export var getMenuRouteListApi = function (params) {
    // 本地模式下，返回模拟数据
    return Promise.resolve({
        code: 200,
        data: [
            {
                path: '/dashboard',
                name: 'Dashboard',
                component: 'LAYOUT',
                redirect: '/dashboard/analysis',
                meta: {
                    title: '仪表盘',
                    icon: 'DashboardOutlined',
                    sort: 1
                },
                children: [
                    {
                        path: 'analysis',
                        name: 'Analysis',
                        component: '/dashboard/analysis/index',
                        meta: {
                            title: '分析页',
                            sort: 1
                        }
                    }
                ]
            }
        ]
    });
};
