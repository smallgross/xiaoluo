package com.lrm.blog.service;

import com.lrm.blog.NotFoundException;
import com.lrm.blog.dao.BlogRepository;
import com.lrm.blog.po.Blog;
import com.lrm.blog.po.Type;
import com.lrm.blog.util.MarkdownUtils;
import com.lrm.blog.util.MyBeanUtils;
import com.lrm.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

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

    //编辑器转换为html
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");

        } else {
            Blog b = new Blog();
            BeanUtils.copyProperties(blog, b);
            String content = b.getContent();
            b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
            blogRepository.updateViews(id);
            return b;
        }
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
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
                List<Predicate> predicates = new ArrayList<>();
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
     * Spring Data -Specification用法和常用查询方法（in，join，equal等）
     * Repository层中为了支持这样的查询，sysUserRepository需要继承JpaRepository（基本查询），JpaSpecificationExecutor（分页），这个接口是不需要再去实现的，到了Repository层就行，再对此进行扩充（比Mybatis简单多了）。
     *
     * @param tagId
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
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
        } else {
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
        System.out.println(b);
        if(b==null){
            throw new NotFoundException("该博客不存在");

        }else {
            // BeanUtils提供对Java反射和自省API的包装。其主要目的是利用反射机制对JavaBean的属性进行处理。
            BeanUtils.copyProperties(b, blog, MyBeanUtils.getNullPropertyNames(blog));
            b.setUpdateTime(new Date());
            return blogRepository.save(b);
        }



    }
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.delete(id);
    }
    @Transactional
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }


    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRepository.findTop(pageable);
    }
}
