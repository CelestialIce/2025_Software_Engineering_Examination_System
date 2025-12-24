package org.example.onlinetestbackend.Service;

import org.example.onlinetestbackend.Mapper.TestMapper;
import org.example.onlinetestbackend.pojo.PostQuestion;
import org.example.onlinetestbackend.pojo.StorageQuestion;
import org.example.onlinetestbackend.pojo.UserToQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TestService {
    // 每次测试的题目数量
    final int quantity = 3;

    @Autowired
    TestMapper testMapper;

    /**
     * 检查用户是否有未提交的试卷
     * @param userid 用户ID
     * @return 返回用户未提交的试卷问题列表，如果没有则返回null
     */
    public int paperExistCheck(int userid) {
        List<UserToQuestion> userToQuestions = testMapper.paperCheck(userid);
        if (!userToQuestions.isEmpty()) return 1;
        return 0;
    }

    /**
     * 生成新的试卷
     * @param userid 用户ID
     * @param major 用户选择的专业
     */
    public void testCreate(int userid, String major) {
        List<StorageQuestion> questionsResult = new ArrayList<>();
        String[] qmajor = major.split("/");

        List<StorageQuestion> storageQuestions = testMapper.qSelectBy2Major(qmajor[0], qmajor[1]);  // 若不存在未提交的试卷
        Random random = new Random();  // 对题库进行抽取
        random.setSeed(System.currentTimeMillis());
        Collections.shuffle(storageQuestions, random);
        int count = 0;
        while (count < quantity && count < storageQuestions.size()) {
            questionsResult.add(storageQuestions.get(count++));
        }
        testMapper.testCreate(userid, questionsResult);
    }

    public List<PostQuestion> paperFetch(int userid) {
        List<PostQuestion> questionsResult = new ArrayList<>();
        Comparator<PostQuestion> byqid = Comparator.comparing(PostQuestion::getQid);

        List<UserToQuestion> userToQuestions = testMapper.paperCheck(userid);
        if (!userToQuestions.isEmpty()) {  // 若上次试卷未提交
            List<Integer> qids = new ArrayList<>();
            for (UserToQuestion userToQuestion : userToQuestions) {
                qids.add(userToQuestion.getQid());
            }
            for (StorageQuestion storageQuestion : testMapper.repostQuestions(qids,userid)) {
                questionsResult.add(storageQuestion.postReady());
            }
            questionsResult.sort(byqid);  // 排序后发送
            return questionsResult;
        }
        return null;
    }

    /**
     * 保存用户的答案
     * @param userToQuestion 用户回答的问题对象
     */
    public void answerSave(UserToQuestion userToQuestion) {
        testMapper.answerSave(userToQuestion.getUserid(), userToQuestion.getQid(), userToQuestion.getUseranswer());
    }

    /**
     * 计算用户的分数
     * @param userid 用户ID
     * @return 返回用户的分数
     */
    public float getGrade(int userid) {
        float score = (int) (testMapper.questionCorrect(userid) * 1000.0f / quantity) / 10.0f;  // 按照回答正确的题目数量占比抽取题目总数百分制计算分数
        testMapper.testOver(userid);
        return score;
    }
}