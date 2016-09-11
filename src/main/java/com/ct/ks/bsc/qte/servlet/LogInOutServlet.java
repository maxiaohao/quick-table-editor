package com.ct.ks.bsc.qte.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ct.ks.bsc.qte.core.AuthHandler;
import com.ct.ks.bsc.qte.core.Constants;
import com.ct.ks.bsc.qte.util.CrudResult;
import com.ct.ks.bsc.qte.util.JsonUtils;

public class LogInOutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json");
        String servletPath = request.getServletPath();
        String loginName = request.getParameter("login_name");
        if ("/login".equals(servletPath)) {
            String pwdMd5 = request.getParameter("pwd_md5");
            boolean authenticated = AuthHandler.getInstance().authenticateUser(loginName, pwdMd5);
            if (authenticated) {
                request.getSession().setAttribute(Constants.SESSION_ATTR_AUTHENTICATED_LOGIN_NAME, loginName);
            }
            JsonUtils.writeAsJson(response.getWriter(), new CrudResult(authenticated));
        } else if ("/logout".equals(servletPath)) {
            // logout
            request.getSession().setAttribute(Constants.SESSION_ATTR_AUTHENTICATED_LOGIN_NAME, null);
            JsonUtils.writeAsJson(response.getWriter(), new CrudResult(true));
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}
