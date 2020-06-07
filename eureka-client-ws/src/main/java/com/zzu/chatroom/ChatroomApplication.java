package com.zzu.chatroom;

import com.zzu.chatroom.Util.SpringUtil;
import com.zzu.chatroom.WebSocket.WebSocketConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatroomApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatroomApplication.class, args);


    }

}
