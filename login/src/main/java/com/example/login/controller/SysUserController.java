package com.example.login.controller;

import com.example.login.req.SysUserLoginReq;
import com.example.login.req.SysUserSaveReq;
import com.example.login.resp.CommonResp;
import com.example.login.resp.SysUserLoginResp;
import com.example.login.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("register")
    public CommonResp register (@RequestBody SysUserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));//加密密码
        CommonResp resp = new CommonResp<>();
        sysUserService.register(req);
        return resp;
    }

    @PostMapping("login")
    public CommonResp login(@RequestBody SysUserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        SysUserLoginResp loginResp = sysUserService.login(req);
        resp.setContent(loginResp);
        return resp;

    }


}
