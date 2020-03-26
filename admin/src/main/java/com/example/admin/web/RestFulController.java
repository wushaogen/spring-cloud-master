package com.example.admin.web;

import cn.hutool.json.JSONUtil;
import com.example.admin.context.Constans;
import com.example.admin.context.ReturnData;
import com.example.admin.context.UserName;
import com.example.admin.model.ReceiveMessage;
import com.example.admin.service.RestFulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hui.yunfei@qq.com on 2019/5/31
 */
@Controller
@RequestMapping("/restful")
public class RestFulController {

    @Autowired
    RestFulService restFulService;

    @Autowired
    public SimpMessagingTemplate template;


    @Autowired
    private SimpUserRegistry userRegistry;





    /**
     * @Description:广播
     *
     * @Author:hui.yunfei@qq.com
     * @Date: 2019/6/4
     */
    @MessageMapping("/subscribe")
    @SendTo
    public void subscribe(ReceiveMessage rm) {
        System.out.println("服务端接收到广播消息："+rm);
        for(int i =1;i<=5;i++) {
            //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
            template.convertAndSend("/topic/getResponse", rm.getMsg());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Description:点对点
     * @Author:hui.yunfei@qq.com
     * @Date: 2019/6/4
     */
    @MessageMapping("/queue")
    @SendToUser
    public void queue(ReceiveMessage rm) {
        System.out.println("服务端接收到点对点消息："+rm);
        for(int i =1;i<=5;i++) {
            /*广播使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为
            "/user/" + 用户Id + "/message",其中"/user"是固定的*/
            template.convertAndSendToUser(rm.getUser(),"/message",rm.getMsg());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Description:主动推消息到客户端(可配做定时器)
     * @Author:hui.yunfei@qq.com
     * @Date: 2019/6/4
     */

    @RequestMapping("/send")
    public void send(){
        template.convertAndSend("/topic/getResponse", "我是服务器主动推送的消息");
    }

    @RequestMapping("/getOnline")
    @ResponseBody
    public ReturnData getOnline(){
        System.out.println("当前在线人数:" + userRegistry.getUserCount());
        int i = 1;
        for (SimpUser user : userRegistry.getUsers()) {
            System.out.println("用户" + i++ + "---" + user);
        }
        return ReturnData.ok(JSONUtil.toJsonStr(userRegistry.getUsers()));
    }

    @PostMapping("/login")
    @ResponseBody
    public ReturnData login(@RequestBody UserName userName){
        System.out.println("当前用户名:" + userName.getUsername()+";登录成功");
        return ReturnData.ok(userName.getUsername() + UUID.randomUUID().toString().replace("-", ""));
    }

    @PostMapping("/jianlian")
    @ResponseBody
    public ReturnData jianlian(@RequestBody UserName userName){
        Constans.relationMap.put(userName.getUsername(), userName.getUsername1());
        Constans.relationMap.put(userName.getUsername1(), userName.getUsername());
        return ReturnData.ok();
    }

    @PostMapping("/test")
    @ResponseBody
    public void test(@RequestBody ReceiveMessage rm) {
        System.out.println("服务端接收到点对点消息："+rm);
        String user = rm.getUser();
        String s = Constans.relationMap.get(user);
        template.convertAndSendToUser(s,"/message",rm.getMsg());
    }

}




