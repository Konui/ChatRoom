package zzu.chatroom.common.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class User {
    private Long id;
    private String name;
    private String password;
    private String headimg;
    private List<User> friendList;
    private List<Room> roomList;


}
