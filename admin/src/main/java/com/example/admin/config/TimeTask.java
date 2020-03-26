package com.example.admin.config;

import cn.hutool.core.date.DateUtil;
import com.example.admin.service.RestFulService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.Set;

/**
 * Created by hui.yunfei@qq.com on 2019/6/4
 */
@Component
@EnableScheduling
@Slf4j
public class TimeTask {
    private static Logger logger = LoggerFactory.getLogger(TimeTask.class);

    @Autowired
    public SimpMessagingTemplate template;
    @Autowired
    RestFulService restFulService;
    @Autowired
    private SimpUserRegistry userRegistry;
    //@Scheduled(cron = "0/20 * * * * ?")
    public void test(){
        System.err.println("*********   定时任务执行   **************");
//        CopyOnWriteArraySet<WebSocketServer> webSocketSet =
//                WebSocketServer.getWebSocketSet();
//        int i = 0 ;
//        webSocketSet.forEach(c->{
//            try {
//                c.sendMessage("  定时发送  " + new Date().toLocaleString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
        String message=restFulService.getMessage();
        template.convertAndSend("/topic/getResponse", "我是服务器主动推送的定时消息"+message+"|||"+new Date().toLocaleString());
        System.err.println("/n 定时任务完成.......");
    }
    @Scheduled(cron = "0/5 * * * * ?")
    public void test1(){
        System.out.println("*********   定时任务执行   **************");


        String message=restFulService.getMessage();

//        template.convertAndSendToUser("wu","/message", "我是服务器主动推送的定时消息"+message+"|||"+ DateUtil.formatDateTime(new Date()));
//        log.info("wu"+DateUtil.formatDateTime(new Date()));

        Set<SimpUser> users = userRegistry.getUsers();
        for (SimpUser user : users) {
            System.out.println(user.getName());
            template.convertAndSendToUser(user.getName(),"/message1", message+"|||"+user.getName()+ new Random().nextInt());
        }
//        int i = new Random().nextInt(2)+1;
//        if(i==1){
//
//        }else {
//            template.convertAndSendToUser("qw","/message", "我是服务器主动推送的定时消息"+message+"|||"+ new Random().nextInt());
//        }

    }

}
