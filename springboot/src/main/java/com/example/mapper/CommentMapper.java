package com.example.mapper;

import com.example.entity.Comment;
import java.util.List;

/**
 * 评论数据访问
 */
public interface CommentMapper {

    List<Comment> selectAll(Comment comment);

    Comment selectById(Integer id);

    void insert(Comment comment);

    void updateById(Comment comment);

    void deleteById(Integer id);

}
