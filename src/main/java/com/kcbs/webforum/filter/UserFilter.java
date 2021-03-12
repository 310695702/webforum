package com.kcbs.webforum.filter;

import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.service.UserService;
import com.kcbs.webforum.utils.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户token校验过滤器
 */
public class UserFilter implements Filter {
    @Resource
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            JwtUtils.checkToken(request);
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
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
