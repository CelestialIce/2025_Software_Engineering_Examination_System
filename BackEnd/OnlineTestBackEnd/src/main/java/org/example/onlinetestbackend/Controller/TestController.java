package org.example.onlinetestbackend.Controller;

import com.alibaba.fastjson.JSONObject;
import org.example.onlinetestbackend.Service.TestService;
import org.example.onlinetestbackend.pojo.PostQuestion;
import org.example.onlinetestbackend.pojo.Result;
import org.example.onlinetestbackend.pojo.UserToQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    TestService testService;

    /**
     * 检查用户是否有已创建的试卷
     * @return 查询结果，若存在已创建试卷返回1和试卷信息，否则返回0和提示信息
     */
    @RequestMapping("/Test/paperExistCheck")
    public Result paperExistCheck(@RequestBody JSONObject jsonObject) {
        try{
            int userid = jsonObject.getInteger("userid");
            return new Result(testService.paperExistCheck(userid));
        }catch (Exception e){
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 进入考试，生成试卷
     * @return 生成的试卷，成功返回1和试卷信息，失败返回0
     */
    @RequestMapping("/Test/testCreate")
    public Result testCreate(@RequestBody JSONObject jsonObject) {
        try{
            int userid = jsonObject.getInteger("userid");
            String major = jsonObject.getString("major");
            testService.testCreate(userid, major);
            return new Result(1 );
        }catch (Exception e){
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 进入考试，生成试卷
     * @return 生成的试卷，成功返回1和试卷信息，失败返回0
     */
    @RequestMapping("/Test/paperFetch")
    public Result paperFetch(Integer userid) {
        try{
            return new Result(1 ,testService.paperFetch(userid));
        }catch (Exception e){
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 保存用户答案
     * @param userToQuestion 用户答案对象
     * @return 保存结果，成功返回1，失败返回0
     */
    @RequestMapping("/Test/answerSave")
    public Result answerSave(UserToQuestion userToQuestion) {
        try{
            testService.answerSave(userToQuestion);
            return new Result(1);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 提交试卷并获取成绩
     * @param userid 用户ID
     * @return 成绩结果，成功返回1和成绩信息，失败返回0
     */
    @RequestMapping("/Test/paperSubmit")
    public Result paperSubmit(int userid) {
        try{
            return new Result(1, testService.getGrade(userid));
        }catch (Exception e){
            e.printStackTrace();
            return new Result(0);
        }
    }
}