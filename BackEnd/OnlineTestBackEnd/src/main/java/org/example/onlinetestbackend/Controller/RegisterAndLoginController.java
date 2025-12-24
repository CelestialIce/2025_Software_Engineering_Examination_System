package org.example.onlinetestbackend.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.onlinetestbackend.Service.RegisterAndLoginService;
import org.example.onlinetestbackend.pojo.Result;
import org.example.onlinetestbackend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600) // 允许所有来源访问
@RestController
public class RegisterAndLoginController {
    @Autowired
    RegisterAndLoginService registerAndLoginService;

    /**
     * 用户登录
     * @param user 用户对象，包含用户ID和密码
     * @return 登录结果，成功返回1和JWT，失败返回0和错误信息
     */
    @RequestMapping("/login")
    public Result login(User user) {
        try {
            String jwt = registerAndLoginService.login(user.getUserid(), user.getPassword());
            if (jwt != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", jwt);
                response.put("admin", user.getUserid() == 10000 ? 1:0);
                return new Result(1, jwt);
            }
            return new Result(0, "ID或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(0, "系统错误");
        }
    }

    /**
     * 发送邮箱验证码
     * @param json
     */
    @RequestMapping("/sendCode")
    public Result sendCode(@RequestBody JSONObject json) {
        try {
            if(registerAndLoginService.sendCode(json.get("email").toString()))
            return new Result(1, "发送成功");
            else return new Result(0,"该邮箱已被注册");
        } catch (Exception e){
            e.printStackTrace();
            return new Result(0, "发送失败");
        }
    }

    /**
     * 新用户注册
     * @param json
     * @return 注册结果，成功返回1和JWT，失败返回0和错误信息
     */
    @RequestMapping("/register")
    public Result register(@RequestBody JSONObject json) {
        try {
            User user = JSON.parseObject(json.toString(), User.class);
            user.setAdmin(0);
            String code=json.getString("code");
            return new Result(1, registerAndLoginService.register(user,code));
        } catch (Exception e){
            e.printStackTrace();
            return new Result(0, "注册失败");
        }
    }
}
