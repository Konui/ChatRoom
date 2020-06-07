package zzu.chatroom.eurekaclient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zzu.chatroom.common.Entity.RedisName;
import zzu.chatroom.common.Entity.Room;
import zzu.chatroom.common.Entity.User;
import zzu.chatroom.common.Entity.initList;
import zzu.chatroom.common.Untils.JsonUtil;
import zzu.chatroom.eurekaclient.dao.MessageDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RedisService {
    @Autowired
    RedisTemplate  redisTemplate;
    @Autowired
    MessageDao messageDao;

    //上线
    public void addRoom(Room room,User user){
        redisTemplate.opsForSet().add(RedisName.ROOM_ID+room.getId(),user.getId());
    }
    //下线
    public void dlRoom(Room room,User user){
        redisTemplate.opsForSet().remove(RedisName.ROOM_ID+room.getId(),user.getId());
    }

    //添加新用户到房间
    public void addUserToRoom(Room room,User user){
        redisTemplate.opsForSet().add(RedisName.ROOM_ID+room.getId(),user.getId());
        List<initList> list=new ArrayList<>();
        list.add(initList.RoomtoList(room));
        redisTemplate.opsForValue().set(RedisName.FRIENDS_ID+user.getId(),JsonUtil.parseObjToJson(list));
    }
    //用户退出房间
    public void delUserToRoom(Room room,User user){
        redisTemplate.opsForSet().remove(RedisName.ROOM_ID+room.getId(),user.getId());
        redisTemplate.opsForList().remove(RedisName.FRIENDS_ID+user.getId(),1,initList.UsertoList(user));
    }

    //初始化列表
    public void init(User user){
        List<initList> list=new ArrayList<>();
        if(user.getFriendList()!=null){
            for(User f:user.getFriendList()){
                list.add(initList.UsertoList(f,messageDao.getUserMsg(user.getId(),f.getId())));
            }
        }
        if(user.getRoomList()!=null){
            for(Room r:user.getRoomList()){
                list.add(initList.RoomtoList(r,messageDao.getRoomMsg(r.getId())));
            }
        }
        redisTemplate.opsForValue().set(RedisName.FRIENDS_ID+user.getId(), JsonUtil.parseObjToJson(list));
    }

    //添加好友
    public void addFriend(User user ,User friend){
        List<initList> list1=new ArrayList<>();
        list1.add(initList.UsertoList(friend));
        redisTemplate.opsForValue().set(RedisName.FRIENDS_ID+user.getId(),JsonUtil.parseObjToJson(list1));
        //好友通知
        List<initList> list2=new ArrayList<>();
        list2.add(initList.UsertoList(user));
        redisTemplate.opsForValue().set(RedisName.FRIENDS_ID+friend.getId(),JsonUtil.parseObjToJson(list2));
    }
    //删除好友
    public void delFriend(User user,User friend){
        redisTemplate.opsForList().remove(RedisName.FRIENDS_ID+user.getId(),1,initList.UsertoList(friend));
        if(redisTemplate.hasKey(RedisName.FRIENDS_ID+user.getId())){
            redisTemplate.opsForList().remove(RedisName.FRIENDS_ID+friend.getId(),1,initList.UsertoList(user));
        }
    }

    public void delRoom(Room room){
        redisTemplate.delete(RedisName.ROOM_ID+room.getId());
    }
    public void delUser(User user){
        redisTemplate.delete(RedisName.FRIENDS_ID+user.getId());
    }

}
