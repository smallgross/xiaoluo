package com.lrm.blog.service;

import com.lrm.blog.dao.CommentRepository;
import com.lrm.blog.po.Comment;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;



/**
 * 评论区实现
 * @author D20200328
 *
 */
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
    /*
     * 
     *  /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
     

    private List<Comment> eachComment(List<Comment> comments) {
		List<Comment> cArrayList = new ArrayList<>();
		for (Comment comment : cArrayList) {
			Comment c = new Comment();
			BeanUtils.copyProperties(comment, c);
			cArrayList.add(c);
		}//合并评论的各层子代到第一级子代集合中
		combineChildren(cArrayList);
		
		return cArrayList;
	}
    
    
    @Transactional
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
            List<Comment> replyst1 = comment.getReplyComments();
            for (Comment rep1yl : replyst1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(rep1yl);
            }
            //修改顶级节点的reply集合迭代处理的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys= new ArrayList<>();
        }
    }
    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
	private void recursively(Comment comment) {
		tempReplys.add(comment);//顶节点添加到临时存放集合
		
		if (comment.getReplyComments().size()>0) {
			List<Comment>replys=comment.getReplyComments();
			for (Comment reply : replys) {
				tempReplys.add(reply);
				if (reply.getReplyComments().size()>0) {
					recursively(reply);
					
				}
				
			}
			
		}
		
	}

}
