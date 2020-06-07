package zzu.chatroom.eurekaclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzu.chatroom.common.Entity.User;
import zzu.chatroom.eurekaclient.dao.UserDao;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    //验证密码
    public boolean checkPw(User user) {
        return userDao.checkPw(user.getName(),user.getPassword());
    }

    //获取用户
    public User getUser(User user) {
        return userDao.getUser(user.getName());
    }
    public User getUserById(User user){
        return userDao.getUserById(user.getId());
    }

    //添加用户
    public boolean addUser(User user) {
        return userDao.createUser(user.getName(), user.getPassword());
    }

    //重置密码
    public boolean rePw(Long id, String pw) {
        return userDao.rePw(id, pw);
    }

    //添加好友
    public boolean addFriend(Long uid, Long fid) {
        return userDao.addFriend(uid, fid);
    }

    //删除好友
    public boolean delFriend(Long uid, Long fid) {
        return userDao.delFriend(uid, fid);
    }

    public boolean updateHeadImg(Long id,String url){return userDao.updateHeadImg(id,url);}
    public String getName(Long uid){return userDao.getName(uid);}
}
