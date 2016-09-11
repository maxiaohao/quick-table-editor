package com.ct.ks.bsc.qte.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ct.ks.bsc.qte.core.AuthHandler;

/**
 * Authenticate if the current user is allowed to access admin resources, otherwise, return a 401 page
 *
 * @author xma11
 *
 */
public class AdminAuthenticationFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(AdminAuthenticationFilter.class);


    @Override
    public void destroy() {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpServletResponse resp = ((HttpServletResponse) response);
        Principal prcpl = req.getUserPrincipal();
        PrintWriter pw = resp.getWriter();
        if (null == prcpl) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            pw.print("<h1>500 Server Internal Error</h1><p>Refer to the server log for more information.</p>");
            log.error("Failed to get userPrincipal from request, probable cause is lack of HttpServletRequestWrapperFilter in web.xml.");
        } else {
            if (AuthHandler.getInstance().isAdmin(prcpl.getName())) {
                chain.doFilter(request, response);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                pw.print("<h1>401 Unauthorized</h1><p>You are not authorized to access this page.</p>");
            }
        }
    }


    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
