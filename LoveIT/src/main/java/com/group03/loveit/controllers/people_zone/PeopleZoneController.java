package com.group03.loveit.controllers.people_zone;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.group03.loveit.models.comment.CommentDAO;
import com.group03.loveit.models.comment.CommentDTO;
import com.group03.loveit.models.favourite.FavoriteDAO;
import com.group03.loveit.models.favourite.FavoriteDTO;
import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.post.PostFilter;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.ConstantUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This servlet handles requests related to the people zone feature.
 * It supports operations like viewing post details, searching for posts, fetching more posts, creating a post, and favoriting a post.
 *
 * @author Nhat
 */
public class PeopleZoneController extends HttpServlet {
    /**
     * Handles GET requests.
     * Depending on the action parameter in the request, it delegates the handling to the appropriate method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "post_details":
                    handlePostDetailsGet(request, response);
                    break;
                case "search":
                    handleSearchGet(request, response);
                    break;
                default:
                    break;
            }
        } else {
            handleDefaultGet(request, response);
        }
    }

    /**
     * Handles POST requests.
     * Depending on the action parameter in the request, it delegates the handling to the appropriate method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action != null) {
            try {
                switch (action) {
                    case "create_post":
                        handleCreatePostPost(request, response);
                        break;
                    case "favorite":
                        handleFavoritePost(request, response);
                        break;
                    case "fetch":
                        handleFetchGet(request, response);
                        break;
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

    /**
     * Handles the "post_details" action for GET requests.
     * It forwards the request to the post details page.
     */
    private void handlePostDetailsGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postId = request.getParameter("post_id");
        if (postId != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/post-details?post_id=" + postId);
            dispatcher.forward(request, response);
        }
    }

    /**
     * Handles the "search" action for GET requests.
     * It processes the post list with the given keyword and forwards the request to the people zone page.
     */
    private void handleSearchGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword != null) {
            processPostList(request, keyword, 1, null);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/people-zone/people-zone.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Handles the "fetch" action for GET requests.
     * It processes the post list and sends the posts as a JSON response.
     */
    private void handleFetchGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            int page = Integer.parseInt(pageParam);
            String fetchedPostIdsJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            List<Long> fetchedPostIds = new Gson().fromJson(fetchedPostIdsJson, new TypeToken<List<Long>>(){}.getType());
            processPostList(request, null, page, fetchedPostIds);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                        @Override
                        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                            return new JsonPrimitive(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
                        }
                    })
                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                        @Override
                        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                            return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        }
                    })
                    .create();
            String jsonPosts = gson.toJson(request.getAttribute("posts"));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonPosts);
        }
    }

    /**
     * Handles the default action for GET requests.
     * It processes the post list and forwards the request to the people zone page.
     */
    private void handleDefaultGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processPostList(request, null, 1, null);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/people-zone/people-zone.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles the "create_post" action for POST requests.
     * It forwards the request to the create post page.
     */
    private void handleCreatePostPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/create-post");
        dispatcher.forward(request, response);
    }

    /**
     * Handles the "favorite" action for POST requests.
     * It toggles the favorite status of a post for the current user and redirects to the people zone page.
     */
    private void handleFavoritePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String postId = request.getParameter("post_id");

        if (postId != null) {
            PostDAO postDAO = new PostDAO();
            PostDTO post = postDAO.getPostById(Long.parseLong(postId)).join();

            FavoriteDAO favoriteDAO = new FavoriteDAO();
            UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            List<FavoriteDTO> favourites = favoriteDAO.getFavoritesByUser(user.getId()).join();
            boolean isFavorite = favourites.stream().anyMatch(fav -> fav.getPost().getId() == post.getId());
            if (isFavorite) {
                favoriteDAO.deleteFavorite(post.getId(), user.getId());
            } else {
                FavoriteDTO favorite = new FavoriteDTO(post, user, LocalDateTime.now());
                favoriteDAO.insertFavorite(favorite);
            }
            response.sendRedirect(request.getContextPath() + "/people-zone");
        }
    }

    /**
     * This method processes the post list for the people zone page.
     * It fetches the posts from the database, checks if each post is a favorite, and sets the posts as a request attribute.
     */
    private void processPostList(HttpServletRequest request, String keyword, int page, List<Long> fetchedPostIds) {
        int pageSize = 10;
        PostDAO postDAO = new PostDAO();

        CommentDAO commentDAO = new CommentDAO();
        FavoriteDAO favoriteDAO = new FavoriteDAO();
        List<FavoriteDTO> favourites = new ArrayList<>();
        UserDTO user = (UserDTO) request.getSession().getAttribute(ConstantUtils.SESSION_USER);
        PostFilter filter = new PostFilter();

        if (user != null) {
            Long prefGenderId = user.getPreferenceGender().getId();

            filter.setUserId(user.getId());
            filter.setGenderId(user.getGender().getId());
            filter.setPrefGenderId(prefGenderId);

            favourites = favoriteDAO.getFavoritesByUser(user.getId()).join();
        }

        if (keyword != null) {
            filter.setKeyword(keyword);
        }

        if (fetchedPostIds == null) {
            fetchedPostIds = new ArrayList<>();
        }

        List<PostDTO> posts = postDAO.getFilteredPosts(pageSize, page, filter, fetchedPostIds).join();

        // Fetch the top comment for each post
        for (PostDTO post : posts) {
            CommentDTO topComment = commentDAO.getTopCommentByPost(post.getId()).join();
            post.setTopComment(topComment);
        }

        // Map the favorite posts by their ID for quick lookup
        Map<Long, FavoriteDTO> favouriteMap = favourites.stream().collect(Collectors.toMap(fav -> fav.getPost().getId(), Function.identity()));

        // Iterate over the posts and determine if the post is a favorite by checking the map
        for (PostDTO post : posts) {
            post.setIsFavorite(favouriteMap.containsKey(post.getId()));
        }
        request.setAttribute("posts", posts);
    }
}