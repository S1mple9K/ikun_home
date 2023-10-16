package com.user.controller;

import com.ikun.pojo.Tag;
import com.ikun.request.Result;
import com.user.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 话题控制层
 *
 * @author 9K
 * @create: 2023-07-27 15:43
 */

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    /**
     * 注入TagRepository
     */
    @Autowired
    private TagRepository tagRepository;

    /**
     * 获取标签集合
     * @return
     */
    @GetMapping("/getTagList")
    public Result getTagList() {
        List<Tag> list = tagRepository.findAll();
        list.removeIf(item->"全部".equals(item.getTagName()));
        return Result.ok(list);
    }

    /**
     * 获取大分类
     */
    @GetMapping("/getTag/{parentId}")
    public Result getTag(@PathVariable Long parentId){
        //查询parentId为0的
        List<Tag> tagAList = tagRepository.getTagsByParentId(parentId);
        return Result.ok(tagAList);
    }
}
