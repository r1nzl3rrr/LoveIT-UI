/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group03.loveit.models.gender;

import com.group03.loveit.utilities.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duyvu
 */
public final class GenderDAO {

    // ===========================
    // == Fields
    // ===========================
    private final String TABLE_NAME = "Gender";
    private static Map<Long, GenderDTO> genderMap;

    // ===========================
    // == Constructor
    // ===========================
    /**
     * Singleton
     *
     * - Due to high usage of genders, load it 1 time only
     *
     * - Lazy Loading (Being loaded when in usage)
     */
    private static GenderDAO INSTANCE = null;

    private GenderDAO() {
        genderMap = new HashMap<>();
        listGendersAsync().join();
    }

    public static synchronized GenderDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GenderDAO();
        }
        return INSTANCE;
    }
    // ===========================
    // == Methods
    // ===========================

    /**
     * List all genders from database
     *
     * @return future of list genders
     */
    private CompletableFuture<Void> listGendersAsync() {
        return CompletableFuture.runAsync(() -> {
            String sql = " SELECT * FROM " + TABLE_NAME;

            try (Connection conn = DBUtils.getConnection()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            GenderDTO gender = new GenderDTO(rs.getLong(1), rs.getNString(2));
                            genderMap.put(rs.getLong(1), gender);
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenderDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Return the unmodified HashMap to avoid outside modification
     *
     * - entry: id - GenderDTO
     *
     * @return unmodified HashMap
     */
    public final Map<Long, GenderDTO> getGenderMap() {
        return Collections.unmodifiableMap(genderMap);
    }

    /**
     * Return the unmodified
     *
     * @return
     */
    public final ArrayList<GenderDTO> getGenderList() {
        return new ArrayList<>(genderMap.values());
    }
}
