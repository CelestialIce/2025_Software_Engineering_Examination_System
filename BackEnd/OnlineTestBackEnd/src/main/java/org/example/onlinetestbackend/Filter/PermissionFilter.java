package org.example.onlinetestbackend.Filter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.onlinetestbackend.Utils.JWTUtils;
import org.example.onlinetestbackend.pojo.Result;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Integer.parseInt;

// @WebFilter(urlPatterns = {"/User/*", "/Question/*"}) // 对 "/User/*" 和 "/Question/*" 路径下的请求进行过滤
public class PermissionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String url = request.getRequestURL().toString();
        String jwt = request.getHeader("JWT");

        try {
            // 解析 JWT 令牌
            Claims claims = JWTUtils.parseToken(jwt);
            // 检查是否为管理员或者用户更新自己的信息
            if (claims.get("admin", Integer.class) == 1 // 如果是管理员，直接放行
                    || (url.contains("User/userUpdate") && parseInt(request.getParameter("userid")) == claims.get("userid", Integer.class)
                    && claims.get("admin", Integer.class) == 0)  // 用户更新自己的信息也放行（但不能更改管理员权限）
                    ||(url.contains("User/userSelect") && parseInt(request.getParameter("userid")) == claims.get("userid", Integer.class)))
                    //用户查询自己的信息也放行
            {
                chain.doFilter(request, response);
                return;
            }

            // 其他情况返回无权限错误信息
            Result result = new Result(0, "no permission");
            resp.getWriter().write(JSONObject.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常时返回系统错误信息
            Result result = new Result(0, "system wrong");
            resp.getWriter().write(JSONObject.toJSONString(result));
        }
    }
}