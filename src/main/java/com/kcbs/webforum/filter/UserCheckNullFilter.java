package com.kcbs.webforum.filter;

import com.kcbs.webforum.exception.WebforumException;
import com.kcbs.webforum.exception.WebforumExceptionEnum;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 验证用户名密码不能为空的过滤器
 */
public class UserCheckNullFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (StringUtils.isEmpty(username)) {
                throw new WebforumException(WebforumExceptionEnum.NEED_USER_NAME);
            }
            if (StringUtils.isEmpty(password)) {
                throw new WebforumException(WebforumExceptionEnum.NEED_PASSWORD);
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (WebforumException e){
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
