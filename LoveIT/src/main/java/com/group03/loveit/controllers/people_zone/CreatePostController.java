package com.group03.loveit.controllers.people_zone;

import com.group03.loveit.controllers.post_details.CreateCommentController;
import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.UserDAO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Nhat
 */
public class CreatePostController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String content = request.getParameter("content");
            String imageUrl = request.getParameter("imageUrl");

            UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            PostDTO post = new PostDTO(user, content, LocalDateTime.now(), 0, 0, "Active", imageUrl);
            PostDAO postDAO = new PostDAO();
            postDAO.insertPost(post).get();

            response.sendRedirect(request.getContextPath() + "/people-zone");
        } catch (InterruptedException | ExecutionException e) {
            log("Error creating post: " + e.getMessage());
        }
    }
}
