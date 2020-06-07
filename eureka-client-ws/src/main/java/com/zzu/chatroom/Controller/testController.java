package com.zzu.chatroom.Controller;

import com.zzu.chatroom.Util.SpringUtil;
import com.zzu.chatroom.WebSocket.WebSocketConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class testController {


    @RequestMapping("/t")
    public String test() {
        WebSocketConfig w1 = SpringUtil.getBean(WebSocketConfig.class);
        System.out.println(w1.registry.toString());
        return "test";
    }
}
