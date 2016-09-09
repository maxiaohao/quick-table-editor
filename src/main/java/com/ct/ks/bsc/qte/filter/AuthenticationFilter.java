package com.ct.ks.bsc.qte.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
public class AuthenticationFilter implements Filter {

    /**
     * Default constructor.
     */
    public AuthenticationFilter() {
    }


    @Override
    public void destroy() {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("req url grabbed in filter" + ((HttpServletRequest) request).getRequestURL());
        chain.doFilter(new QteAuthHttpServletRequestWrapper((HttpServletRequest) request, null), response);

//        String loginName = ((HttpServletRequest) request).getUserPrincipal().getName();
//        if (loginName == null) {
//            ((HttpServletResponse) response).sendRedirect("/login.html");
//        } else {
//            chain.doFilter(new QteAuthHttpServletRequestWrapper((HttpServletRequest) request, null), response);
//        }
    }

    final class QteAuthHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final Principal prcpl;


        public QteAuthHttpServletRequestWrapper(final HttpServletRequest request, final Principal principal) {
            super(request);
            this.prcpl = principal;
        }
    }

    final class QteUserPrincipal implements Principal {

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            return null;
        }

    }


    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {

    }

}
