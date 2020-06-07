package zzu.chatroom.eurekaclient.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zzu.chatroom.common.Entity.Message;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageDao {
    //插入信息
    Boolean insertMsg(@Param("roomid") Long roomid, @Param("type") String type, @Param("fromid") Long fromid, @Param("toid") Long toid, @Param("content") String content, @Param("time") Date time);

    //获取聊天室记录
    List<Message> getRoomMsg(@Param("id") Long room_id);

    //获取用户记录
    List<Message> getUserMsg(@Param("uid") Long uid, @Param("fid") Long fid);

//    //个人记录查询
//    List<Message> searchMsg(@Param("uid") Long uid, @Param("keyWord") String keyWord);

    //删除信息
    Boolean delMsg(@Param("id") Long id);
}

