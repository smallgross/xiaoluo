package com.lrm.blog.web.admin;

import com.lrm.blog.po.Blog;
import com.lrm.blog.po.Type;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    /**
     * 分页功能
     */
    @GetMapping("/blogs")
    public String blgos(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBloag(pageable, blog));
        return LIST;
    }

    /**
     * 搜索功能
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3,sort =
            {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                         BlogQuery blog, Model model){
        model.addAttribute("page",blogService.listBloag(pageable, blog));
        return "admin/blogs ::blogList";
    }

    /**
     * 博客列表展示
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    /**
     * 添加功能
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
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
    @GetMapping("bolgs/{id}/input")
    public String bolginput( @PathVariable Long id, Model model){


        return "redirect:/admin/blogs";
    }
    /**
     * 博客删除功能
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/bolgs/{id}/delete")
    public String blogdelete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";

    }

}
