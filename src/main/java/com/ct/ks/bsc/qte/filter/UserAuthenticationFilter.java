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

import com.ct.ks.bsc.qte.core.MasterCrudHandler;
import com.ct.ks.bsc.qte.model.User;
import com.ct.ks.bsc.qte.util.CrudResult;

/**
 * Authenticate if the current user is a basic user which is configured in master db, otherwise, return a 401 page.
 *
 * @author xma11
 *
 */
public class UserAuthenticationFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(UserAuthenticationFilter.class);


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
        if (null == prcpl || null == prcpl.getName()) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            pw.print("<h2>500 Server Internal Error</h2><p>Refer to the server log for more information.</p>");
            log.error("Failed to get userPrincipal from request, probable cause is lack of preceding "
                    + "HttpServletRequestWrapperFilter configured in web.xml.");
        } else {
            CrudResult ret = MasterCrudHandler.getInstance().getUser(prcpl.getName());
            if (null != ret && null != ret.getData() && null != ((User) ret.getData())
                    && ((User) ret.getData()).isAdmin()) {
                chain.doFilter(request, response);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                pw.print("<h2>401 Server Internal Error</h2><p>You are not authorized to access this page.</p>");
            }
        }
    }


    @Override
    public void init(FilterConfig fConfig) throws ServletException {

    }

}
