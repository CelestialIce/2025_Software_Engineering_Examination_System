package org.example.onlinetestbackend.Service;

import org.example.onlinetestbackend.Mapper.QuestionMapper;
import org.example.onlinetestbackend.pojo.StorageQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    // 定义存储图片的路径
    final String path = "/root/MySQLServer";

    /**
     * 插入问题
     * @param storageQuestion 问题的存储对象
     * @param file 问题的图片文件（可选）
     */
    public void insertQuestion(StorageQuestion storageQuestion, MultipartFile file) {
        // 如果存在图片数据则生成UUID并存入本地存储空间，将存储地址存入数据库
        if (file != null) {
            String id = UUID.randomUUID().toString();
            try {
                file.transferTo(new File(path + id + ".jpg"));
                storageQuestion.setQimg(path + id + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        questionMapper.insertQuestion(storageQuestion);
    }

    /**
     * 删除问题
     * @param ids 要删除的问题ID数组
     */
    public void deleteQuestion(int[] ids) {
        for (int id : ids) {
            // 删除对应的图片文件
            StorageQuestion storageQuestion = new StorageQuestion(id, null, null, null, null, null);
            File file = new File(questionMapper.selectQuestion(storageQuestion).get(0).getQimg());
            file.delete();
        }
        questionMapper.deleteQuestion(ids);
    }

    /**
     * 查询问题
     * @param storageQuestion 问题的存储对象，用于过滤查询条件
     * @return 查询到的问题列表
     */
    public List<StorageQuestion> selectQuestion(StorageQuestion storageQuestion) {
        return questionMapper.selectQuestion(storageQuestion);
    }

    /**
     * 更新问题
     * @param storageQuestion 问题的存储对象
     * @param file 问题的图片文件（可选）
     */
    public void updateQuestion(StorageQuestion storageQuestion, MultipartFile file) {
        // 如果存在图片数据则生成UUID并存入本地存储空间，将存储地址存入数据库
        if (file != null) {
            String id = UUID.randomUUID().toString();
            try {
                file.transferTo(new File(path + id + ".jpg"));
                storageQuestion.setQimg(path + id + ".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        questionMapper.updateQuestion(storageQuestion);
    }
}