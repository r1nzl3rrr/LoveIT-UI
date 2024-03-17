package com.group03.loveit.controllers.people_zone;

import com.group03.loveit.models.comment.CommentDAO;
import com.group03.loveit.models.comment.CommentDTO;
import com.group03.loveit.models.favourite.FavoriteDAO;
import com.group03.loveit.models.favourite.FavoriteDTO;
import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Nhat
 */
public class PeopleZoneController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        RequestDispatcher dispatcher;
        if (action != null) {
            switch (action) {
                case "post_details":
                    String postId = request.getParameter("post_id");
                    if (postId != null) {
                        dispatcher = request.getRequestDispatcher("/post-details?post_id=" + postId);
                        dispatcher.forward(request, response);
                    }
                    break;
                case "search":
                    String keyword = request.getParameter("keyword");
                    if (keyword != null) {
                        processPostList(request, keyword);
                        dispatcher = request.getRequestDispatcher("/views/people-zone/people-zone.jsp");
                        dispatcher.forward(request, response);
                    }
                    break;
                default:
                    break;
            }
        } else {
            processPostList(request, null);
            dispatcher = request.getRequestDispatcher("/views/people-zone/people-zone.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action != null) {
            try {
                switch (action) {
                    case "create_post":
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/create-post");
                        dispatcher.forward(request, response);
                        break;
                    case "favorite":
                        String postId = request.getParameter("post_id");

                        if (postId != null) {
                            PostDAO postDAO = new PostDAO();
                            PostDTO post = postDAO.getPostById(Long.parseLong(postId)).join();

                            FavoriteDAO favoriteDAO = new FavoriteDAO();
                            UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);
                            List<FavoriteDTO> favourites = favoriteDAO.getFavoritesByUser(user.getId()).join();
                            boolean isFavorite = favourites.stream().anyMatch(fav -> fav.getPost().getId() == post.getId());
                            if (isFavorite) {
                                favoriteDAO.deleteFavorite(post.getId(), user.getId());
                            } else {
                                FavoriteDTO favorite = new FavoriteDTO(post, user, LocalDateTime.now());
                                favoriteDAO.insertFavorite(favorite);
                            }
                            response.sendRedirect(request.getContextPath() + "/people-zone");
                            break;
                        }
                    default:
                        break;
                }
            } catch (NumberFormatException e) {
                log("Invalid post ID: " + e.getMessage());
            } catch (ServletException | IOException e) {
                log("Error forwarding request: " + e.getMessage());
            } catch (Exception e) {
                log("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void processPostList(HttpServletRequest request, String keyword) {
        PostDAO postDAO = new PostDAO();
        CommentDAO commentDAO = new CommentDAO();
        FavoriteDAO favoriteDAO = new FavoriteDAO();
        List<PostDTO> posts = null;
        try {
            // Fetch all posts based on the keyword
            if (keyword != null) {
                posts = postDAO.getPostsByCondition(keyword).join();
            } else {
                posts = postDAO.getAllPosts().get();
            }

            // Fetch the top comment for each post
            for (PostDTO post : posts) {
                CommentDTO topComment = commentDAO.getTopCommentByPost(post.getId()).join();
                post.setTopComment(topComment);
            }

            // Fetch all favorite posts for the current user in a single database call
            UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

            List<FavoriteDTO> favourites = new ArrayList<>();

            if (user != null) {
                favourites = favoriteDAO.getFavoritesByUser(user.getId()).join();
            }

            // Map the favorite posts by their ID for quick lookup
            Map<Long, FavoriteDTO> favouriteMap = favourites.stream().collect(Collectors.toMap(fav -> fav.getPost().getId(), Function.identity()));

            // Iterate over the posts and determine if the post is a favorite by checking the map
            for (PostDTO post : posts) {
                post.setIsFavorite(favouriteMap.containsKey(post.getId()));
            }
        } catch (InterruptedException | ExecutionException e) {
            log("Error getting posts or top comments: " + e.getMessage());
        }
        request.setAttribute("posts", posts);
    }
}
