package zzu.chatroom.common.Entity;

import lombok.Data;

import java.util.*;

@Data
public class initList {
    private Long id;
    private String name;
    private String url;
    private boolean isRoom;
    private List<Message> msgList;

    public initList(Long id, String name, String url, boolean isRoom) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.isRoom = isRoom;
    }
    public initList(Long id, String name, String url, boolean isRoom,List<Message> msgList) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.isRoom = isRoom;
        this.msgList=msgList;
    }

    public static initList UsertoList(User user){
        return new initList(user.getId(),user.getName(),user.getHeadimg(),false);
    }
    public static initList RoomtoList(Room room){
        return new initList(room.getId(),room.getName(),room.getHeadimg(),true);
    }
    public static List<initList> UsertoList(List<User> list){
        List<initList> res=new ArrayList<>();
        for(User u:list){
            res.add(UsertoList(u));
        }
        return res;
    }

    public static List<initList> RoomtoList(List<Room> list){
        List<initList> res=new ArrayList<>();
        for(Room u:list){
            res.add(RoomtoList(u));
        }
        return res;
    }

    public static initList UsertoList(User user,List<Message> msgList){
        Collections.sort(msgList, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                int flg=o1.getTime().compareTo(o2.getTime());
                return flg;
            }
        });
        return new initList(user.getId(),user.getName(),user.getHeadimg(),false,msgList);
    }
    public static initList RoomtoList(Room room,List<Message> msgList){
        Collections.sort(msgList, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                int flg=o1.getTime().compareTo(o2.getTime());
                return flg;
            }
        });
        return new initList(room.getId(),room.getName(),room.getHeadimg(),true,msgList);
    }
    public static List<initList> UsertoList(List<User> list, Map<Long,List<Message>> map){
        List<initList> res=new ArrayList<>();
        for(User u:list){
            Collections.sort(map.get(u.getId()), new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    int flg=o1.getTime().compareTo(o2.getTime());
                    return flg;
                }
            });
            res.add(UsertoList(u,map.get(u.getId())));
        }
        return res;
    }

    public static List<initList> RoomtoList(List<Room> list,Map<Long,List<Message>> map){
        List<initList> res=new ArrayList<>();
        for(Room u:list){
            Collections.sort(map.get(u.getId()), new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    int flg=o1.getTime().compareTo(o2.getTime());
                    return flg;
                }
            });
            res.add(RoomtoList(u,map.get(u.getId())));
        }
        return res;
    }
}
