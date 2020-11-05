package com.lrm.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

/**
 * 测试
 */
@Controller
public class IndexController {


    @GetMapping("/")
  //  @RequestMapping
   public String index(){
        return "index";
    }
    @GetMapping("/bolog")
    //  @RequestMapping
    public String bolog(){
        return "bolog";
    }
}
