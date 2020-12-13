package com.lrm.blog.service;

import com.lrm.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 分类新增
 */
public interface TypeService {
    /**
     * 保存
     * @param type
     * @return
     */
    Type saveType(Type type);

    /**
     * 查询
     */
    Type getType(Long id);
/**
 * 通过名称来查询密码
 */
    Type getTypeByName(String name);
    /**
     * 分页
     */
    Page<Type>lisType(Pageable pageable);


    List<Type> listType();

    /**
     * 修改
     */
    Type updateype(Long id,Type type);

    /**
     * 删除
     */
    void deleteType(Long id);

    /**
     * 前端
     */
    List<Type> listTypeTop(Integer size);

}
