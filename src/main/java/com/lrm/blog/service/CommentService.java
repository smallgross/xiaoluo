package com.lrm.blog.service;

import com.lrm.blog.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommtByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
