/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group03.loveit.models.user;

import com.group03.loveit.models.gender.GenderDAO;
import com.group03.loveit.models.gender.GenderDTO;
import com.group03.loveit.utilities.CryptoUtils;
import com.group03.loveit.utilities.DBUtils;
import com.group03.loveit.utilities.DataProcessingUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duyvu
 */
public final class UserDAO implements IUserDAO {

    // ===========================
    // == Fields
    // ===========================
    private final String TABLE_NAME = "[User]";
    private final String COL_ID = "Id";
    private final String COL_EMAIL = "Email";
    private final String COL_PASSWORD = "Password";
    private final String COL_FULLNAME = "FullName";
    private final String COL_NICKNAME = "Nickname";

    private final String COL_IMAGEURL = "Image_Url";
    private final String COL_STATUS = "Status";
    private final String COL_ROLE = "Role";
    private final String COL_AGE = "Age";
    private final String COL_CREATE_AT = "Created_At";
    private final String COL_GENDER_ID = "Gender_Id";
    private final String COL_GENDER_PREFERENCE_ID = "Preference_Id";

    // ===========================
    // == Methods
    // ===========================
    /**
     * Login using username and password
     *
     * @param email
     * @param password
     * @return an account model
     */
    public UserDTO login(String email, String password) {
        String sql = " SELECT u.Id, u.Email, u.Password, u.Fullname, u.Nickname, u.Role, u.Status, "
                + "		u.Gender_Id, u.Preference_Id, u.Image_Url, u.Age "
                + " FROM [User] as u "
                + " WHERE u.Email = ? ";

        // Fetching data
        try (Connection conn = DBUtils.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {

                        // Checking hash password
                        byte[] storedPassword = rs.getString(COL_PASSWORD).getBytes();
                        boolean isVerified = CryptoUtils.verify(password, storedPassword);

                        // Fetching data if verified
                        if (isVerified) {
                            long id = rs.getLong(COL_ID);
                            String userEmail = rs.getNString(COL_EMAIL);
                            String userPassword = rs.getNString(COL_PASSWORD);
                            String fullName = rs.getNString(COL_FULLNAME);
                            String nickName = rs.getNString(COL_NICKNAME);
                            String imageUrl = rs.getString(COL_IMAGEURL);
                            EStatus status = EStatus.getEnumFromName(rs.getString(COL_STATUS));
                            EAccountRole role = EAccountRole.getEnumFromName(rs.getString(COL_ROLE));
                            byte age = rs.getByte(COL_AGE);
                            GenderDTO gender = GenderDAO.getInstance().getGenderMap().get(rs.getLong(COL_GENDER_ID));
                            GenderDTO preferenceGender = GenderDAO.getInstance().getGenderMap().get(rs.getLong(COL_GENDER_PREFERENCE_ID));

                            return new UserDTO(id, age, userPassword, gender, preferenceGender, nickName, fullName, userEmail, imageUrl, status, role);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    // ===========================
    // == Override Methods
    // ===========================
    /**
     * Using Async Function to retrieve extensive User list
     *
     * @return a future containing the list of users
     */
    @Override
    public CompletableFuture<List<UserDTO>> getUsers() {
        return CompletableFuture.supplyAsync(() -> {

            List<UserDTO> list = new ArrayList<>();

            try (Connection conn = DBUtils.getConnection()) {

                String sql = " SELECT * FROM [User] ";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            UserDTO user = new UserDTO(
                                    rs.getLong(COL_ID),
                                    rs.getInt(COL_AGE),
                                    GenderDAO.getInstance().getGenderMap().get(rs.getLong(COL_GENDER_ID)),
                                    GenderDAO.getInstance().getGenderMap().get(rs.getLong(COL_GENDER_PREFERENCE_ID)),
                                    rs.getNString(COL_NICKNAME),
                                    rs.getNString(COL_FULLNAME),
                                    rs.getString(COL_EMAIL),
                                    rs.getString(COL_IMAGEURL),
                                    EAccountRole.getEnumFromName(rs.getString(COL_ROLE)),
                                    EStatus.getEnumFromName(rs.getString(COL_STATUS)),
                                    rs.getTimestamp(COL_CREATE_AT).toLocalDateTime()
                            );
                            list.add(user);
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Cannot get list of users");
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
            return list;
        });
    }

    /**
     * Get User By Id
     *
     * @param userId
     * @return
     */
    @Override
    public UserDTO getUserById(long userId) {
        String sql
                = " SELECT * "
                + " FROM [User] as u "
                + " WHERE u.Id = ? ";
        try (Connection conn = DBUtils.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long id = rs.getLong(COL_ID);
                        String email = rs.getNString(COL_EMAIL);
                        String fullName = rs.getNString(COL_FULLNAME);
                        String nickName = rs.getNString(COL_NICKNAME);
                        String imageUrl = rs.getString(COL_IMAGEURL);
                        EStatus status = EStatus.getEnumFromName(rs.getString(COL_STATUS));
                        EAccountRole role = EAccountRole.getEnumFromName(rs.getString(COL_ROLE));
                        byte age = rs.getByte(COL_AGE);
                        GenderDTO gender = GenderDAO.getInstance().getGenderMap().get(rs.getLong(COL_GENDER_ID));
                        GenderDTO preferenceGender = GenderDAO.getInstance().getGenderMap().get(rs.getLong(COL_GENDER_PREFERENCE_ID));
                        LocalDateTime createdAt = rs.getTimestamp(COL_CREATE_AT).toLocalDateTime();

                        return new UserDTO(id, age, gender, preferenceGender, nickName, fullName, email, imageUrl, role, status, createdAt);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Cannot get user by id: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean insertUser(UserDTO user) {
        String sql = " INSERT INTO [User](Fullname, Nickname, Email, Password, Age, Status, Role, Gender_Id, Preference_Id, Created_At, Image_Url) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setNString(1, user.getFullName());
                ps.setNString(2, user.getNickName());
                ps.setNString(3, user.getEmail());
                ps.setNString(4, CryptoUtils.encode(user.getPassword()));
                ps.setInt(5, user.getAge());
                ps.setString(6, user.getStatus().getStringFromEnum());
                ps.setString(7, user.getRole().getStringFromEnum());
                ps.setLong(8, user.getGender().getId());
                ps.setLong(9, user.getPreferenceGender().getId());
                ps.setTimestamp(10, Timestamp.valueOf(user.getCreatedAt()));
                ps.setString(11, user.getImageUrl());

                return ps.executeUpdate() != 0;
            }
        } catch (SQLException ex) {
            System.out.println("Cannot insert user : " + ex.getMessage());
        }

        return false;
    }

    @Override
    public boolean updateUser(UserDTO user) {
        String sql
                = " UPDATE [User] "
                + " SET "
                + "	Fullname = ?, "
                + "	Nickname = ?, "
                + "	Email = ?, "
                + "	Password = ?, "
                + "	Age = ?, "
                + "	Gender_Id = ?,"
                + "	Preference_Id = ? "
                + " WHERE Id = ? ";

        try (Connection conn = DBUtils.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setNString(1, user.getFullName());
                stmt.setNString(2, user.getNickName());
                stmt.setNString(3, user.getEmail());
                stmt.setNString(4, CryptoUtils.encode(user.getPassword()));
                stmt.setInt(5, user.getAge());
                stmt.setLong(6, user.getGender().getId());
                stmt.setLong(7, user.getGender().getId());
                stmt.setLong(8, user.getId());

                return stmt.executeUpdate() != 0;
            }

        } catch (SQLException ex) {
            System.out.println("Cannot update user by id: " + ex.getMessage());
        }

        return false;
    }

//    @Override
//    public boolean deleteUserById(long id) {
//        try (Connection conn = DBUtils.getConnection()) {
//            String sql
//                    = " DELETE FROM [User] "
//                    + " WHERE Id = ? ";
//            try (PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setLong(1, id);
//                return ps.executeUpdate() != 0;
//            }
//        } catch (SQLException ex) {
//            System.out.println("Cannot delete comment: " + ex.getMessage());
//        }
//        return false;
//    }

    public boolean flagUser(long id, boolean isActive) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql
                    = " UPDATE [User] "
                    + " SET "
                    + "     Status = ? "
                    + " WHERE Id = ? ";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, isActive ? EStatus.ACTIVE.getStringFromEnum() : EStatus.DISABLE.getStringFromEnum());
                ps.setLong(2, id);
                return ps.executeUpdate() != 0;
            }
        } catch (SQLException ex) {
            System.out.println("Cannot flag user: " + ex.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
//        UserDTO user = userDAO.login("duy@gmail.com", "duy123");
//
//        System.out.println(user.getId());
        UserDTO user = userDAO.getUserById(3);
        System.out.println(user.getCreatedAt());
//
//        user.setPassword("vu kim duy");
//        System.out.println(userDAO.updateUser(user));

        List<UserDTO> lists = userDAO.getUsers().join();

    }
}
