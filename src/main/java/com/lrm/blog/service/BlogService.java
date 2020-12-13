package com.lrm.blog.service;

import com.lrm.blog.po.Blog;
import com.lrm.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 博客 界面
 */
public interface BlogService {
/*
查询
 */
    Blog getBlog(Long id);

    ;
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


    //首页查询方法


    Page<Blog> listBlog(String query, Pageable pageable);

    Page<Blog> listBlog(Pageable pageable);

    //首页展示
    List<Blog> listRecommendBlogTop(Integer size);
}
