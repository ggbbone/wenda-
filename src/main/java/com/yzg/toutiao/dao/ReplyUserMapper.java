package com.yzg.toutiao.dao;

import com.yzg.toutiao.model.ReplyUser;
import com.yzg.toutiao.model.example.ReplyUserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyUserMapper {
    long countByExample(ReplyUserExample example);

    int deleteByExample(ReplyUserExample example);

    int insert(ReplyUser record);

    int insertSelective(ReplyUser record);

    List<ReplyUser> selectByExample(ReplyUserExample example);

    int updateByExampleSelective(@Param("record") ReplyUser record, @Param("example") ReplyUserExample example);

    int updateByExample(@Param("record") ReplyUser record, @Param("example") ReplyUserExample example);
}