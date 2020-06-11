package zzu.chatroom.eurekaconsumer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.common.Entity.Room;
import zzu.chatroom.common.Entity.User;
import zzu.chatroom.eurekaconsumer.client.UserClient;

import java.util.Map;

@Component
public class UserService {
    @Autowired
    UserClient userClient;

    // @HystrixCommand(fallbackMethod = "fallback")
    public boolean checkPw(User user) {
        return userClient.checkPw(user);
    }

    public User getUser(User user){
        return userClient.getUser(user);
    }
    // @HystrixCommand(fallbackMethod = "fallback")
    public boolean addUser(User user) {
        return userClient.addUser(user);
    }
    public String getName(Long uid){return userClient.getName(uid);}
    public boolean rePw(User user){
        return userClient.rePw(user);
    }
    public boolean insertMsg(Message msg){return userClient.insertMsg(msg);}
    public boolean addFriend(Long uid,String fname){
        return userClient.addFriend(uid,fname);
    }
    public boolean createRoom(Long uid, String name){return  userClient.createRoom(uid,name);}
    public boolean addRoom(Long uid,String rid){return userClient.addRoom(uid,rid);}
    public boolean updateHeadImg(Long id,String url){return userClient.updateHeadImg(id,url);}

    public boolean delFriend(Long uid,Long fid){
        return userClient.delFriend(uid,fid);
    }
    public boolean delRoomUser(Long uid,Long rid){
        return userClient.delRoomUser(uid,rid);
    }
    public Map<String,String> getOnlienMeb(){return userClient.getOnlineMeb();}

    /*
    public String fallback(User user){
        return "当前服务繁忙，请稍后重试。";
    }
     */
}
