package org.example.onlinetestbackend.Controller;

import com.alibaba.fastjson.JSONObject;
import org.example.onlinetestbackend.Service.UserService;
import org.example.onlinetestbackend.pojo.Result;
import org.example.onlinetestbackend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 插入新用户
     * @param user 用户对象，包含用户信息
     * @return 插入结果，成功返回1，失败返回0
     */
    @RequestMapping("/User/userInsert")
    public Result userInsert(User user) {
        try {
            userService.userInsert(user);
            return new Result(1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 删除用户
     * @param userid 用户ID数组
     * @return 删除结果，成功返回1，失败返回0
     */
    @RequestMapping("/User/userDelete")
    public Result userDelete(int[] userid) {
        try {
            userService.userDelete(userid);
            return new Result(1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 更新用户信息
     * @return 更新结果，成功返回1，失败返回0
     */
    @RequestMapping("/User/userUpdate")
    public Result userUpdate(@RequestBody JSONObject jsonObject) {
        try {
            User user=jsonObject.toJavaObject(User.class);
            user.setPassword(null);
            userService.userUpdate(user);
            return new Result(1);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 查询用户信息
     * @param user 用户对象，包含查询条件
     * @return 查询结果，成功返回1和用户信息，失败返回0
     */
    @RequestMapping("/User/userSelect")
    public Result userSelect(User user) {
        try {
            return new Result(1, userService.userSelect(user));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(0);
        }
    }
}