package com.group03.loveit.models.comment;

import com.group03.loveit.models.post.PostDAO;
import com.group03.loveit.models.post.PostDTO;
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
 * This class provides the Data Access Object (DAO) for the Comment entity. It
 * provides methods to interact with the database such as retrieving, inserting,
 * updating, and deleting comments.
 *
 * @author Nhat
 */
public class CommentDAO implements ICommentDAO {

    // ===========================
    // == Fields
    // ===========================
    private static final String COL_ID = "Id";
    private static final String COL_POST_ID = "Post_Id";
    private static final String COL_USER_ID = "User_Id";
    private static final String COL_CONTENT = "Content";
    private static final String COL_CREATED_AT = "Created_At";
    private static final String COL_STATUS = "Status";
    private static final String COL_REPLY_ID = "Reply_Id";

    // ===========================
    // == Override Methods
    // ===========================
    /**
     * Retrieves a comment by its ID from the database.
     *
     * @param id The ID of the comment to retrieve.
     * @return The CompletableFuture that returns the CommentDTO object, or null
     * if the comment is not found.
     */
    @Override
    public CompletableFuture<CommentDTO> getCommentById(long id) {
        return CompletableFuture.supplyAsync(() -> {
            CommentDTO comment = null;
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT * FROM Comment WHERE " + COL_ID + " = ?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            long userId = rs.getLong(COL_USER_ID);
                            UserDAO userDAO = new UserDAO();
                            UserDTO user = userDAO.getUserById(userId);

                            long postId = rs.getLong(COL_POST_ID);
                            PostDAO postDAO = new PostDAO();
                            PostDTO post = postDAO.getPostById(postId).join();

                            CommentDTO parentCmt = null;
                            if (rs.getLong(COL_REPLY_ID) != 0) {
                                parentCmt = getCommentById(rs.getLong(COL_REPLY_ID)).join();
                            }

                            comment = new CommentDTO(
                                    rs.getLong(COL_ID),
                                    post,
                                    user,
                                    rs.getString(COL_CONTENT),
                                    rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
                                    rs.getString(COL_STATUS),
                                    parentCmt
                            );
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Cannot get comment by id: " + ex.getMessage());
            }
            return comment;
        });
    }

    /**
     * Retrieves all comments associated with a post.
     *
     * @param postId The ID of the post to retrieve comments for.
     * @return The CompletableFuture that returns a list of CommentDTO objects
     * representing the comments, or an empty list if no comments are found.
     */
    @Override
    public CompletableFuture<List<CommentDTO>> getCommentsByPost(long postId) {
        return CompletableFuture.supplyAsync(() -> {
            List<CommentDTO> comments = new ArrayList<>();
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT * FROM Comment WHERE " + COL_POST_ID + " = ? AND " + COL_REPLY_ID + " IS NULL AND " + COL_STATUS + " != 'Disable' ORDER BY " + COL_ID + " DESC";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, postId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            long userId = rs.getLong(COL_USER_ID);
                            UserDAO userDAO = new UserDAO();
                            UserDTO user = userDAO.getUserById(userId);

                            PostDAO postDAO = new PostDAO();
                            PostDTO post = postDAO.getPostById(postId).join();

                            comments.add(new CommentDTO(
                                    rs.getLong(COL_ID),
                                    post,
                                    user,
                                    rs.getString(COL_CONTENT),
                                    rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
                                    rs.getString(COL_STATUS),
                                    null
                            ));
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Cannot get comments by post id: " + e.getMessage());
            }
            return comments;
        });
    }

    /**
     * Retrieves the first comment of a post from the database.
     *
     * @param postId The ID of the post to retrieve the top comment for.
     * @return The CompletableFuture that returns the CommentDTO object
     * representing the top comment, or null if the post has no comments.
     */
    @Override
    public CompletableFuture<CommentDTO> getTopCommentByPost(long postId) {
        return CompletableFuture.supplyAsync(() -> {
            CommentDTO topComment = null;
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT TOP 1 * FROM Comment WHERE " + COL_POST_ID + " = ? AND " + COL_REPLY_ID + " IS NULL AND " + COL_STATUS + " != 'Disable' ORDER BY " + COL_ID + " DESC";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, postId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            long userId = rs.getLong(COL_USER_ID);
                            UserDAO userDAO = new UserDAO();
                            UserDTO user = userDAO.getUserById(userId);

                            PostDAO postDAO = new PostDAO();
                            PostDTO post = postDAO.getPostById(postId).join();

                            topComment = new CommentDTO(
                                    rs.getLong(COL_ID),
                                    post,
                                    user,
                                    rs.getString(COL_CONTENT),
                                    rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
                                    rs.getString(COL_STATUS),
                                    null
                            );
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Cannot get top comment by post id: " + ex.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error while getting top comment: " + e.getMessage());
            }
            return topComment;
        });
    }

    /**
     * Retrieves all replies associated with a comment.
     *
     * @param parentId The ID of the parent comment to retrieve replies for.
     * @return The CompletableFuture that returns a list of CommentDTO objects
     * representing the replies, or an empty list if no replies are found.
     */
    @Override
    public CompletableFuture<List<CommentDTO>> getRepliesByComment(long parentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<CommentDTO> replies = new ArrayList<>();
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT * FROM Comment WHERE " + COL_REPLY_ID + " = ? AND " + COL_STATUS + " != 'Disable' ORDER BY " + COL_USER_ID + " DESC";
                CommentDTO parentCmt = getCommentById(parentId).join();
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setLong(1, parentId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            long userId = rs.getLong(COL_USER_ID);
                            UserDAO userDAO = new UserDAO();
                            UserDTO user = userDAO.getUserById(userId);

                            long postId = rs.getLong(COL_POST_ID);
                            PostDAO postDAO = new PostDAO();
                            PostDTO post = postDAO.getPostById(postId).join();

                            replies.add(new CommentDTO(
                                    rs.getLong(COL_ID),
                                    post,
                                    user,
                                    rs.getString(COL_CONTENT),
                                    rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
                                    rs.getString(COL_STATUS),
                                    parentCmt
                            ));
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Cannot get replies by comment id: " + e.getMessage());
            }
            return replies;
        });
    }

    /**
     * Retrieves all comments from the database.
     *
     * @return The CompletableFuture that returns a list of CommentDTO objects
     * representing all the comments, or an empty list if no comments are found.
     */
    @Override
    public CompletableFuture<List<CommentDTO>> getAllComments() {
        return CompletableFuture.supplyAsync(() -> {
            List<CommentDTO> comments = new ArrayList<>();
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "SELECT * FROM Comment ORDER BY " + COL_USER_ID + " DESC";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            long userId = rs.getLong(COL_USER_ID);
                            UserDAO userDAO = new UserDAO();
                            UserDTO user = userDAO.getUserById(userId);

                            long postId = rs.getLong(COL_POST_ID);
                            PostDAO postDAO = new PostDAO();
                            PostDTO post = postDAO.getPostById(postId).join();

                            CommentDTO parentCmt = null;
                            if (rs.getLong(COL_REPLY_ID) != 0) {
                                parentCmt = getCommentById(rs.getLong(COL_REPLY_ID)).join();
                            }

                            comments.add(new CommentDTO(
                                    rs.getLong(COL_ID),
                                    post,
                                    user,
                                    rs.getString(COL_CONTENT),
                                    rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
                                    rs.getString(COL_STATUS),
                                    parentCmt
                            ));
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Cannot get all comments: " + e.getMessage());
            }
            return comments;
        });
    }

    /**
     * Inserts a new comment into the database.
     *
     * @param comment The CommentDTO object containing the comment data to be
     * inserted.
     * @return The CompletableFuture that represents the completion of the
     * insertion.
     */
    @Override
    public CompletableFuture<Void> insertComment(CommentDTO comment) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Comment (" + COL_POST_ID + ", " + COL_USER_ID + ", " + COL_CONTENT + ", " + COL_CREATED_AT + ", " + COL_STATUS + ", " + COL_REPLY_ID + ") VALUES (?, ?, ?, ?, ?, ?)")) {
                    ps.setLong(1, comment.getPost().getId());
                    ps.setLong(2, comment.getUser().getId());
                    ps.setString(3, comment.getContent());
                    ps.setTimestamp(4, java.sql.Timestamp.valueOf(comment.getCreatedAt()));
                    ps.setString(5, comment.getStatus());
                    if (comment.getParentCmt() == null) {
                        ps.setNull(6, java.sql.Types.BIGINT);
                    } else {
                        ps.setLong(6, comment.getParentCmt().getId());
                    }
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Error inserting comment: " + ex.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        });
    }

    /**
     * Updates a comment in the database.
     *
     * @param comment The CommentDTO object containing the updated comment data.
     * @return The CompletableFuture that represents the completion of the
     * update.
     */
    @Override
    public CompletableFuture<Void> updateComment(CommentDTO comment) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("UPDATE Comment SET " + COL_POST_ID + " = ?, " + COL_USER_ID + " = ?, " + COL_CONTENT + " = ?, " + COL_CREATED_AT + " = ?, " + COL_STATUS + " = ?, " + COL_REPLY_ID + " = ? WHERE " + COL_ID + " = ?")) {
                    ps.setLong(1, comment.getPost().getId());
                    ps.setLong(2, comment.getUser().getId());
                    ps.setString(3, comment.getContent());
                    ps.setTimestamp(4, java.sql.Timestamp.valueOf(comment.getCreatedAt()));
                    ps.setString(5, comment.getStatus());
                    ps.setLong(6, comment.getParentCmt().getId());
                    ps.setLong(7, comment.getId());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot update comment: " + ex.getMessage());
            }
        });
    }

    /**
     * Flags a comment in the database by changing its status. If isActive is
     * true, the status of the comment will be set to "ACTIVE". If isActive is
     * false, the status of the comment will be set to "DISABLED".
     *
     * @param id The ID of the comment to flag.
     * @param isActive A boolean indicating whether the comment should be
     * active.
     * @return A CompletableFuture that represents the completion of the
     * flagging operation.
     */
    @Override
    public CompletableFuture<Void> flagComment(long id, boolean isActive) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                String query = "UPDATE Comment SET " + COL_STATUS + " = ? WHERE " + COL_ID + " = ?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, isActive ? EStatus.ACTIVE.getStringFromEnum() : EStatus.DISABLE.getStringFromEnum());
                    ps.setLong(2, id);
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot flag comment: " + ex.getMessage());
            }
        });
    }

    /**
     * Deletes a comment from the database.
     *
     * @param id The ID of the comment to delete.
     * @return The CompletableFuture that represents the completion of the
     * deletion.
     */
    @Override
    public CompletableFuture<Void> deleteComment(long id) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = DBUtils.getConnection()) {
                if (conn == null) {
                    throw new SQLException();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Comment WHERE " + COL_ID + " = ?")) {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println("Cannot delete comment: " + ex.getMessage());
            }
        });
    }
    
        /**
     * Count total of comments
     *
     * @return
     */
    public long getCommentsCount() {

        try (Connection conn = DBUtils.getConnection()) {
            String sql
                    = " SELECT count(*) as comment_count "
                    + " FROM Comment ";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("comment_count");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Cannot get comments: " + e.getMessage());
        }

        return 0L;
    }
}
