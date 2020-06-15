package com.xieke.test.tyqxcms.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xieke.test.tyqxcms.annotation.SysLog;
import com.xieke.test.tyqxcms.dto.ResultInfo;
import com.xieke.test.tyqxcms.dto.UserInfo;
import com.xieke.test.tyqxcms.entity.User;
import com.xieke.test.tyqxcms.service.IUserService;
import com.xieke.test.tyqxcms.util.PasswordEncoder;
import com.xieke.test.tyqxcms.util.StringUtils;
import com.xieke.test.tyqxcms.util.UuidUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2018-07-10
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Reference(version = "1.0.0")
    private IUserService iUserService;

    @RequestMapping("/*")
    public void toHtml(){

    }
    
    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/registerUser")
    public @ResponseBody 
    ResultInfo<Boolean> register(User user){
    	//查询用户名，邮箱是否已经存在注册。
    	if(isUser(user.getUserName()) ==false){
    		//已存在
    		return new ResultInfo<>(false);
    	}
    	//查询邮箱是否存在和是否激活
    	User userOne=this.selectByMail(user.getMailName());
    	if(userOne==null){
    		//没有这个邮箱
    		 user.setState(0);
    	        String code = UuidUtils.getUUID()+ UuidUtils.getUUID();
    	        user.setCode(code);
    	        iUserService.register(user);
    	        return new ResultInfo<>("lomg","注册成功前去激活");
    	}else{
	    	if(userOne.getMailName().equals(user.getMailName()) && userOne.getState()==0){
	    		EntityWrapper<User> wrapper = new EntityWrapper<>(user);
	    		//未激活
	    		user.setState(1);
	    		iUserService.update(user, wrapper);
	    		 return new ResultInfo<>("and","已注册请去激活");
	    	}else{
	    		return new ResultInfo<>("msg","该邮箱已经激活激活");
	    	}
    	}
    	
    }
    
    
    
    
    /*
     * 查询用户是否已经存在
     */
    public Boolean isUser(String userName){
    	return iUserService.selectByUsername(userName); 	
    }
    
    /*
     * 根据邮箱信息查询
     */
    public User selectByMail(String mailName){
    	
    	return iUserService.selectByMail(mailName)	;
    }

    /**
     *校验邮箱中的code激活账户
     * 首先根据激活码code查询用户，之后再把状态修改为"1"
     */
    @RequestMapping(value = "/checkCode")
    public String checkCode(String code){
        User user = iUserService.checkCode(code);
        System.out.println(user);
        //如果用户不等于null，把用户状态修改status=1
       if (user !=null){
           user.setState(1);
           //把code验证码清空，已经不需要了
           user.setCode("");
           System.out.println(user);
           iUserService.updateUserStatus(user);
       }
        return "index";
    }

    /**
     * 跳转到登录页面
     * @return login
     */
    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }

   /* *//**
     * 登录
     *//*
    @RequestMapping(value = "/login")
    public String login(User user, Model model){
        User u = iUserService.loginUser(user);
        if (u !=null){
            return "welcome";
        }
        return "login";
    }
*/
    @RequestMapping("/unlock")
    @ResponseBody
    public ResultInfo<Boolean> unlock(@RequestParam("password") String password){
        UserInfo userInfo = this.getUserInfo();
        SimpleHash simpleHash = new SimpleHash("md5", password, userInfo.getCredentialsSalt(), 2);
        if (simpleHash.toString().equals(userInfo.getPassWord())){
            return new ResultInfo<>(true);
        }
        return new ResultInfo<>(false);
    }

    @RequestMapping("/listData")
    @RequiresPermissions("user:view")
    public @ResponseBody
    ResultInfo<List<User>> listData(User user, Integer page, Integer limit){
        EntityWrapper<User> wrapper = new EntityWrapper<>(user);
        if(user!=null&&user.getUserName()!=null){
            wrapper.like("user_name", user.getUserName());
            user.setUserName(null);
        }
        if(user!=null&&user.getName()!=null){
            wrapper.like("name",user.getName());
            user.setName(null);
        }
        Page<User> pageObj = iUserService.selectPage(new Page<>(page,limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    @SysLog("添加用户操作")
    @RequestMapping("/add")
    @RequiresPermissions("user:add")
    public @ResponseBody
    ResultInfo<Boolean> add(User user){
        Map<String, String> map = PasswordEncoder.enCodePassWord(user.getUserName(), user.getPassWord());
        user.setSalt(map.get(PasswordEncoder.SALT));
        user.setPassWord(map.get(PasswordEncoder.PASSWORD));
        boolean b = iUserService.insert(user);
        return new ResultInfo<>(b);
    }

    @SysLog("删除用户操作")
    @RequestMapping("/delBatch")
    @RequiresPermissions("user:del")
    public @ResponseBody
    ResultInfo<Boolean> delBatch(Integer[] idArr){
        boolean b = iUserService.deleteBatchIds(Arrays.asList(idArr));
        return new ResultInfo<>(b);
    }

    @SysLog("修改用户操作")
    @RequestMapping("/edit")
    @RequiresPermissions("user:edit")
    public @ResponseBody
    ResultInfo<Boolean> edit(User user){
        User us = iUserService.selectById(user.getId());
        us.setName(user.getName());
        us.setRoleId(user.getRoleId());
        us.setState(user.getState());
        boolean b = iUserService.updateById(us);
        return new ResultInfo<>(b);
    }

    @SysLog("本人修改用户操作")
    @RequestMapping("/userEdit")
    public @ResponseBody
    ResultInfo<Boolean> userEdit(User user){
        UserInfo userInfo = this.getUserInfo();
        User us = iUserService.selectById(userInfo.getId());
        if(!StringUtils.isEmpty(user.getName())){
            us.setName(user.getName());
        }
        if(!StringUtils.isEmpty(user.getPassWord())){
            Map<String, String> map = PasswordEncoder.enCodePassWord(us.getUserName(),user.getPassWord());
            us.setSalt(map.get(PasswordEncoder.SALT));
            us.setPassWord(map.get(PasswordEncoder.PASSWORD));
        }
        boolean b = iUserService.updateById(us);
        return new ResultInfo<>(b);
    }

    @RequestMapping("/centerDate")
    public @ResponseBody
    ResultInfo<UserInfo> centerDate(){
        UserInfo userInfo = this.getUserInfo();
        BeanUtils.copyProperties(iUserService.selectById(userInfo.getId()),userInfo);
        return new ResultInfo<>(userInfo);
    }

    @RequestMapping("/count")
    public @ResponseBody
    ResultInfo<Integer> count(){
        return new ResultInfo<>(iUserService.selectCount(new EntityWrapper<>()));
    }

}