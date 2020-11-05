package com.lrm.blog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 页面构造访问错误跳转它会吧数据跳转到这里
 *
 * 带有@ResponseStatus注解的异常类会被ResponseStatusExceptionResolver 解析。
 * 可以实现自定义的一些异常,同时在页面上进行显示
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends  RuntimeException{//运行时异常，

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

