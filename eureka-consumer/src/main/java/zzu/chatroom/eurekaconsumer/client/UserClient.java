package zzu.chatroom.eurekaconsumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.common.Entity.Room;
import zzu.chatroom.common.Entity.User;

import java.util.Map;

@FeignClient("eureka-client")
public interface UserClient {
    //
    @GetMapping(value = "/pw", consumes = "application/json")
    boolean checkPw(User user);

    @GetMapping(value = "/user", consumes = "application/json")
    User getUser(User user);

    //
    @PostMapping(value = "/user")
    boolean addUser(@RequestBody User user);
    @PostMapping(value = "/msg")
    boolean insertMsg(@RequestBody Message msg);

    @PostMapping(value = "/pw")
    boolean rePw(@RequestBody User user);

    @GetMapping(value = "/friend/{uid}/{fname}", consumes = "application/json")
    boolean addFriend(@PathVariable(value = "uid") Long uid,@PathVariable(value = "fname")String fname);
    @GetMapping(value = "/room/{uid}/{name}",consumes = "application/json")
    boolean createRoom(@PathVariable(value = "uid") Long uid,@PathVariable(value = "name")String name);
    @GetMapping(value = "/addroom/{uid}/{rid}", consumes = "application/json")
     boolean addRoom(@PathVariable(value = "uid")Long uid ,@PathVariable(value = "rid")String rid);
    @GetMapping(value = "/headimg",consumes = "application/json")
     boolean updateHeadImg(@RequestParam(value = "id") Long id, @RequestParam("url") String url);
    @GetMapping(value= "/name/{uid}" , consumes = "application/json")
    String getName(@PathVariable(value = "uid")Long uid);
    @GetMapping(value = "/rmFriend/{uid}/{fid}",consumes = "application/json")
    boolean delFriend(@PathVariable("uid")Long uid,@PathVariable("fid")Long fid);
    @GetMapping(value = "/rmRoom/{uid}/{rid}",consumes = "application/json")
    boolean delRoomUser(@PathVariable("uid")Long uid,@PathVariable("rid")Long rid);
    @GetMapping(value = "/onlinemeb", consumes = "application/json")
    Map<String,String> getOnlineMeb();
}
