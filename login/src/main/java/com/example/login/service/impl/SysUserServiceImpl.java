package com.example.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.login.entity.SysUserEntity;
import com.example.login.mapper.SysUserMapper;
import com.example.login.req.SysUserLoginReq;
import com.example.login.req.SysUserSaveReq;
import com.example.login.resp.SysUserLoginResp;
import com.example.login.service.SysUserService;
import com.example.login.utils.CopyUtil;
import com.example.login.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service


public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SnowFlake snowFlake;


    @Override
    public void register(SysUserSaveReq req) {
       SysUserEntity user = CopyUtil.copy(req, SysUserEntity.class);
       if (ObjectUtils.isEmpty(req.getId())) {
           SysUserEntity userDb =selectByLoginName(req.getLoginName());
           if (ObjectUtils.isEmpty(userDb)) {
               user.setId(snowFlake.nextId());
               sysUserMapper.insert(user);
           }

       }


    }

    @Override
    public SysUserLoginResp login(SysUserLoginReq req) {
        SysUserEntity userDb = selectByLoginName(req.getLoginName());
        if(ObjectUtils.isEmpty(userDb)) {
            //用户不存在
            return null;
        }else {
            //登陆成功
            SysUserLoginResp userLoginResp = CopyUtil.copy(userDb, SysUserLoginResp.class);
            return userLoginResp;
        }

    }

    //查询LoginName是否被注册
    public SysUserEntity selectByLoginName(String loginName) {
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserEntity::getLoginName, loginName);// 是否有相同
        List<SysUserEntity> userEntityList = sysUserMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(userEntityList)){//验证是否为空
            return null;
        }else {
            return userEntityList.get(0);
        }
    }


}
