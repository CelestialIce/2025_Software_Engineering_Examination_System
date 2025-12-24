package org.example.onlinetestbackend.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.onlinetestbackend.pojo.StorageQuestion;

import java.util.List;

@Mapper // 这个注解表明这是一个 MyBatis 的 Mapper 接口，用于映射 SQL 语句到方法上
public interface QuestionMapper {

    /**
     * 插入一个新的问题到数据库
     * @param storageQuestion 要插入的 StorageQuestion 对象
     */
    public void insertQuestion(StorageQuestion storageQuestion);

    /**
     * 根据问题 ID 删除问题
     * @param qids 要删除的问题 ID 数组
     */
    public void deleteQuestion(int[] qids);

    /**
     * 根据给定的条件查询问题
     * @param storageQuestion 查询条件封装成的 StorageQuestion 对象
     * @return 查询到的 StorageQuestion 对象列表
     */
    public List<StorageQuestion> selectQuestion(StorageQuestion storageQuestion);

    /**
     * 更新一个问题的信息
     * @param storageQuestion 要更新的 StorageQuestion 对象
     */
    public void updateQuestion(StorageQuestion storageQuestion);
}