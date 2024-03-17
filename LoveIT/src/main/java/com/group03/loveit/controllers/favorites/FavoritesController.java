package com.group03.loveit.controllers.favorites;

import com.group03.loveit.controllers.post_details.CreateCommentController;
import com.group03.loveit.models.favourite.FavoriteDAO;
import com.group03.loveit.models.favourite.FavoriteDTO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoritesController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "post_details":
                        long postId = Long.parseLong(request.getParameter("post_id"));
                        response.sendRedirect(request.getContextPath() + "/post-details?post_id=" + postId);
                        break;
                    default:
                        break;
                }
            } else {
                FavoriteDAO favoriteDAO = new FavoriteDAO();
                UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

                if (user == null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }

                List<FavoriteDTO> favorites = favoriteDAO.getFavoritesByUser(user.getId()).join();
                List<PostDTO> favoritePosts = new ArrayList<>();
                for (FavoriteDTO favorite : favorites) {
                    favoritePosts.add(favorite.getPost());
                }

                request.setAttribute("favorite_posts", favoritePosts);
                request.getRequestDispatcher("/views/favorites/favorites.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            log("Error parsing id: " + e.getMessage());
        } catch (IOException | ServletException e) {
            log("Error processing request: " + e.getMessage());
        } catch (Exception e) {
            log("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "remove_fav":
                    request.getRequestDispatcher("/remove-favorite").forward(request, response);
                    break;
                default:
                    break;
            }
        }
    }
}
