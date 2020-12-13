package com.lrm.blog.service;


import com.lrm.blog.NotFoundException;
import com.lrm.blog.dao.TypeRepository;
import com.lrm.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 实现分类新增
 */
@Service
public class TypeServiceiml implements  TypeService{

@Autowired
private TypeRepository typeRepository;

@Transactional
    @Override
    public Type saveType(Type type) {

        return typeRepository.save(type);
    }
    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findOne(id);
    }
    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> lisType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }


    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    /**
     * 修改
     *
     * @param id
     * @param type
     * @return
     */
    @Transactional
    @Override
    public Type updateype(Long id, Type type) {
        Type t = typeRepository.findOne(id);
        if (t== null) {
            throw new NotFoundException("不存在该分类");

        }
        BeanUtils.copyProperties(type,t);

        return typeRepository.save(t);
    }
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.delete(id);

    }

    @Transactional
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);

        return typeRepository.findTop(pageable);
    }
}
