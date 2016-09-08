package com.ct.ks.bsc.qte.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        // TODO Auto-generated constructor stub
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String model = request.getParameter("model");
        String action = request.getParameter("action");
        if ("user".equalsIgnoreCase(model)) {
            String userId = request.getParameter("user_id");
            if (action.equalsIgnoreCase("create")) {

            } else if (action.equalsIgnoreCase("read")) {
                if (null == userId) {
                    // read all
                } else {
                    // read one
                }
            } else if (action.equalsIgnoreCase("update")) {

            } else if (action.equalsIgnoreCase("delete")) {

            }

        } else if ("QteDataSource".equalsIgnoreCase(model)) {

        } else if ("table".equalsIgnoreCase(model)) {

        }
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
