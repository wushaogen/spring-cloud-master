package com.example.admin.context;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @Author: wsg
 * @Date: 2019-09-25
 * @Description
 */
@Component
@Slf4j
public class WebSocketAgent {
    @Autowired
    private SimpUserRegistry userRegistry;
    @Autowired
    public SimpMessagingTemplate template;
    public static final String destination = "/message";
    /**
     * 给指定用户发送消息，并处理接收者不在线的情况
     * @param receiver 消息接收者
     * @param payload 消息正文
     */
    private void sendToUser(String receiver, String payload){
        SimpUser simpUser = userRegistry.getUser(receiver);

        //如果接收者存在，则发送消息
        if(simpUser != null && StrUtil.isNotBlank(simpUser.getName())){
            this.template.convertAndSendToUser(receiver, destination, payload);
        }else {
            log.info("消息接收者{0}还未建立WebSocket连接",receiver);
        }
//        //否则将消息存储到redis，等用户上线后主动拉取未读消息
//        else{
//            //存储消息的Redis列表名
//            String listKey = Constants.REDIS_UNREAD_MSG_PREFIX + receiver + ":" + destination;
//            logger.info(MessageFormat.format("消息接收者{0}还未建立WebSocket连接，{1}发送的消息【{2}】将被存储到Redis的【{3}】列表中", receiver, sender, payload, listKey));
//
//            //存储消息到Redis中
//            redisService.addToListRight(listKey, ExpireEnum.UNREAD_MSG, payload);
//        }

    }

}
