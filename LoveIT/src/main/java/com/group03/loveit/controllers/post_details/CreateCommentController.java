package com.group03.loveit.controllers.post_details;

import com.group03.loveit.models.comment.CommentDAO;
import com.group03.loveit.models.comment.CommentDTO;
import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 *
 * @author Nhat
 */
public class CreateCommentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "create_comment":
                        handleCreateComment(request, response);
                        break;
                    case "create_reply":
                        handleCreateReply(request, response);
                        break;
                }
            }
        } catch (NumberFormatException e) {
            log("Error parsing post id: " + e.getMessage());
        } catch (Exception e) {
            log("Unexpected error: " + e.getMessage());
        }
    }

    private void handleCreateComment(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException {
        long postId = Long.parseLong(request.getParameter("post_id"));
        String content = request.getParameter("content");

        PostDAO postDAO = new PostDAO();
        PostDTO post = postDAO.getPostById(postId).join();

        UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CommentDAO commentDAO = new CommentDAO();

        CommentDTO comment = new CommentDTO(post, user, content, LocalDateTime.now(), "Active", null);
        commentDAO.insertComment(comment).join();

        response.sendRedirect(request.getContextPath() + "/post-details?post_id=" + postId);
    }

    private void handleCreateReply(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException {
        long postId = Long.parseLong(request.getParameter("post_id"));
        String content = request.getParameter("reply_content");

        long parentCmtId = Long.parseLong(request.getParameter("parent_cmt_id"));
        UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/people-zone");
            return;
        }

        CommentDAO commentDAO = new CommentDAO();
        CommentDTO parentComment = commentDAO.getCommentById(parentCmtId).join();

        CommentDTO reply = new CommentDTO(parentComment.getPost(), user, content, LocalDateTime.now(), "Active", parentComment);
        commentDAO.insertComment(reply).join();

        response.sendRedirect(request.getContextPath() + "/post-details?post_id=" + postId);
    }
}
