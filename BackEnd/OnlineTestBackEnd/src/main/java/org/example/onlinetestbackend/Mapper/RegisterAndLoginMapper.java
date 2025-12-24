package org.example.onlinetestbackend.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegisterAndLoginMapper {
    /**
     * 清除该地址已有验证码
     * @param email 邮箱
     */
    public void cleanCode(String email);

    /**
     * 存入该邮箱的对应验证码
     * @param email 邮箱
     * @param code 加密后的六位验证码
     */
    public void insertCode(String email,String code);

    /**
     * 查询该邮箱的对应验证码
     * @param email 用户 ID
     * @return 数据库内加密后的验证码
     */
    public String checkCode(String email);
}
