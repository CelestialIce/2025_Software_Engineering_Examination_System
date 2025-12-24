package org.example.onlinetestbackend;

import org.example.onlinetestbackend.Controller.TestController;
import org.example.onlinetestbackend.Mapper.QuestionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class OnlineTestBackEndApplicationTests {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    private TestController testController;

    @Test
    void contextLoads() {
        System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
    }

}
