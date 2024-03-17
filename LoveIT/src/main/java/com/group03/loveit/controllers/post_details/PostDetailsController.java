package com.group03.loveit.controllers.post_details;

import com.group03.loveit.models.comment.CommentDAO;
import com.group03.loveit.models.comment.CommentDTO;
import com.group03.loveit.models.favourite.FavoriteDAO;
import com.group03.loveit.models.favourite.FavoriteDTO;
import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author Nhat
 */
public class PostDetailsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            long post_id = Long.parseLong(request.getParameter("post_id"));
            PostDAO postDAO = new PostDAO();
            PostDTO post = postDAO.getPostById(post_id).join();
            UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }


            long userId = user.getId();

            FavoriteDAO favoriteDAO = new FavoriteDAO();
            FavoriteDTO favorite = favoriteDAO.getFavoriteById(post_id, userId).join();
            System.out.println("Is favorite: " + (favorite != null));
            post.setIsFavorite(favorite != null);

            CommentDAO commentDAO = new CommentDAO();
            List<CommentDTO> comments = commentDAO.getCommentsByPost(post_id).join();

            for (CommentDTO comment : comments) {
                List<CommentDTO> replies = commentDAO.getRepliesByComment(comment.getId()).join();
                comment.setReplies(replies);
            }

            request.setAttribute("post", post);
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("/views/post-details/post-details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            log("Error parsing id: " + e.getMessage());
        } catch (Exception e) {
            log("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String postIdS;
        if (action != null) {
            switch (action) {
                case "create_comment":
                    postIdS = request.getParameter("post_id");
                    String content = request.getParameter("content");

                    if (postIdS != null && content != null) {
                        request.getRequestDispatcher("/create-comment?action=create_comment&post_id = " + postIdS + "&content = " + content).forward(request, response);
                    }
                    break;
                case "create_reply":
                    postIdS = request.getParameter("post_id");
                    String parentCmtId = request.getParameter("parent_cmt_id");
                    String replyContent = request.getParameter("reply_content");

                    if (parentCmtId != null && replyContent != null) {
                        request.getRequestDispatcher("/create-comment?action=create_reply&post_id = " + postIdS + "&parent_cmt_id = " + parentCmtId + "&reply_content = " + replyContent).forward(request, response);
                    }
                    break;
                case "toggle_favorite":
                    long postId = Long.parseLong(request.getParameter("post_id"));

                    UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

                    if (user == null) {
                        response.sendRedirect(request.getContextPath() + "/login");
                        return;
                    }

                    long userId = user.getId();

                    FavoriteDAO favoriteDAO = new FavoriteDAO();
                    FavoriteDTO favorite = favoriteDAO.getFavoriteById(postId, userId).join();

                    if (favorite != null) {
                        System.out.println("Removing favorite post: " + postId + " for user: " + userId);
                        favoriteDAO.deleteFavorite(postId, userId).join();
                    } else {
                        PostDAO postDAO = new PostDAO();
                        PostDTO post = postDAO.getPostById(postId).join();

                        FavoriteDTO newFavorite = new FavoriteDTO(post, user, LocalDateTime.now());
                        favoriteDAO.insertFavorite(newFavorite).join();
                    }

                    response.sendRedirect(request.getContextPath() + "/post-details?post_id=" + postId);

                    break;
                default:
                    break;
            }
        }
    }
}
