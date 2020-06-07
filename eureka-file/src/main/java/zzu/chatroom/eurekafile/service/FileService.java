package zzu.chatroom.eurekafile.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class FileService {
    private static final List<String> ALLOW_TYPES = Arrays.asList("image/png", "image/jpeg", "image/bmg");
    public String uploadHeadImg(MultipartFile file,Long uid) {
        String url="";
        try {
            String type=file.getContentType();
            if(!ALLOW_TYPES.contains(type)){
                log.info("图片格式只能为png、jpg、bmg");
            }
            BufferedImage img= ImageIO.read(file.getInputStream());
            if(img==null){
                log.info("图片为空");
            }
            //用户名加head
            File f=new File("F:\\Temp\\"+uid.toString()+"headImg."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            file.transferTo(f);
            url=f.getName();
        }catch (IOException e){
            log.error("上传文件失败",e);
        }finally {
            return url;
        }
    }

    public String uploadFile(MultipartFile file,Long uid){
        String url="";
        try {
            String type=file.getContentType();
            BufferedImage img= ImageIO.read(file.getInputStream());
            if(img==null){
                log.info("文件为空");
            }
            //用户名加head
            File f=new File("F:\\Temp\\"+uid.toString()+getTimeName(uid)+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            file.transferTo(f);
            url=f.getName();
        }catch (IOException e){
            log.error("上传文件失败",e);
        }finally {
            return url;
        }
    }
    public String getTimeName(long id){
        //用户名加时间戳
        String dateString = String.valueOf(Calendar.getInstance().getTimeInMillis());
        return "-"+dateString+".";
    }
}
