package org.example.onlinetestbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径生效
                .allowedOrigins(
                        "http://localhost:80",
                        "http://localhost:5173",
                        "http://localhost:8081" // 您当前的前端地址
                )
                .allowedMethods("*") // 允许所有方法
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(true) // 允许发送 Cookie
                .maxAge(3600); // 预检请求的有效期
    }
}
