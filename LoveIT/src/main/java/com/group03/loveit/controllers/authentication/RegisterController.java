/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.group03.loveit.controllers.authentication;

import com.group03.loveit.models.gender.GenderDAO;
import com.group03.loveit.models.gender.GenderDTO;
import com.group03.loveit.models.user.UserDAO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;
import com.group03.loveit.utilities.CryptoUtils;
import com.group03.loveit.utilities.DataProcessingUtils;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Register Controller
 *
 * @author duyvu
 */
public class RegisterController extends HttpServlet {

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
        processRequest(request, response);
    }

    /**
     * Handle POST request
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check Actions
        String action = request.getParameter("action");
        List<GenderDTO> genders = GenderDAO.getInstance().getGenderList();
        request.setAttribute("genders", genders);
        
        String genderId = request.getParameter("picked_gender");
        String preferenceId = request.getParameter("picked_preference");
        
        
        GenderDTO gender = GenderDAO.getInstance().getGenderMap().get(Long.parseLong(genderId));
        GenderDTO preferenceGender = GenderDAO.getInstance().getGenderMap().get(Long.parseLong(preferenceId));
        
        request.setAttribute("picked_gender", gender);
        request.setAttribute("picked_preference", preferenceGender);
        

        // Check actions then go to page
        if (action == null) {
            request.getRequestDispatcher("/views/authentication/register.jsp").forward(request, response);
        } else if ("goToLogin".equals(action)) {
            response.sendRedirect("login");
        } else if ("check".equals(action)) {
            handleRegister(request, response);
        }
    }

    /**
     * Handling the register by checking each field
     *
     * @param request
     * @param response
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String fullName = request.getParameter("fullName");
        String nickName = request.getParameter("nickName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String genderId = request.getParameter("gender");
        String preferenceGenderId = request.getParameter("preferenceGender");
        String retypePassword = request.getParameter("retypePassword");
        String dob = request.getParameter("dob"); // yyyy-MM-dd

        // Hash Map contains error
        Map<String, String> errorMessages = new HashMap<>();

        // Prechecking at each <input> since they are dynamic input
        // Error text can change later
        if (!DataProcessingUtils.isEmailValid(email, 8, 32)) {
            errorMessages.put("errorEmail", "Please enter email in the right format and from length of 8 to 32");
        }

        if (!DataProcessingUtils.isFullNameValid(fullName, 8, 32)) {
            errorMessages.put("errorFullName", "Please enter full name in the right format and from length of 8 to 32");
        }

        if (!DataProcessingUtils.isNickNameValid(fullName, 8, 32)) {
            errorMessages.put("errorNickName", "Please enter nick name in the right format and from length of 8 to 32");
        }

        if (!DataProcessingUtils.isPasswordValid(password)) {
            errorMessages.put("errorPassword", "Please enter password in the right format");
        }

        if (!DataProcessingUtils.isRetypedPasswordValid(password, retypePassword)) {
            errorMessages.put("errorRetypePassword", "Please enter retype password in the right format");
        }

        LocalDate date = DataProcessingUtils.parseLocalDate(dob, "yyyy-MM-dd");
        if (!DataProcessingUtils.isDateOfBirthValid(date)) {
            errorMessages.put("errorDob", "You must be 18 years old to register an account");
        }

        // If having error, redirect to the page immediately
        if (!errorMessages.isEmpty()) {
            errorMessages.forEach((errName, errMsg) -> {
                request.setAttribute(errName, errMsg);
            });
            request.getRequestDispatcher("/views/authentication/register.jsp").forward(request, response);
        } else {

            // After registation, move to login
            // Register only User account
            GenderDTO gender = GenderDAO.getInstance().getGenderMap().get(Long.parseLong(genderId));
            GenderDTO preferenceGender = GenderDAO.getInstance().getGenderMap().get(Long.parseLong(preferenceGenderId));
            int age = DataProcessingUtils.convertDateToAge(date);

            UserDAO userDAO = new UserDAO();
            UserDTO user = new UserDTO(email, CryptoUtils.encode(password), fullName, nickName, age, gender, preferenceGender);
            userDAO.insertUser(user);

            request.setAttribute("success", "You register the account successfully.");
        }

        request.getRequestDispatcher("/views/authentication/login.jsp").forward(request, response);
    }
}
