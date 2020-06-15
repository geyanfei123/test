package com.xieke.test.tyqxcms.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xieke.test.tyqxcms.dto.ResultInfo;
import com.xieke.test.tyqxcms.util.FormatUtil;
import com.xieke.test.tyqxcms.util.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;
import com.xieke.test.tyqxcms.entity.Log;
import com.xieke.test.tyqxcms.service.ILogService;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2018-10-27
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

    @Reference(version = "1.0.0")
    private ILogService ilogService;

    @RequestMapping("/*")
    public void toHtml(){

    }

    @RequestMapping("/listData")
    @RequiresPermissions("log:view")
    public @ResponseBody
        ResultInfo<List<Log>> listData(String userName, String operTime, Integer page, Integer limit){
        Log log = new Log();
        log.setUserName(userName);
        EntityWrapper<Log> wrapper = new EntityWrapper<>(log);
        if(!StringUtils.isEmpty(operTime)){
            wrapper.ge("create_time",FormatUtil.parseDate(operTime.split(" - ")[0]+" 00:00:00", null));
            wrapper.le("create_time",FormatUtil.parseDate(operTime.split(" - ")[1]+" 23:59:59", null));
        }
        wrapper.orderBy("create_time",false);
        Page<Log> pageObj = ilogService.selectPage(new Page<>(page,limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

}
