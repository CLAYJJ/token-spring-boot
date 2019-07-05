package com.ict.token.spring.boot.autoconfigure;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Dante
 * @since 1.0.0
 */
public class TokenFiler implements Filter {
    private static final String TOKEN_HEADER = "Token";
    private List<String> excludes;
    public TokenFiler(List<String> excludes) {
        this.excludes = excludes;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setContentType("application/json;charset=utf-8");
        if (excludes != null && excludes.stream().anyMatch(req.getRequestURI()::startsWith)) {
            chain.doFilter(request, response);
            return;
        }
        String token = req.getHeader(TOKEN_HEADER);
        String result = TokenUtils.checkToken(token);

        if (JSONObject.parseObject(result).getString("code").equals(ResponseCode.SUCCESS.getName()))
            chain.doFilter(request, response);
        else {
            response.getWriter().write(result);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
