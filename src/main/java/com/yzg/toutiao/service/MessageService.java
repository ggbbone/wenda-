package com.yzg.toutiao.service;

import com.yzg.toutiao.dao.MessageMapper;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author yzg
 * @create 2019/7/20
 */
@Service
@Transactional
public class MessageService {

    @Autowired
    MessageMapper messageMapper;

    /**
     * 插入一条消息
     * @param message
     */
    public void insertMessage(Message message){
        message.setCreatedDate(new Date());
        message.setHasRead((byte) 0);
        messageMapper.insertSelective(message);
    }


}
