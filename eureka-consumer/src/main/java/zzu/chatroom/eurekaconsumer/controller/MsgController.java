package zzu.chatroom.eurekaconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.common.Entity.ResponseMsg;
import zzu.chatroom.eurekaconsumer.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class MsgController {
    @Autowired
    UserService userService;

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @PostMapping("/msg")
    public ResponseMsg insertMsg(Message msg){
        ResponseMsg m=new ResponseMsg();
        if(userService.insertMsg(msg)){
            m.setCode(200);
        }
        return m;
    }
}
