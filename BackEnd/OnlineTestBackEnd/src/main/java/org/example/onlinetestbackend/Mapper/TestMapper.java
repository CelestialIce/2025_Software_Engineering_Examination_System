package org.example.onlinetestbackend.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.onlinetestbackend.pojo.PostQuestion;
import org.example.onlinetestbackend.pojo.StorageQuestion;
import org.example.onlinetestbackend.pojo.UserToQuestion;

import java.util.List;

@Mapper // 这个注解表明这是一个 MyBatis 的 Mapper 接口，用于映射 SQL 语句到方法上
public interface TestMapper {

    /**
     * 检查用户是否有未完成的试卷
     * @param userid 用户 ID
     * @return 包含用户与问题关联的列表
     */
    public List<UserToQuestion> paperCheck(int userid);

    /**
     * 根据问题 ID 列表重新发布问题
     * @param qids 问题 ID 列表
     * @return 包含 StorageQuestion 对象的列表
     */
    public List<StorageQuestion> repostQuestions(List<Integer> qids,int userid);

    /**
     * 根据两个专业选取问题
     * @param major1 专业1
     * @param major2 专业2
     * @return 包含 StorageQuestion 对象的列表
     */
    public List<StorageQuestion> qSelectBy2Major(String major1, String major2);

    /**
     * 为用户创建试卷
     * @param userid 用户 ID
     * @param questions 包含 PostQuestion 对象的列表
     */
    public void testCreate(int userid, List<StorageQuestion> questions);

    /**
     * 保存用户答案
     * @param rid 用户答案记录 ID
     * @param useranswer 用户答案
     */
    public void answerSave(int userid,int qid, String useranswer);

    /**
     * 计算用户的正确答案数量
     * @param userid 用户 ID
     * @return 正确答案的数量
     */
    public int questionCorrect(int userid);

    /**
     * 结束用户的测试
     * @param userid 用户 ID
     */
    public void testOver(int userid);

}