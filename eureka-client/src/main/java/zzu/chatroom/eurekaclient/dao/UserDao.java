package zzu.chatroom.eurekaclient.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zzu.chatroom.common.Entity.User;

import java.util.List;

@Mapper
public interface UserDao {

    //获取用户信息
    User getUserById(@Param("id") Long id);

    User getUser(@Param("name") String name);

    //创建用户
    Boolean createUser(@Param("name") String name, @Param("password") String password);

    //修改密码
    Boolean rePw(@Param("id") Long id, @Param("pw") String pw);

    //验证密码
    Boolean checkPw(@Param("name") String name, @Param("pw") String pw);

    //添加好友
    Boolean addFriend(@Param("uid1") Long id, @Param("uid2") Long fid);

    //删除好友
    Boolean delFriend(@Param("uid") Long id, @Param("fid") Long fid);

    Boolean updateHeadImg(@Param("id")Long id,@Param("url") String url);
    String getName(@Param("id")Long id);
}
