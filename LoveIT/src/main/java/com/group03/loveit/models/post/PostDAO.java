package com.group03.loveit.models.post;

import com.group03.loveit.models.user.EStatus;
import com.group03.loveit.models.user.UserDAO;
import com.group03.loveit.models.user.UserDTO;
import com.group03.loveit.utilities.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides the Data Access Object (DAO) for the Post entity. It
 * provides methods to interact with the database such as retrieving, inserting,
 * updating, and deleting posts.
 *
 * @author Nhat
 */
public class PostDAO implements IPostDAO {

    // ===========================
    // == Fields
    // ===========================
    private static final String COL_ID = "Id";
    private static final String COL_USER_ID = "User_Id";
    private static final String COL_CONTENT = "Content";
    private static final String COL_CREATED_AT = "Created_At";
    private static final String COL_HEARTS_TOTAL = "Hearts_Total";
    private static final String COL_COMMENT_TOTAL = "Comment_Total";
    private static final String COL_STATUS = "Status";
    private static final String COL_IMAGE_URL = "Image_Url";

    // ===========================
    // == Override Methods
    // ===========================
    /**
     * Retrieves a post by its ID.
     *
     * @param id The ID of the post to retrieve.
     * @return The CompletableFuture that returns the PostDTO object, or null if
     * the post is not found.
     */
    @Override
    public CompletableFuture<PostDTO> getPostById(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new RuntimeException("Connection is null");
                }
                String query = "SELECT * FROM Post WHERE " + COL_ID + " = ?";

                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            long userId = rs.getLong(COL_USER_ID);
                            UserDAO userDAO = new UserDAO();
                            UserDTO user = userDAO.getUserById(userId);

