package com.group03.loveit.controllers.favorites;

import com.group03.loveit.controllers.post_details.CreateCommentController;
import com.group03.loveit.models.favourite.FavoriteDAO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveFavoriteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            long postId = Long.parseLong(request.getParameter("post_id"));
            UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            long userId = user.getId();

            FavoriteDAO favoriteDAO = new FavoriteDAO();
            favoriteDAO.deleteFavorite(postId, userId).join();

            response.sendRedirect(request.getContextPath() + "/favorites");
        } catch (NumberFormatException e) {
            log("Error parsing post id: " + e.getMessage());
        } catch (Exception e) {
            log("Unexpected error: " + e.getMessage());
        }
    }
}
