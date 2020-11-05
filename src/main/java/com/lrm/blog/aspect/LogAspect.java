package com.lrm.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * spring aop日志处理切面操作
 * 切面都会经过这里，进行请求
 */
@Aspect
@Component
public class LogAspect {
    private  final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(* com.lrm.blog.web.*.*(..))")
    public  void log(){
    }

    /**
     * 请求方法之前
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        //获取url的请求参数
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
       String classMethod=  joinPoint.getSignature().getDeclaringTypeName()+
                "."+joinPoint.getSignature().getName();
      //拿到请求参数
        Object[] args = joinPoint.getArgs();
        /* 将拿到参数输入 */

        RequestLog requestLog = new RequestLog(url, ip, classMethod ,args);
        //返回内容
        logger.info("Request:{}",requestLog);
    }

/**
 * 请求方法之后
 */
@After("log()")
public void doAfter(){
  //  logger.info("----------后测试---------");
}
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturning(Object result){
        logger.info("Result : {}",result);
    }

    private class RequestLog{
    private String url;//请求url
    private String ip;//主机ip
    private String classMethod;//调用的classMethod
    private Object[]args;//参数agrs

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
