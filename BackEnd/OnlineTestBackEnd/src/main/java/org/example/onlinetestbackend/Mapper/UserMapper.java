package org.example.onlinetestbackend.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.onlinetestbackend.pojo.User;

import java.util.List;

@Mapper // 这个注解表明这是一个 MyBatis 的 Mapper 接口，用于映射 SQL 语句到方法上
public interface UserMapper {

    /**
     * 插入新用户
     * @param user 用户对象
     */
    public void userInsert(User user);

    /**
     * 删除用户
     * @param userids 用户 ID 数组
     */
    public void userDelete(int[] userids);

    /**
     * 更新用户信息
     * @param user 用户对象
     */
    public void userUpdate(User user);

    /**
     * 查询用户
     * @param user 用户对象（作为查询条件）
     * @return 包含符合条件的用户对象的列表
     */
    public List<User> userSelect(User user);
}