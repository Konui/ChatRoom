package zzu.chatroom.common.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message  {
    private Long id;
    private Long room_id;
    private String type;
    private Long from_uid;
    private Long to_uid;
    private String content;
    private Date time;
}
