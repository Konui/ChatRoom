package zzu.chatroom.eurekaconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import zzu.chatroom.eurekaconsumer.client.FileClient;

@Component
public class FileService {
    @Autowired
    FileClient fileClient;

    public String uploadHeadImg(MultipartFile file,Long uid){
        return fileClient.uploadHeadImg(file,uid);
    }
    public String uploadFile(MultipartFile file,Long uid){
        return fileClient.uploadFile(file,uid);
    }
}
