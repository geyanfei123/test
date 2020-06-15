package com.xieke.test.tyqxcms.service;

import com.baomidou.mybatisplus.service.IService;
import com.xieke.test.tyqxcms.dto.UserInfo;
import com.xieke.test.tyqxcms.entity.User;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
public interface IUserService extends IService<User> {

    UserInfo findUserInfo(String userName);
    
    /**
     * 用户注册,
     * @param user
     */
    void register(User user);
    
    
    boolean selectByUsername(String userName);
    
    User selectByMail(String mailName);

    /**
     * 根据激活码code查询用户，之后再进行修改状态
     * @param code
     * @return
     */
    User checkCode(String code);

    /**
     * 激活账户，修改用户状态为“1”
     * @param user
     */
    void updateUserStatus(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    User loginUser(User user);

}
