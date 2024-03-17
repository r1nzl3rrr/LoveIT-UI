/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group03.loveit.controllers.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author duyvu
 */
public class AdminController extends HttpServlet {

    // ==========================
    // == Sub Routes
    // ==========================
    private final String CONTROLLER_DASHBOARD = "dashboard";
    private final String CONTROLLER_ACCOUNTS = "accounts";
    private final String CONTROLLER_POSTS = "posts";

    // ==========================
    // == Main Route
    // ==========================
    /**
     * Process the same pattern of doGET and doPOST
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String url = "admin";

        try {
            if (action == null || action.equals("dashboard")) {
                url = url.concat("/").concat(CONTROLLER_DASHBOARD);
            } else if (action.equals("accounts")) {
                url = url.concat("/").concat(CONTROLLER_ACCOUNTS);
            } else if (action.equals("posts")) {
                url = url.concat("/").concat(CONTROLLER_POSTS);
            }

        } catch (Exception ex) {
            log("Error at MainController: " + ex.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    /**
     * Handling doGet method
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handling doPost method
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
