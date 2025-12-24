package org.example.onlinetestbackend.Controller;

import com.alibaba.fastjson.JSONObject;
import org.example.onlinetestbackend.Service.QuestionService;
import org.example.onlinetestbackend.pojo.StorageQuestion;
import org.example.onlinetestbackend.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class QuestionController {
    @Autowired
    QuestionService questionService;

    /**
     * 插入新题目
     * @param storageQuestion 包含题目信息的对象
     * @param file 题目相关的图片文件（可选）
     * @return 插入结果，成功返回1，失败返回0
     */
    @RequestMapping("/Question/qInsert")
    public Result qInsert(StorageQuestion storageQuestion, @RequestParam(name = "img",required = false)MultipartFile file) {
        try{
            questionService.insertQuestion(storageQuestion,file);
            return new Result(1);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 删除题目
     * @param qid 题目ID数组
     * @return 删除结果，成功返回1，失败返回0
     */
    @RequestMapping("/Question/qDelete")
    public Result qDelete(int[] qid) {
        try{
            questionService.deleteQuestion(qid);
            return new Result(1);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 查询题目
     * @param storageQuestion 包含查询条件的对象
     * @return 查询结果，成功返回1和查询结果，失败返回0
     */
    @RequestMapping("/Question/qSelect")
    public Result qSelect(StorageQuestion storageQuestion) {
        try{
            return new Result(1, questionService.selectQuestion(storageQuestion));
        } catch (Exception e){
            e.printStackTrace();
            return new Result(0);
        }
    }

    /**
     * 更新题目信息
     * @param storageQuestion 包含更新后信息的题目对象
     * @param file 更新后题目相关的图片文件（可选）
     * @return 更新结果，成功返回1，失败返回0
     */
    @RequestMapping("/Question/qUpdate")
    public Result qUpdate(@RequestBody JSONObject jsonObject) {
        try{
            StorageQuestion storageQuestion=jsonObject.toJavaObject(StorageQuestion.class);
            questionService.updateQuestion(storageQuestion,null);
            return new Result(1);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(0);
        }
    }
}