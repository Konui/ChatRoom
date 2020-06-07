package zzu.chatroom.common.Entity;

import lombok.Data;

@Data
public class ResponseMsg {
    private int code = 400;
    private String msg = "操作出错";
    private Object data;

}
