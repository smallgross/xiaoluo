package com.lrm.blog.service;

import com.lrm.blog.NotFoundException;
import com.lrm.blog.dao.TagRepository;
import com.lrm.blog.po.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签
 */
@Service
public class TagServiceiml implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Transactional
    @Override
    public Tag saveTag(Tag type) {
        return tagRepository.save(type);
    }
    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }
    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }
    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }
  @Transactional
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Transactional
    @Override
    public List<Tag> listTag(String ids) {//123
        return tagRepository.findAll(convertToList(ids));
    }
    /**
     * 把字符串转换成类
     * @param ids
     * @return
     */
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag type) {
        Tag t=tagRepository.findOne(id);
        if (t==null){
            throw  new NotFoundException("不存在该标签");
        }
        return tagRepository.save(t);
    }
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.delete(id);

    }
}
