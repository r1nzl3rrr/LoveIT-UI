/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group03.loveit.controllers.admin;

import com.group03.loveit.models.comment.CommentDAO;
import com.group03.loveit.models.comment.CommentDTO;
import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.EStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author duyvu
 */
public class AdminController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PostDAO postDAO = new PostDAO();
        List<PostDTO> posts = postDAO.getAllPosts().join();

        CommentDAO commentDAO = new CommentDAO();
        List<CommentDTO> comments = commentDAO.getAllComments().join();

        request.setAttribute("posts", posts);
        request.setAttribute("comments", comments);

        request.getRequestDispatcher("/views/admin/admin-posts.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "delete_p":
                    handleDeletePost(request, response);
                    break;
                case "flag_p":
                    handleFlagPost(request, response);
                    break;
                case "delete_cmt":
                    handleDeleteComment(request, response);
                    break;
                case "flag_cmt":
                    handleFlagComment(request, response);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException {
        long id = Long.parseLong(request.getParameter("id"));
        PostDAO postDAO = new PostDAO();
        postDAO.deletePost(id);
        response.sendRedirect(request.getContextPath() + "/admin");
    }

    private void handleFlagPost(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException {
        long id = Long.parseLong(request.getParameter("id"));
        PostDAO postDAO = new PostDAO();
        PostDTO post = postDAO.getPostById(id).join();
        postDAO.flagPost(id, !post.getStatus().equals(EStatus.ACTIVE.getStringFromEnum()));
        response.sendRedirect(request.getContextPath() + "/admin");
    }

    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException {
        long id = Long.parseLong(request.getParameter("id"));
        CommentDAO commentDAO = new CommentDAO();
        commentDAO.deleteComment(id);
        response.sendRedirect(request.getContextPath() + "/admin");
    }

    private void handleFlagComment(HttpServletRequest request, HttpServletResponse response) throws IOException, NumberFormatException {
        long id = Long.parseLong(request.getParameter("id"));
        CommentDAO commentDAO = new CommentDAO();
        CommentDTO comment = commentDAO.getCommentById(id).join();
        commentDAO.flagComment(id, !comment.getStatus().equals(EStatus.ACTIVE.getStringFromEnum()));
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
