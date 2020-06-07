package zzu.chatroom.eurekaconsumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.common.Entity.ResponseMsg;
import zzu.chatroom.common.Entity.Room;
import zzu.chatroom.common.Entity.User;
import zzu.chatroom.common.Untils.JsonUtil;
import zzu.chatroom.eurekaconsumer.service.UserService;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Value("${redis.channel.msgToAll}")
    private String msgToAll;
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/pw")
    public ResponseMsg checkPw(User user) {
        log.info("接受到请求，username:"+user.getName()+",password:"+user.getPassword());
        ResponseMsg msg=new ResponseMsg();
        if(userService.checkPw(user)){
            User u=userService.getUser(user);
            user.setId(u.getId());
            user.setPassword(null);
            user.setHeadimg(u.getHeadimg());
            msg.setData(user);
            msg.setCode(200);
            msg.setMsg("登录成功");
        }else{
            msg.setCode(400);
            msg.setMsg("用户名或密码错误");
        }
        return msg;
    }

    @PostMapping("/user")
    public ResponseMsg addUser(User user) {
        ResponseMsg msg=new ResponseMsg();
        if(userService.addUser(user)){
            msg.setCode(200);
            msg.setMsg("注册成功");
        }
        return msg;
    }

    @GetMapping("/name/{uid}")
    public ResponseMsg getName(@PathVariable("uid") Long uid){
        ResponseMsg msg=new ResponseMsg();
        String name=userService.getName(uid);
        if(name!=""){
            msg.setCode(200);
            msg.setData(name);
        }else{
            msg.setCode(400);
            msg.setData("unknow");
        }
        return msg;
    }

    @GetMapping("/room/{uid}/{name}")
    public ResponseMsg createRoom(@PathVariable("uid") Long uid,@PathVariable("name")String name){
        ResponseMsg msg=new ResponseMsg();
        if(userService.createRoom(uid,name)){
            Message m=new Message();
            m.setType("init");
            m.setTo_uid(uid);
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(m));
            msg.setCode(200);
            msg.setMsg("创建成功");
        }
        return msg;
    }

    @GetMapping("/addroom/{uid}/{rid}")
    public ResponseMsg addRoom(@PathVariable("uid")Long uid,@PathVariable("rid")String rid){
        ResponseMsg msg=new ResponseMsg();
        if(userService.addRoom(uid,rid)){
            Message m=new Message();
            m.setType("init");
            m.setTo_uid(uid);
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(m));

            msg.setCode(200);
            msg.setMsg("添加成功");
        }
        return msg;
    }
    @GetMapping("/friend/{uid}/{fname}")
    public ResponseMsg addFriend(@PathVariable("uid") Long uid,@PathVariable("fname")String fname){
        ResponseMsg msg=new ResponseMsg();
        if(userService.addFriend(uid,fname)){
            Message m=new Message();
            m.setType("init");
            User f=new User();
            f.setName(fname);
            m.setTo_uid(userService.getUser(f).getId());
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(m));
            m.setTo_uid(uid);
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(m));
            msg.setCode(200);
            msg.setMsg("添加成功");
        }
        return msg;
    }

    @PostMapping("/pw")
    public ResponseMsg rePw(User user) {
        ResponseMsg msg=new ResponseMsg();
        if(userService.rePw(user)){
            msg.setCode(200);
            msg.setMsg("重置成功");
        }
        return msg;
    }
    @GetMapping("/rmFriend/{uid}/{fid}")
    public ResponseMsg delFriend(@PathVariable("uid")Long uid,@PathVariable("fid")Long fid){
        ResponseMsg msg=new ResponseMsg();
        if(userService.delFriend(uid,fid)){
            Message m=new Message();
            m.setType("del");
            m.setFrom_uid(uid);
            m.setTo_uid(fid);
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(m));
            msg.setCode(200);
            msg.setMsg("删除成功");
        }
        return msg;
    }
    @GetMapping("/rmRoom/{uid}/{rid}")
    public ResponseMsg delRoomUser(@PathVariable("uid")Long uid ,@PathVariable("rid")Long rid){
        ResponseMsg msg=new ResponseMsg();
        if(userService.delRoomUser(uid,rid)){
            msg.setCode(200);
            msg.setMsg("退出成功");
        }else{
            msg.setCode(400);
        }
        return msg;
    }

}
