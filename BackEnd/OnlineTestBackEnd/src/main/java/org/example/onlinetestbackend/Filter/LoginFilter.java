package org.example.onlinetestbackend.Filter;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.example.onlinetestbackend.Utils.JWTUtils;
import org.example.onlinetestbackend.pojo.Result;

// @WebFilter(urlPatterns = "/*") // 对所有操作都需要登录校验
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        try {
            // 如果请求是登录操作和注册操作，则直接放行
            if (request.getRequestURI().toString().contains("login") || request.getRequestURI().toString().contains("register")|| request.getRequestURI().toString().contains("sendCode")) {
                chain.doFilter(request, resp);
                return;
            }

            // 获取请求头中的 JWT 令牌
            String jwt = request.getHeader("JWT");
            if (jwt != null) {
                // 校验 JWT 令牌，校验成功则放行
                try {
                    JWTUtils.parseToken(jwt);
                    chain.doFilter(request, resp);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    // 如果 JWT 令牌无效，返回错误信息
                    Result result = new Result(0, "wrong jwt");
                    resp.getWriter().write(JSONObject.toJSONString(result));
                    return;
                }
            }

            // 如果没有 JWT 令牌，返回未登录错误信息
            Result result = new Result(0, "didnt login");
            resp.getWriter().write(JSONObject.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}