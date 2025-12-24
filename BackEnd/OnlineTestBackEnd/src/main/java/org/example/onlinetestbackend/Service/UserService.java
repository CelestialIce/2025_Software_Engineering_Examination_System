package org.example.onlinetestbackend.Service;

import org.example.onlinetestbackend.Mapper.UserMapper;
import org.example.onlinetestbackend.Utils.JWTUtils;
import org.example.onlinetestbackend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 插入新用户
     * @param user 用户对象
     */
    public void userInsert(User user) {
        // 对密码进行MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.userInsert(user);
    }

    /**
     * 删除用户
     * @param userids 用户ID数组
     */
    public void userDelete(int[] userids) {
        userMapper.userDelete(userids);
    }

    /**
     * 更新用户信息
     * @param user 用户对象
     */
    public void userUpdate(User user) {
        if (user.getPassword() != null) {
            // 对密码进行MD5加密
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        userMapper.userUpdate(user);
    }

    /**
     * 查询用户信息
     * @param user 用户对象
     * @return 查询到的用户列表
     */
    public List<User> userSelect(User user) {
        List<User> users = userMapper.userSelect(user);
        for (User userIns : users) {
            // 隐藏密码
            userIns.setPassword("~password~ ^_^");
        }
        return users;
    }
}