package com.ct.ks.bsc.qte.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import com.ct.ks.bsc.qte.core.MasterCrudHandler;
import com.ct.ks.bsc.qte.model.User;
import com.ct.ks.bsc.qte.util.CrudResult;
import com.ct.ks.bsc.qte.util.JsonUtils;

/**
 * Servlet implementation class AdminCrudServlet
 */
public class AdminCrudServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminCrudServlet() {
        super();
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json");

        String model = request.getParameter("model");
        String action = request.getParameter("action");
        String data = request.getParameter("json_data");

        // System.out.println("data == [" + request.getCharacterEncoding() + "]" + data);

        CrudResult ret = null;

        if ("user".equalsIgnoreCase(model)) {
            long userId = NumberUtils.toLong(request.getParameter("user_id"));
            User user = JsonUtils.jsonToObject(data, User.class);
            if (action.equalsIgnoreCase("create")) {
                ret = MasterCrudHandler.getInstance().createUser(user);
            } else if (action.equalsIgnoreCase("read")) {
                if (0 == userId) {
                    // no id specified, read all
                    ret = MasterCrudHandler.getInstance().getUsers();
                } else {
                    // read one
                    ret = MasterCrudHandler.getInstance().getUser(userId);
                }
            } else if (action.equalsIgnoreCase("update")) {
                ret = MasterCrudHandler.getInstance().updateUser(userId, user);
            } else if (action.equalsIgnoreCase("delete")) {
                ret = MasterCrudHandler.getInstance().deleteUser(userId);
            }

        } else if ("QteDataSource".equalsIgnoreCase(model)) {

        } else if ("table".equalsIgnoreCase(model)) {

        }

        JsonUtils.writeAsJson(response.getWriter(), ret);

    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}
