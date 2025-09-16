import { dbService } from './index';
import type { UserLoginType, UserType } from '@/api/login/types';

/**
 * 登录服务
 */
export class LoginService {
  /**
   * 用户登录
   * @param data 登录信息
   */
  async login(data: UserLoginType): Promise<UserType> {
    try {
      const { username, password } = data;
      
      // 查询用户
      const users = await dbService.selectSql(
        'SELECT * FROM users WHERE username = ? AND password = ? LIMIT 1',
        [username, password]
      );
      
      if (users && users.length > 0) {
        const user = users[0];
        // 解析权限字段（存储为JSON字符串）
        let permissions = user.permissions;
        try {
          permissions = JSON.parse(permissions);
        } catch (e) {
          // 如果解析失败，保持原样
        }
        
        // 返回用户信息
        return {
          username: user.username,
          password: user.password,
          role: user.role,
          roleId: user.roleId,
          permissions: permissions,
          token: user.token
        };
      } else {
        throw new Error('用户名或密码错误');
      }
    } catch (error) {
      console.error('登录失败', error);
      throw error;
    }
  }
}

// 导出单例
export const loginService = new LoginService(); 