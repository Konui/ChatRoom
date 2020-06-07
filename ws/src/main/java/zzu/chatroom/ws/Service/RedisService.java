package zzu.chatroom.ws.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zzu.chatroom.common.Entity.RedisName;
import zzu.chatroom.common.Entity.User;
import zzu.chatroom.common.Entity.initList;

import java.util.List;
import java.util.Set;

@Service
public class RedisService {


    @Value("${server.port}")
    private String serverPort;

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Autowired
    RedisTemplate redisTemplate;

    public List<User> getFriends(Long id){
        return redisTemplate.opsForList().range(RedisName.FRIENDS_ID+id.toString(),0,-1);
    }
    public Set<User> getRoomList(Long id){
        return redisTemplate.opsForSet().members(RedisName.FRIENDS_ID+id.toString());
    }
    public void whenClose(Long uid){
        redisTemplate.delete(RedisName.FRIENDS_ID+uid.toString());
        Set<String> keys=redisTemplate.keys(RedisName.ROOM_ID+"*");
        if(keys!=null){
            for(String key:keys){
                redisTemplate.opsForSet().remove(key,uid);
            }
        }
    }
    public void sendToPub(String msg){
        redisTemplate.convertAndSend(msgToAll, msg);
    }

    public String initList(Long uid){
        String a=(String)redisTemplate.opsForValue().get(RedisName.FRIENDS_ID+uid.toString());
        redisTemplate.delete(RedisName.FRIENDS_ID+uid.toString());
        return a;
    }


}
