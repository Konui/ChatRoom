package zzu.chatroom.eurekafile.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zzu.chatroom.eurekafile.service.FileService;
@Slf4j
@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping(value = "/headImg",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String headImg(@RequestPart("file")MultipartFile file, @RequestParam("uid")Long uid){
        log.info("img upload");
        return fileService.uploadHeadImg(file,uid);
    }

    @PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart("file")MultipartFile file, @RequestParam("uid")Long uid){
        log.info("file upload");
        return fileService.uploadFile(file,uid);
    }
}
