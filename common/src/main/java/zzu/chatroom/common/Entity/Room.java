package zzu.chatroom.common.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Room {
    private Long id;
    private String name;
    private Long admin_uid;
    private String headimg;
    private List<User> memberList;
}
