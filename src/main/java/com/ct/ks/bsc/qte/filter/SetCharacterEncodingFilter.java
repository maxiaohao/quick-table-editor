package com.ct.ks.bsc.qte.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SetCharacterEncodingFilter implements Filter {

    protected String encoding = null;


    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.encoding = fConfig.getInitParameter("encoding");
    }


    @Override
    public void destroy() {
        this.encoding = null;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        request.setCharacterEncoding(this.encoding);
        response.setCharacterEncoding(this.encoding);
        chain.doFilter(request, response);
    }

}
