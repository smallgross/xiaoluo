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
import org.springframework.web.bind.annotation.PathVariable;
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

    private static final String INPUT = "admin/blogs-input";//文档编辑页面的跳转
    private static final String LIST = "admin/blogs";//博客跳转
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    private Blog blog;
    private RedirectAttributes attributes;
    private HttpSession httpSession;

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
     *公用（保存和修改）
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        //拿到当前的登录用户端的seesion
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));

        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;

        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);

        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }
        if (b==null){
            attributes.addFlashAttribute("message","操作失败");

        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;

    }

    /**
     * 修改博客内容调用
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/bolgs/{id}/input")
    public String bolginput( @PathVariable Long id, Model model){
        seTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    /**
     * 添加时初始化，查询分类和标签
     * @param model
     */
    private void seTypeAndTag(Model model) {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        System.out.println(model);
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
        return REDIRECT_LIST;

    }
}
