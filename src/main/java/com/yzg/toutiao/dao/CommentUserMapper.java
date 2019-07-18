package com.yzg.toutiao.dao;

import com.yzg.toutiao.model.CommentUser;
import com.yzg.toutiao.model.example.CommentUserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentUserMapper {
    long countByExample(CommentUserExample example);

    int deleteByExample(CommentUserExample example);

    int insert(CommentUser record);

    int insertSelective(CommentUser record);

    List<CommentUser> selectByExample(CommentUserExample example);

    int updateByExampleSelective(@Param("record") CommentUser record, @Param("example") CommentUserExample example);

    int updateByExample(@Param("record") CommentUser record, @Param("example") CommentUserExample example);
}