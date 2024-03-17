/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.group03.loveit.controllers.profile;

import com.group03.loveit.models.gender.GenderDAO;
import com.group03.loveit.models.gender.GenderDTO;
import com.group03.loveit.models.user.UserDAO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;
import com.group03.loveit.utilities.CryptoUtils;
import com.group03.loveit.utilities.DataProcessingUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author duyvu
 */
public class UserProfileController extends HttpServlet {

    /**
     * Process Request
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Checking Session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(ConstantUtils.SESSION_USER) == null) {
            response.sendRedirect("login");
            return;
        }

        // Check Actions
        String action = request.getParameter("action");
        List<GenderDTO> genders = GenderDAO.getInstance().getGenderList();
        request.setAttribute("genders", genders);

        // Go to the first page
        if (action == null && session.getAttribute(ConstantUtils.SESSION_USER) != null) {
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("/views/profile/user-profile.jsp").forward(request, response);
        } else if ("edit".equals(action) && session.getAttribute(ConstantUtils.SESSION_USER) != null) {
            UserDTO user = (UserDTO) session.getAttribute(ConstantUtils.SESSION_USER);
            String fullName = request.getParameter("fullName");
            String nickName = request.getParameter("nickName");
            String email = request.getParameter("email");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String retypePassword = request.getParameter("retypePassword");
            String age = request.getParameter("age"); // yyyy-MM-dd
            String genderId = request.getParameter("gender");
            String preferenceGenderId = request.getParameter("preferenceGender");

            // Hash Map contains error
            Map<String, String> errorMessages = new HashMap<>();

            // Check Full Name
            if (!DataProcessingUtils.isFullNameValid(fullName, 8, 32)) {
                errorMessages.put("errorFullName", "Please enter full name in the right format and from length of 8 to 32");
            }

            // Check Nick Name
            if (!DataProcessingUtils.isNickNameValid(nickName, 8, 32)) {
                errorMessages.put("errorNickName", "Please enter nick name in the right format and from length of 8 to 32");
            }

            // Check Email
            if (!DataProcessingUtils.isEmailValid(email, 8, 32)) {
                errorMessages.put("errorEmail", "Please enter email in the right format and from length of 8 to 32");
            }

            // Checking Old Password
            byte[] storedPassword = user.getPassword().getBytes();
            boolean isVerified = CryptoUtils.verify(oldPassword, storedPassword);
            if (!isVerified) {
                errorMessages.put("errorOldPassword", "Failed to verify the old password. Please try again");
            }

            // Checking New Password
            if (!DataProcessingUtils.isPasswordValid(newPassword)) {
                errorMessages.put("errorNewPassword", "Please enter password in the right format");
            }

            // Checking Retype Password
            if (!DataProcessingUtils.isRetypedPasswordValid(newPassword, retypePassword)) {
                errorMessages.put("errorRetypePassword", "Retype password does not match. Please try again.");
            }

            // If having error, redirect to the page immediately
            // else update user in session and user in database
            if (!errorMessages.isEmpty()) {
                errorMessages.forEach((errName, errMsg) -> {
                    request.setAttribute(errName, errMsg);
                });
            } else {
                // Update user from session
                GenderDTO gender = GenderDAO.getInstance().getGenderMap().get(Long.parseLong(genderId));
                GenderDTO preferenceGender = GenderDAO.getInstance().getGenderMap().get(Long.parseLong(preferenceGenderId));
                user.setFullName(fullName);
                user.setNickName(nickName);
                user.setEmail(email);
                user.setPassword(newPassword);
                user.setAge(Byte.parseByte(age));
                user.setGender(gender);
                user.setPreferenceGender(preferenceGender);
                session.setAttribute(ConstantUtils.SESSION_USER, user);

                // Update user from database
                UserDAO userDAO = new UserDAO();
                userDAO.updateUser(user);
            }

            // Redirect to profile page
            request.getRequestDispatcher("/views/profile/user-profile.jsp").forward(request, response);
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
}
