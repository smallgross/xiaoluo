package com.lrm.blog.service;

import com.lrm.blog.dao.CommentRepository;
import com.lrm.blog.po.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommtByBlogId(Long blogId) {
        Sort sort = new Sort("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentCommentNull(blogId, sort);

        return eachComment(comments);
    }

    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != null) {
            comment.setParentComment(commentRepository.findOne(parentCommentId));


        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }


    /**
     * root根节点，blog不为空对象集合
     */


    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replyst = comment.getReplyComments();
            for (Comment rep1yl : replyst) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
        }
    }

}
