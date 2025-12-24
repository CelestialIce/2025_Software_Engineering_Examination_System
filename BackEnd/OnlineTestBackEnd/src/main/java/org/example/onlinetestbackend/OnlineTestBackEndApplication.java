package org.example.onlinetestbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan(value = "org.example.onlinetestbackend.Mapper")
@ServletComponentScan
@SpringBootApplication
public class OnlineTestBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineTestBackEndApplication.class, args);
    }

}
