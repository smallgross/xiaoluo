package com.lrm.blog.service;

import com.lrm.blog.po.Blog;
import com.lrm.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 博客 界面
 */
public interface BlogService {
/*
查询
 */
    Blog getBlog(Long id);

    /**
     * 分页功能
     */
    Page<Blog>listBloag(Pageable pageable, BlogQuery blog);

    /**
     * 增加功能
     */
    Blog saveBlog(Blog blog);
    /**
     * 修改接口
     */
    Blog updateBlog(Long id,Blog blog);
    /**
     * 删除接口
     */
    void deleteBlog(Long id);
}
