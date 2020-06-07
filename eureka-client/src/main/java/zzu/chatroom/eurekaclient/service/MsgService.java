package zzu.chatroom.eurekaclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.eurekaclient.dao.MessageDao;

import java.util.List;

@Service
public class MsgService {
    @Autowired
    MessageDao messageDao;

    //插入信息
    public boolean insertMsg(Message msg) {
        return messageDao.insertMsg(msg.getRoom_id(), msg.getType(), msg.getFrom_uid(), msg.getTo_uid(), msg.getContent(), msg.getTime());
    }

    //获取聊天室记录
    public List<Message> getRoomMsg(Long id) {
        return messageDao.getRoomMsg(id);
    }

    public List<Message> getMsg(Long uid,Long fid){return messageDao.getUserMsg(uid,fid);}
    //获取个人记录查询
    //public List<Message> searchMsg(Long uid, Long fid) { return messageDao.getUserMsg(uid, fid); }

    //删除信息
    public boolean delMsg(Long id) {
        return messageDao.delMsg(id);
    }
}
