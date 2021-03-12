package com.kcbs.webforum.filter;

import com.alibaba.fastjson.JSONObject;
import com.kcbs.webforum.common.ApiRestResponse;
import com.kcbs.webforum.common.Constant;
import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import com.kcbs.webforum.model.pojo.User;
import com.kcbs.webforum.service.UserService;
import com.kcbs.webforum.utils.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 管理员校验过滤器
 */
public class AdminFilter implements Filter {
    @Resource
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ApiRestResponse res = null;
        try {
        res = JwtUtils.checkToken(request);
        User user = JSONObject.parseObject(res.getData().toString(), User.class);
        if (user==null){
            throw new WebforumException(WebforumExceptionEnum.NEED_LOGIN);
        }
        Boolean adminRole = userService.checkAdminRole(user);
        if (adminRole){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": 10010,\n" +
                    "    \"msg\": \"需要管理员权限\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        } catch (WebforumException e) {
            e.printStackTrace();
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": "+e.getCode()+",\n" +
                    "    \"msg\": \""+e.getMessage()+"\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
