package com.group03.loveit.models.favourite;

import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
import com.group03.loveit.models.user.UserDAO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides the Data Access Object (DAO) for the Favourite entity. It
 * provides methods to interact with the database such as retrieving, inserting,
 * and deleting favourites.
 *
 * @author Nhat
 */
public class FavoriteDAO implements IFavoriteDAO {

    // ===========================
    // == Fields
    // ===========================
    private static final String COL_POST_ID = "Post_Id";
    private static final String COL_USER_ID = "User_Id";
    private static final String COL_CREATED_AT = "Created_At";

    // ===========================
    // == Override Methods
    // ===========================
    /**
     * Retrieves a favourite by its post ID and user ID.
     *
     * @param postId The ID of the post to retrieve.
     * @param userId The ID of the user to retrieve.
     * @return The CompletableFuture that returns the FavouriteDTO object, or
     * null if the favourite is not found.
     */
    @Override
    public CompletableFuture<FavoriteDTO> getFavoriteById(long postId, long userId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT * FROM Favorite WHERE " + COL_POST_ID + " = ? AND " + COL_USER_ID + " = ?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, postId);
                    ps.setLong(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            // Create DAO objects for Post and User
                            PostDAO postDAO = new PostDAO();
                            UserDAO userDAO = new UserDAO();

                            // Fetch the PostDTO and UserDTO objects using their respective DAOs
                            PostDTO post = postDAO.getPostById(postId).join();
                            UserDTO user = userDAO.getUserById(userId);
                            LocalDateTime createdAt = rs.getTimestamp(COL_CREATED_AT).toLocalDateTime();

                            // Return the FavouriteDTO object
                            return new FavoriteDTO(post, user, createdAt);
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Cannot get favorite by id: " + ex.getMessage());
            }
            return null;
        });
    }

    /**
     * Retrieves all favourites by a user.
     *
     * @param userId The ID of the user to retrieve.
     * @return The CompletableFuture that returns a list of FavouriteDTO
     * objects, or empty list if the user has no favourites.
     */
    @Override
    public CompletableFuture<List<FavoriteDTO>> getFavoritesByUser(long userId) {
        return CompletableFuture.supplyAsync(() -> {
            List<FavoriteDTO> favourites = new ArrayList<>();
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT * FROM Favorite WHERE " + COL_USER_ID + " = ? ORDER BY " + COL_USER_ID + " DESC";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        // Create DAO objects for Post and User
                        PostDAO postDAO = new PostDAO();
                        UserDAO userDAO = new UserDAO();

                        while (rs.next()) {
                            // Fetch the PostDTO and UserDTO objects using their respective DAOs
                            long postId = rs.getLong(COL_POST_ID);
                            PostDTO post = postDAO.getPostById(postId).join();
                            UserDTO user = userDAO.getUserById(userId);
                            LocalDateTime createdAt = rs.getTimestamp(COL_CREATED_AT).toLocalDateTime();

                            // Add the FavouriteDTO object to the list
                            favourites.add(new FavoriteDTO(post, user, createdAt));
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Cannot get favorites by user: " + ex.getMessage());
            }
            return favourites;
        });
    }

    /**
     * Retrieves all favourites by a post.
     *
     * @param postId The ID of the post to retrieve.
     * @return The CompletableFuture that returns the list of FavouriteDTO
     * objects, or empty list if no user has favourited the post.
     */
    @Override
    public CompletableFuture<List<FavoriteDTO>> getFavoritesByPost(long postId) {
        return CompletableFuture.supplyAsync(() -> {
            List<FavoriteDTO> favourites = new ArrayList<>();
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT * FROM Favorite WHERE " + COL_POST_ID + " = ?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, postId);
                    try (ResultSet rs = ps.executeQuery()) {
                        // Create DAO objects for Post and User
                        PostDAO postDAO = new PostDAO();
                        UserDAO userDAO = new UserDAO();

                        while (rs.next()) {
                            // Fetch the PostDTO and UserDTO objects using their respective DAOs
                            long userId = rs.getLong(COL_USER_ID);
                            PostDTO post = postDAO.getPostById(postId).join();
                            UserDTO user = userDAO.getUserById(userId);
                            LocalDateTime createdAt = rs.getTimestamp(COL_CREATED_AT).toLocalDateTime();

                            // Add the FavouriteDTO object to the list
                            favourites.add(new FavoriteDTO(post, user, createdAt));
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Cannot get favorites by post: " + ex.getMessage());
            }
            return favourites;
        });
    }

    /**
     * Inserts a new favourite into the database.
     *
     * @param favorite The FavouriteDTO object to insert.
     * @return The CompletableFuture that represents the completion of the
     * insertion.
     */
    @Override
    public CompletableFuture<Void> insertFavorite(FavoriteDTO favorite) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Favorite (" + COL_POST_ID + ", " + COL_USER_ID + ", " + COL_CREATED_AT + ") VALUES (?, ?, ?)")) {
                    ps.setLong(1, favorite.getPost().getId());
                    ps.setLong(2, favorite.getUser().getId());
                    ps.setTimestamp(3, java.sql.Timestamp.valueOf(favorite.getCreatedAt()));
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot insert favourite: " + ex.getMessage());
            }
        });
    }

    /**
     * Deletes a favourite from the database.
     *
     * @param postId The ID of the post to delete.
     * @return The CompletableFuture that represents the completion of the
     * deletion.
     */
    @Override
    public CompletableFuture<Void> deleteFavorite(long postId, long userId) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Favorite WHERE " + COL_POST_ID + " = ? AND " + COL_USER_ID + " = ?")) {
                    ps.setLong(1, postId);
                    ps.setLong(2, userId);
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot delete favourite: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Unexpected error while deleting favourite: " + ex.getMessage());
            }
        });
    }
}
