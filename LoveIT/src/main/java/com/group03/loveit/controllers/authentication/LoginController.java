/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.group03.loveit.controllers.authentication;

import com.group03.loveit.models.user.UserDAO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Login Controller
 *
 * - not done attached to the entire session
 *
 * @author duyvu
 */
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get action param
        String action = request.getParameter("action");

        if (action == null) { // ====== Redirect to login page 
            request.getRequestDispatcher("/views/authentication/login.jsp").forward(request, response);
        } else if ("check".equals(action)) { // ====== Handle the login
            handleCheckingAuthentication(request, response);
        } else if ("goToRegister".equals(action)) { // ====== Go to Register Page
            response.sendRedirect("register");
        } else if ("forgotPassword".equals(action)) { // ====== Go to Forgot Password
            response.sendRedirect("forgotPassword");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Function to delete cookies
     *
     * -1: delete when exit the chrome
     *
     * 0: remove the cookie
     *
     * @param request
     */
    private void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    private void handleCheckingAuthentication(HttpServletRequest request, HttpServletResponse response) {
        UserDAO accountDAO = new UserDAO();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String isRememberMe = request.getParameter("remember-me");

        try {
            // Checking if having empty input, redirect to login.jsp
            if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                request.setAttribute("error", "Email and Password cannot be empty. Please try again.");
                request.getRequestDispatcher("/views/authentication/login.jsp").forward(request, response);
            } else {
                UserDTO account = accountDAO.login(email, password);

                if (account == null) {
                    request.setAttribute("error", "Email and Password are wrong. Please try again.");
                    request.getRequestDispatcher("/views/authentication/login.jsp").forward(request, response);
                } else {
                    // Checking account status
                    switch (account.getStatus()) {
                        case DISABLE: {
                            request.setAttribute("error", "Your account has been disabled. Please contact the phone 0909189999");
                            request.getRequestDispatcher("/views/authentication/login.jsp").forward(request, response);
                            break;
                        }
                        case ACTIVE: {

                            // Add session to create account session
                            HttpSession session = request.getSession(true);
                            session.setAttribute(ConstantUtils.SESSION_USER, account);
                            session.setMaxInactiveInterval(60 * 60);

                            // Add cookies to remember the email (not password for security reason)
                            if (isRememberMe != null) {
                                Cookie cEmail = new Cookie(ConstantUtils.COOKIE_EMAIL, email);
                                Cookie cRememberMe = new Cookie(ConstantUtils.COOKIE_IS_REMEMBER_ME, isRememberMe);
                                cEmail.setMaxAge(60 * 60 * 24);
                                cRememberMe.setMaxAge(60 * 60 * 24);
                                response.addCookie(cEmail);
                                response.addCookie(cRememberMe);
                            } else {

                                // delete cookie when isRememberMe is disabled
                                deleteCookies(request, response);
                            }

                            // Redirect to page based on role
                            switch (account.getRole()) {
                                case ADMIN: {
                                    response.sendRedirect("admin");
                                    break;
                                }
                                case USER: {
                                    response.sendRedirect("people-zone");
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }

        } catch (IOException | ServletException e) {
            log("Error on login: " + e.getMessage());
        }
    }
}
