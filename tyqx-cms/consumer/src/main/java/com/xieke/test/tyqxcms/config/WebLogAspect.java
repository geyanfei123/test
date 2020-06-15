package com.xieke.test.tyqxcms.config;

import java.lang.reflect.Method;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xieke.test.tyqxcms.annotation.SysLog;
import com.xieke.test.tyqxcms.dto.UserInfo;
import com.xieke.test.tyqxcms.entity.Log;
import com.xieke.test.tyqxcms.service.ILogService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebLogAspect {

    private Logger logger = Logger.getLogger(getClass());

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Reference(version = "1.0.0")
    private ILogService ilogService;

	@Pointcut("execution(public * com.xieke.test.tyqxcms.web..*.*(..))")
	public void webLog() {
	}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
		startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 请求参数
        Object[] args = joinPoint.getArgs();
        String requestParam = "";
        if (args != null && args.length > 0){
            try {
                requestParam = JSONObject.toJSONString(args[0]);
            }catch (Exception e){

            }
        }

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + requestParam);

        // 添加系统操作日志
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        SysLog sysLog = targetMethod.getAnnotation(SysLog.class);
        if (sysLog!=null){
            UserInfo userInfo = (UserInfo)SecurityUtils.getSubject().getPrincipal();
            Log log = new Log();
            log.setUserId(userInfo.getId());
            log.setUserName(userInfo.getUserName());
            log.setOperMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            log.setRequestParam(requestParam);
            log.setOperDesc(sysLog.value());
            ilogService.insert(log);
        }

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }

}