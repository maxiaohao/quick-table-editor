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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ct.ks.bsc.qte.core.Constants;

/**
 * If login name is found in session, this filter will wrap it as the user principle name into the request, otherwise,
 * redirect client to local login page.
 *
 * @author xma11
 *
 */
public class HttpServletRequestWrapperFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        String loginName = (String) session.getAttribute(Constants.SESSION_ATTR_AUTHENTICATED_LOGIN_NAME);
        if (loginName != null) {
            chain.doFilter(new QteAuthHttpServletRequestWrapper((HttpServletRequest) request, new QteUserPrincipal(
                    loginName)), response);
        } else {
            ((HttpServletResponse) response).sendRedirect(request.getServletContext().getContextPath() + "/login.html");
        }
    }

    protected final class QteAuthHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private Principal prcpl = null;


        public QteAuthHttpServletRequestWrapper(final HttpServletRequest request, final Principal principal) {
            super(request);
            this.prcpl = principal;
        }


        @Override
        public Principal getUserPrincipal() {
            return this.prcpl;
        }


        @Override
        public String getRemoteUser() {
            return null == this.prcpl ? null : this.prcpl.getName();
        }
    }

    protected final class QteUserPrincipal implements Principal {

        String nm = null;


        public QteUserPrincipal(String name) {
            this.nm = name;
        }


        @Override
        public String getName() {
            return this.nm;
        }

    }


    @Override
    public void init(FilterConfig fConfig) throws ServletException {

    }

}
