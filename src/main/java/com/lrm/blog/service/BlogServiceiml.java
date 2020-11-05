package com.lrm.blog.service;

import com.lrm.blog.NotFoundException;
import com.lrm.blog.dao.BlogRepository;
import com.lrm.blog.po.Blog;
import com.lrm.blog.po.Type;
import com.lrm.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客
 */
@Service
public class BlogServiceiml implements BlogService{
    @Autowired
    private BlogRepository blogRepository;

    /**
     * 查询功能
     * @param id
     * @return
     */

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findOne(id);
    }

    /**
     * 动态分页功能
     * @param pageable
     * @param blog
     * @return
     */

    @Override
    public Page<Blog> listBloag(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq,
                                         CriteriaBuilder cb) {
             List<Predicate>predicates =  new ArrayList<>();
                if (!"".equals(blog.getTitle())&& blog.getTitle()!=null){
                    predicates.add( cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));

                }
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    /**
     * 保存
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //保存
        if (blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }
       else {
           blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    /**
     * 修改
     * @param id
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findOne(id);
        if(b==null){
            throw new NotFoundException("该博客不存在");

        }else {
            // BeanUtils提供对Java反射和自省API的包装。其主要目的是利用反射机制对JavaBean的属性进行处理。
            BeanUtils.copyProperties(b,blog);
            return blogRepository.save(b);
        }



    }
    @Transactional
    @Override
    public void deleteBlog(Long id) {
    blogRepository.delete(id);
    }
}
