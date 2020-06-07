package zzu.chatroom.ws.Service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.common.Entity.RedisName;
import zzu.chatroom.common.Entity.User;
import zzu.chatroom.common.Entity.initList;
import zzu.chatroom.common.Untils.JsonUtil;
import zzu.chatroom.ws.WebSocketServer;
import zzu.chatroom.common.Entity.initList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class chatService {
    @Autowired
    RedisTemplate redisTemplate;

    public void sendMsg(String rmsg){
        String ra=rmsg.replaceAll("\\\\", "");
        String ss=ra.substring(1, ra.length() - 1);
        Message msg=JsonUtil.parseJsonToObj(ss,Message.class);
        if("init".equals(msg.getType())){
            WebSocketServer ws =WebSocketServer.wsMap.get(msg.getTo_uid());
            if(ws!=null){
                ws.sendMsg(initList(msg.getTo_uid()));
            }
            return;
        }
        if(msg.getRoom_id()>0){
            //群发消息
            log.info("接受到群发消息");
            Set<Long> set=redisTemplate.opsForSet().members(RedisName.ROOM_ID+msg.getRoom_id());
            System.out.println(set.toString());

            for(Map.Entry<Long,WebSocketServer> ws :WebSocketServer.wsMap.entrySet()){
                System.out.println(set.contains(ws.getKey().intValue()));
                if(set.contains(ws.getKey().intValue())){
                    ws.getValue().sendMsg(JsonUtil.parseObjToJson(msg));
                    log.info("已发送至"+ws.getValue().getUid());
                }
            }
        }else{
            //单发
            WebSocketServer ws =WebSocketServer.wsMap.get(msg.getTo_uid());
            if(ws!=null){
                ws.sendMsg(JsonUtil.parseObjToJson(msg));
            }
        }

    }

    public String initList(Long uid){
        String a=(String)redisTemplate.opsForValue().get(RedisName.FRIENDS_ID+uid.toString());
        redisTemplate.delete(RedisName.FRIENDS_ID+uid.toString());
        return a;
    }



}
