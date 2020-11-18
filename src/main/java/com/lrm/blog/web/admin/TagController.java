package com.lrm.blog.web.admin;

import com.lrm.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 标签
 */
@Controller
@RequestMapping("/admin")
public class TagController<direction> {

    @Autowired
    TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3,
            sort = {"id"}, direction = Sort.Direction.ASC)
                               Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));

        return "admin/tags";
    }
}
