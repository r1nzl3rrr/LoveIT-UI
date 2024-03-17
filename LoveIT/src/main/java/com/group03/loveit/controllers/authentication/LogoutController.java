/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group03.loveit.controllers.authentication;

import com.group03.loveit.utilities.ConstantUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author duyvu
 */
public class LogoutController extends HttpServlet {

    /**
     * Handle GET request
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user in session, invalidate and sendRedirect to login page
        HttpSession session = request.getSession(false);
        if (session.getAttribute(ConstantUtils.SESSION_USER) != null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath().concat("/login"));
        }
    }
}
