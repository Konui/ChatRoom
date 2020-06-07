package zzu.chatroom.eurekaclient.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zzu.chatroom.common.Entity.Room;

import java.util.List;

@Mapper
public interface RoomDao {
    //获取房间
    Room getRoom(@Param("name") String name);
    Room getRoombyId(@Param("id") Long id);
    //获取房间
    Room getRoomAll(@Param("id") Long id);

    List<Room> getUserRoom(@Param("uid")Long uid);
    //创建房间
    Boolean createRoom(@Param("name") String name, @Param("uid") Long uid);

    //修改房间名字
    Boolean reRoomName(@Param("id") Long id, @Param("name") String name);

    //删除房间
    Boolean delRoom(@Param("id") Long id);

    //添加用户
    Boolean addUser(@Param("uid") Long uid, @Param("rid") Long rid);
    //删除用户
    Boolean delUser(@Param("rid") Long rid, @Param("uid") Long uid);
}