                            return new PostDTO(
                                    rs.getLong(COL_ID),
                                    user,
                                    rs.getString(COL_CONTENT),
                                    rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
                                    rs.getInt(COL_HEARTS_TOTAL),
                                    rs.getInt(COL_COMMENT_TOTAL),
                                    rs.getString(COL_STATUS),
                                    rs.getString(COL_IMAGE_URL)
                            );
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Cannot get post by ID: " + e.getMessage());
            }
            return null;
        });
    }

    /**
     * Retrieves all posts from the database.
     *
     * @return A CompletableFuture that completes with a List of PostDTO objects
     * representing all posts in the database. Each PostDTO object contains the
     * post details and the associated user details. If there are no posts in
     * the database, the CompletableFuture completes with an empty list. If an
     * error occurs while retrieving the posts, the CompletableFuture completes
     * exceptionally with the SQLException.
     */
    @Override
    public CompletableFuture<List<PostDTO>> getAllPosts() {
        return CompletableFuture.supplyAsync(() -> {
            List<PostDTO> posts = new ArrayList<>();
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new RuntimeException("Connection is null");
                }
                String query = "SELECT * FROM Post";

                try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        long userId = rs.getLong(COL_USER_ID);
                        UserDAO userDAO = new UserDAO();
                        UserDTO user = userDAO.getUserById(userId);

                        PostDTO post = new PostDTO(
                                rs.getLong(COL_ID),
                                user,
                                rs.getString(COL_CONTENT),
                                rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
                                rs.getInt(COL_HEARTS_TOTAL),
                                rs.getInt(COL_COMMENT_TOTAL),
                                rs.getString(COL_STATUS),
                                rs.getString(COL_IMAGE_URL)
                        );
                        posts.add(post);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Cannot get posts: " + e.getMessage());
            }
            return posts;
        });
    }

    /**
     * Retrieves all posts from the database where either the post content or
     * the user's full name contains the provided keyword.
     *
     * @param keyword The keyword to search for in the post content and user's
     * full name.
     * @return A CompletableFuture that completes with a List of PostDTO objects
     * representing all posts in the database where either the post content or
     * the user's full name contains the provided keyword. Each PostDTO object
     * contains the post details and the associated user details. If there are
     * no posts in the database that meet the condition, the CompletableFuture
     * completes with an empty list.
     */
    @Override
    public CompletableFuture<List<PostDTO>> getPostsByCondition(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return getAllPosts().thenApply(posts -> {
            List<PostDTO> filteredPosts = new ArrayList<>();
            for (PostDTO post : posts) {
                if (post.getContent().toLowerCase().contains(lowerCaseKeyword)
                        || post.getUser().getFullName().toLowerCase().contains(lowerCaseKeyword)
                        || post.getUser().getNickName().toLowerCase().contains(lowerCaseKeyword)) {
                    filteredPosts.add(post);
                }
            }
            return filteredPosts;
        });
    }

    /**
     * Inserts a new post into the database.
     *
     * @param post The PostDTO object containing the post data to be inserted.
     * @return The CompletableFuture that represents the completion of the
     * insertion.
     */
    @Override
    public CompletableFuture<Void> insertPost(PostDTO post) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Inserting post: " + post.toString());
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Post ("
                        + COL_USER_ID + ", "
                        + COL_CONTENT + ", "
                        + COL_CREATED_AT + ", "
                        + COL_HEARTS_TOTAL + ", "
                        + COL_COMMENT_TOTAL + ", "
                        + COL_STATUS + ", "
                        + COL_IMAGE_URL
                        + ") VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                    ps.setLong(1, post.getUser().getId());
                    ps.setString(2, post.getContent());
                    ps.setTimestamp(3, java.sql.Timestamp.valueOf(post.getCreatedAt()));
                    ps.setInt(4, post.getHeartTotal());
                    ps.setInt(5, post.getCommentTotal());
                    ps.setString(6, post.getStatus());
                    ps.setString(7, post.getImageUrl());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot insert post: " + ex.getMessage());
            }
            return null;
        });
    }

    /**
     * Updates a post in the database.
     *
     * @param post The PostDTO object containing the updated post data.
     * @return The CompletableFuture that represents the completion of the
     * update.
     */
    @Override
    public CompletableFuture<Void> updatePost(PostDTO post) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("UPDATE Post SET "
                        + COL_USER_ID + " = ?, "
                        + COL_CONTENT + " = ?, "
                        + COL_CREATED_AT + " = ?, "
                        + COL_HEARTS_TOTAL + " = ?, "
                        + COL_COMMENT_TOTAL + " = ?, "
                        + COL_STATUS + " = ?, "
                        + COL_IMAGE_URL + " = ? WHERE " + COL_ID + " = ?")) {
                    ps.setLong(1, post.getUser().getId());
                    ps.setString(2, post.getContent());
                    ps.setTimestamp(3, java.sql.Timestamp.valueOf(post.getCreatedAt()));
                    ps.setInt(4, post.getHeartTotal());
                    ps.setInt(5, post.getCommentTotal());
                    ps.setString(6, post.getStatus());
                    ps.setString(7, post.getImageUrl());
                    ps.setLong(8, post.getId());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot update post: " + ex.getMessage());
            }
            return null;
        });
    }

    /**
     * Flags a post in the database by changing its status.
     * If isActive is true, the status of the post will be set to "ACTIVE".
     * If isActive is false, the status of the post will be set to "DISABLED".
     *
     * @param id The ID of the post to flag.
     * @param isActive A boolean indicating whether the post should be active.
     * @return A CompletableFuture that represents the completion of the flagging operation.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public CompletableFuture<Void> flagPost(long id, boolean isActive) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "UPDATE Post SET " + COL_STATUS + " = ? WHERE " + COL_ID + " = ?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, isActive ? EStatus.ACTIVE.getStringFromEnum() : EStatus.DISABLE.getStringFromEnum());
                    ps.setLong(2, id);
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot flag post: " + ex.getMessage());
            }
        });
    }

    /**
     * Deletes a post from the database.
     *
     * @param id The ID of the post to delete.
     * @return The CompletableFuture that represents the completion of the
     * deletion.
     */
    @Override
    public CompletableFuture<Void> deletePost(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Post WHERE " + COL_ID + " = ?")) {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot delete post: " + ex.getMessage());
            }
            return null;
        });
    }
}