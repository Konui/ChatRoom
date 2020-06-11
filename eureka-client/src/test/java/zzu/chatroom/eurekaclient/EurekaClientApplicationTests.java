package zzu.chatroom.eurekaclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zzu.chatroom.common.Entity.Room;
import zzu.chatroom.eurekaclient.service.RoomService;

@SpringBootTest
class EurekaClientApplicationTests {

    @Autowired
    RoomService roomService;

    @Test
    void contextLoads() {
        Room room=roomService.getRoom(Long.parseLong("1"));
    }

}
