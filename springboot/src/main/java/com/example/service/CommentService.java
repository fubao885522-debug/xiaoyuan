package com.example.service;

import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论业务处理
 */
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    public void add(Comment comment) {
        commentMapper.insert(comment);
    }

    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

}
