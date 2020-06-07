package zzu.chatroom.eurekaclient.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzu.chatroom.common.Entity.Room;
import zzu.chatroom.eurekaclient.dao.RoomDao;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomDao roomDao;

    public Room getRoom(String name){return roomDao.getRoom(name);}
    public Room getRoom(Long id){return roomDao.getRoombyId(id);}
    //创建房间
    public boolean createRoom(String name, Long uid) {
        return roomDao.createRoom(name, uid);
    }

    //修改房间名
    public boolean reRoomName(Room room) {
        return roomDao.reRoomName(room.getId(), room.getName());
    }

    //删除房间
    public boolean delRoom(Long id) {
        return roomDao.delRoom(id);
    }

    //添加用户
    public boolean addUser(Long uid, String name) {
        return roomDao.addUser(uid, getRoom(name).getId());
    }
    public boolean addUser(Long uid, Long rid) {
        return roomDao.addUser(uid, rid);
    }
    //删除用户
    public boolean delUser(Long rid, Long uid) {
        return roomDao.delUser(rid, uid);
    }

    public List<Room> getUserRoom(Long uid){
        return roomDao.getUserRoom(uid);
    }
}
