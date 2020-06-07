package zzu.chatroom.eurekaconsumer.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisListenerHandle extends MessageListenerAdapter {

    @Value("${server.port}")
    private String serverPort;

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

/*

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String rawMsg;
        String topic;
        try {
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);
            log.info("Received raw message from topic:" + topic + ", raw message content：" + rawMsg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }


        if (msgToAll.equals(topic)) {
            log.info("Send message to all users:" + rawMsg);
            //zzu.chatroom.common.Entity.Message chatMessage = JsonUtil.parseJsonToObj(rawMsg, zzu.chatroom.common.Entity.Message.class);
            chatService.sendMsg(rawMsg);
        } else {
            log.warn("No further operation with this topic!");
        }
    }
    */
}
