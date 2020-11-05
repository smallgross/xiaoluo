package com.lrm.blog.web.admin;

import com.lrm.blog.po.Blog;
import com.lrm.blog.po.User;
import com.lrm.blog.service.BlogService;
import com.lrm.blog.service.TagService;
import com.lrm.blog.service.TypeService;
import com.lrm.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 后台博客界面
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT="admin/blogs-input";
    private static final String LIST="admin/blogs";
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blgos(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBloag(pageable, blog));
        return LIST;
    }
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3,sort =
            {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                         BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBloag(pageable, blog));
        return "admin/blogs ::blogList";
    }
    @GetMapping("/blogs/input")
    public String input(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));

        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b = blogService.saveBlog(blog);

        if (b==null){
            attributes.addFlashAttribute("message","操作失败");

        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;

    }

}
