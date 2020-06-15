package com.xieke.test.tyqxcms.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xieke.test.tyqxcms.entity.Log;
import com.xieke.test.tyqxcms.mapper.LogMapper;
import com.xieke.test.tyqxcms.service.ILogService;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2018-10-27
 */
@Service(version = "1.0.0", timeout = 60000)
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
