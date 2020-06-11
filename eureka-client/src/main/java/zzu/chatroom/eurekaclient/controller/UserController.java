package zzu.chatroom.eurekaclient.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.common.Entity.Room;
import zzu.chatroom.common.Entity.User;
import zzu.chatroom.eurekaclient.service.MsgService;
import zzu.chatroom.eurekaclient.service.RedisService;
import zzu.chatroom.eurekaclient.service.RoomService;
import zzu.chatroom.eurekaclient.service.UserService;

import java.util.Map;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    RedisService redisService;
    @Autowired
    MsgService msgService;




    //创建用户
    @PostMapping("/user")
    public boolean createUser(@RequestBody User user) {
        if(userService.addUser(user)){
            roomService.addUser(userService.getUser(user).getId(),1L);
            return true;
        }
        return false;
    }
    //插入消息
    @PostMapping("/msg")
    public boolean insertMsg(@RequestBody Message msg){
        return msgService.insertMsg(msg);
    }
    //获取用户
    @GetMapping(value = "/user", consumes = "application/json")
    public User getUser(@RequestBody User user) {
        return userService.getUser(user);
    }

    //验证密码
    @GetMapping(value = "/pw", consumes = "application/json")
    public boolean checkPw(@RequestBody User user) {
        log.info("接受到请求，username:"+user.getName()+",password:"+user.getPassword());
        System.out.println(userService.checkPw(user));
        if(userService.checkPw(user)){
            User u=userService.getUser(user);
            u.setRoomList(roomService.getUserRoom(u.getId()));
            redisService.init(u);
            for(Room r:u.getRoomList()){
                redisService.addRoom(r,u);
            }
            return true;
        }
        return false;
    }

    //修改密码
    @PostMapping("/pw")
    public boolean rePw(@RequestBody User user) {
        return userService.rePw(user.getId(),user.getPassword());
    }

    //添加好友
    //*****添加列表
    @GetMapping(value = "/friend/{uid}/{fname}", consumes = "application/json")
    public boolean addFriend(@PathVariable("uid") Long uid, @PathVariable("fname") String fname) {
        User f=new User();
        f.setName(fname);
        f=userService.getUser(f);

        if(userService.addFriend(uid,f.getId())){
            User u=new User();
            u.setId(uid);
            u=userService.getUserById(u);
            redisService.addFriend(u,f);
            return true;
        }
        return false;
    }
    @GetMapping(value = "/name/{uid}" , consumes = "application/json")
    public String getName(@PathVariable("uid")Long uid){
        return userService.getName(uid);
    }
    //删除好友
    @GetMapping(value = "/rmFriend/{uid}/{fid}",consumes = "application/json")
    public boolean delFriend(@PathVariable("uid") Long uid, @PathVariable("fid") Long fid) {
       //数据库内容删除，通知其他用户删除
        User u=new User();
        User f=new User();
        u.setId(uid);
        f.setId(fid);
        redisService.delFriend(u,f);
        return userService.delFriend(uid, fid);
    }
    @GetMapping(value = "/rmRoom/{uid}/{rid}",consumes = "application/json")
    public boolean delRoom(@PathVariable("uid")Long uid,@PathVariable("rid")Long rid){
        redisService.delUserToRoom(rid,uid);
        return roomService.delUser(rid,uid);
    }

    //创建房间
    //*****添加列表
    @GetMapping(value = "/room/{uid}/{name}", consumes = "application/json")
    public boolean createRoom(@PathVariable("uid") Long uid, @PathVariable("name") String name){
        if(roomService.createRoom(name,uid)){
            User u=new User();
            u.setId(uid);
            u=userService.getUserById(u);
            roomService.addUser(u.getId(),name);
            redisService.addUserToRoom(roomService.getRoom(name),u);
            return true;
        }
        return false;
    }
    //加入房间
    //*****添加列表
    @GetMapping(value = "/addroom/{uid}/{rid}", consumes = "application/json")
    public boolean addRoom(@PathVariable(value = "uid")Long uid ,@PathVariable(value = "rid")String rid){
        System.out.println(uid+","+rid);
        if(roomService.addUser(uid,rid)){
            User u=new User();
            u.setId(uid);
            u=userService.getUserById(u);
            redisService.addUserToRoom(roomService.getRoom(rid),u);
            return true;
        }
        return false;
    }

    @GetMapping(value = "/headimg",consumes = "application/json")
    public boolean updateHeadImg(@RequestParam(value = "id") Long id, @RequestParam("url") String url){
        return userService.updateHeadImg(id,url);
    }

    @GetMapping(value ="/onlinemeb", consumes = "application/json")
    public Map<String,String> getOnlineMeb(){
        return redisService.getOnlineRoom();
    }
}
