package com.xieke.test.tyqxcms.service.impl;

import com.xieke.test.tyqxcms.dto.UserInfo;
import com.xieke.test.tyqxcms.entity.User;
import com.xieke.test.tyqxcms.mapper.UserMapper;
import com.xieke.test.tyqxcms.service.IMailService;
import com.xieke.test.tyqxcms.service.IUserService;
import com.xieke.test.tyqxcms.util.RedisUtils;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-16
 */
@Service(version = "1.0.0", timeout = 60000)
//开启redis缓存
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	
	@Resource
	private RedisUtils redisUtils;
	
	
    @Override
    public UserInfo findUserInfo(String userName) {
        return this.baseMapper.findUserInfo(userName);
    }
    
    /**
     * 注入邮件接口
     */
    @Autowired
    private IMailService mailService;

    /**
     * 用户注册，同时发送一封激活邮件
     * @param user
     */
    
    @Override
    public void register(User user) {
    	redisUtils.getAndSet("code", user.getCode());
    	this.baseMapper.insert(user);
        //获取激活码
        String code = user.getCode();
        System.out.println("code:"+code);
        //主题
        String subject = "来自xxx网站的激活邮件";
        //user/checkCode?code=code(激活码)是我们点击邮件链接之后根据激活码查询用户，如果存在说明一致，将用户状态修改为“1”激活
        //上面的激活码发送到用户注册邮箱
        String context = "<a href=\"http://127.0.0.1:5555/user/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
        //发送激活邮件
        mailService.sendHtmlMail (user.getMailName(),subject,context);
    }

    /**
     * 根据激活码code进行查询用户，之后再进行修改状态
     * @param code
     * @return
     */
    @Override
    public User checkCode(String code) {
        return this.baseMapper.checkCode(code);
    }

    /**
     * 激活账户，修改用户状态
     * @param user
     */
    @Override
    public void updateUserStatus(User user) {
    	this.baseMapper.updateUserStatus(user);
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public User loginUser(User user) {
        User user1 = this.baseMapper.loginUser(user);
        if (user1 !=null){
            return user1;
        }
        return null;
    }
    
    /*
     * 根据用户名查询是否已经存在
     * (non-Javadoc)
     * @see com.xieke.test.tyqxcms.service.IUserService#selectByUsername(java.lang.String)
     */
	@Override
	public boolean selectByUsername(String userName) {
		User user=this.baseMapper.selectByUsername(userName);
		if(user==null){
		return true;
		}
		return false;
	}

	@Override
	public User selectByMail(String mailName) {
		User user=this.baseMapper.selectByMail(mailName);
	    if(user==null){
	    	return user;
	    }
	    return user;
	}
}