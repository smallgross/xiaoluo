package com.lrm.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommentController {


    @GetMapping
    public String commets() {
        return "blog::";
    }
}
