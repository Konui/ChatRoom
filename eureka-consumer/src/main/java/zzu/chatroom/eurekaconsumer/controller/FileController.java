package zzu.chatroom.eurekaconsumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zzu.chatroom.common.Entity.ResponseMsg;
import zzu.chatroom.eurekaconsumer.service.FileService;
import zzu.chatroom.eurekaconsumer.service.UserService;

@Slf4j
@RestController
public class FileController {
    @Autowired
    FileService fileService;
    @Autowired
    UserService userService;

    @PostMapping("/headImg")
    public ResponseMsg headImg( MultipartFile file, @RequestParam("uid")Long uid){
        log.info("consumer upload");
        ResponseMsg msg=new ResponseMsg();
        String url=fileService.uploadHeadImg(file,uid);
        if(!"".equals(url)){
            userService.updateHeadImg(uid,url);
            msg.setCode(200);
            msg.setData(url);
            msg.setMsg("上传成功！");
        }else{
            msg.setCode(400);
            msg.setMsg("上传出错！");
        }
        log.info(String.valueOf(msg.getCode()));
        return msg;
    }

    @PostMapping("/uploadFile")
    public ResponseMsg uploadFile( MultipartFile file, @RequestParam("uid")Long uid){
        log.info("consumer upload");
        ResponseMsg msg=new ResponseMsg();
        String url=fileService.uploadFile(file,uid);
        if(!"".equals(url)){
            msg.setCode(200);
            msg.setData(url);
            msg.setMsg("上传成功！");
        }else{
            msg.setCode(400);
            msg.setMsg("上传出错！");
        }
        log.info(String.valueOf(msg.getCode()));
        return msg;
    }

}
