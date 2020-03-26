package com.example.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.example.admin.context.XjxccException;
import com.example.admin.model.User;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by hui.yunfei@qq.com on 2019/6/11
 */
@Slf4j
public class UserInterceptor extends ChannelInterceptorAdapter {
    /**
     * 获取包含在stomp中的用户信息
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            String token = accessor.getNativeHeader("token").get(0);
            String channel = accessor.getNativeHeader("channel").get(0);


        }
        //用户已经断开连接
        if(StompCommand.DISCONNECT.equals(command)){
            String user = "";
            Principal principal = accessor.getUser();
            if(principal != null && StrUtil.isNotBlank(principal.getName())){
                user = principal.getName();
            }else{
                user = accessor.getSessionId();
            }

            log.info(MessageFormat.format("用户{0}的WebSocket连接已经断开", user));
        }

        return message;
    }

}
