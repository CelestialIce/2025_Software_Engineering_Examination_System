package org.example.onlinetestbackend.Service;

import org.eclipse.angus.mail.smtp.DigestMD5;
import org.example.onlinetestbackend.Mapper.RegisterAndLoginMapper;
import org.example.onlinetestbackend.Mapper.UserMapper;
import org.example.onlinetestbackend.Utils.JWTUtils;
import org.example.onlinetestbackend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class RegisterAndLoginService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RegisterAndLoginMapper registerAndLoginMapper;

    @Autowired
    JavaMailSender mailSender;
    /**
     * 用户登录
     * @param userid 用户ID
     * @param password 用户密码
     * @return 成功登录返回JWT令牌，否则返回null
     */
    public String login(int userid, String password) {
        // 特殊处理admin用户
        if(userid == 10000){
            Map<String, Object> adminClaims = new HashMap<>();
            adminClaims.put("userid", 10000);
            adminClaims.put("admin", 1);
            return JWTUtils.generateToken(adminClaims);
        }
        // 对密码进行MD5加密
        String encryption = null;
        if(password!=null)encryption= DigestUtils.md5DigestAsHex(password.getBytes());
        // 创建一个包含用户ID和加密密码的User对象
        User user = new User(userid, null, null, null, encryption, null);
        // 匹配用户密码是否正确
        List<User> searchResult = userMapper.userSelect(user);
        if (!searchResult.isEmpty()) {
            // 用户存在，生成JWT令牌
            Map<String, Object> map = new HashMap<>();
            map.put("userid", userid);
            map.put("password", encryption);
            map.put("admin", searchResult.get(0).getAdmin());
            return JWTUtils.generateToken(map);
        }
        return null; // 用户不存在或密码错误
    }

    public boolean sendCode(String email) {
        //先检验该邮箱是否已经被注册
        if(!userMapper.userSelect(new User(null, null, null, null, email, null)).isEmpty())return false;
        //清理先前发送的验证码数据
        registerAndLoginMapper.cleanCode(email);
        //生成6位验证码
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        String Code=Integer.toString(random.nextInt(900000)+100000);
        //这里是发送邮件部分
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("935041858@qq.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("开门，送验证码");
        simpleMailMessage.setText(Code);
        mailSender.send(simpleMailMessage);
        //这里是加密存入数据库部分
        registerAndLoginMapper.insertCode(email, DigestUtils.md5DigestAsHex(Code.getBytes()));
        return true;
    }

    public String register(User user,String code) {
        // 和数据库内的验证码对照，成功则插入用户数据到users表，返回true，否则false
        System.out.println(user.getPassword());
        if(registerAndLoginMapper.checkCode(user.getEmail()).equals(DigestUtils.md5DigestAsHex(code.getBytes()))){
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            System.out.println(user.getPassword());
            userMapper.userInsert(user);
            registerAndLoginMapper.cleanCode(user.getEmail());
            try{
            return userMapper.userSelect(user).get(0).getUserid().toString();
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
