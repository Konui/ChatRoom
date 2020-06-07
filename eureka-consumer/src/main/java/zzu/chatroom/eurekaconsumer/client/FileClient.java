package zzu.chatroom.eurekaconsumer.client;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "eureka-file",configuration = FileClient.MultipartSupportConfig.class)
public interface FileClient {
    //@PostMapping(value = "/headImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    @PostMapping(value= "/headImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
       String uploadHeadImg(@RequestPart(value = "file") MultipartFile file, @RequestParam("uid")Long uid);

    @PostMapping(value= "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam("uid")Long uid);


    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }

}
