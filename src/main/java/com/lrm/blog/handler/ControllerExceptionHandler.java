package com.lrm.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 错误拦截器
 */
@ControllerAdvice
public class ControllerExceptionHandler {
 private final Logger logger=LoggerFactory.getLogger(this.getClass());
    @ExceptionHandler(Exception.class)
   public ModelAndView exceptinHander(HttpServletRequest request, Exception e) throws Exception {
    logger.error("REUST URL : {} , Exception : {}",request.getRequestURL());

    if (AnnotationUtils.findAnnotation(e.getClass(),ResponseStatus.class)!=null){
        throw  e;

    } else {
        //ModelAndView展示出来
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return  mv;
    }

 }

}
