package com.example.login.service;

import com.example.login.req.SysUserLoginReq;
import com.example.login.req.SysUserSaveReq;
import com.example.login.resp.SysUserLoginResp;

public interface SysUserService {
    void register(SysUserSaveReq req);

    SysUserLoginResp login(SysUserLoginReq req);
}
