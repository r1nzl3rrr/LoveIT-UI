/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group03.loveit.models.user;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Define 2 objects only of roles
 *
 *
 *
 * @author duyvu
 */
public enum EStatus {
    // ===========================
    // == No. objects
    // ===========================
    /**
     * - Active: the current active account
     *
     * - Disable: disabled account will be blocked to login from the system
     */
    ACTIVE("Active"), DISABLE("Disable");

    // ===========================
    // == Fields
    // ===========================
    private final String status;

    // ===========================
    // == Constructor
    // ===========================
    /**
     * By default the enum constructor is private
     *
     * @param status: shorten form of enum object
     */
    private EStatus(String status) {
        this.status = status;
    }

    // ===========================
    // == Methods
    // ===========================
    /**
     * Return the shorten form of status
     *
     * @return string status
     */
    public String getStringFromEnum() {
        return status;
    }

    /**
     * Get the enum object based on the string form
     *
     * @param name string form of enum object
     * @return enum object
     */
    public static EStatus getEnumFromName(String name) {
        return Arrays.stream(EStatus.values()).filter(t -> t.status.equalsIgnoreCase(name)).findFirst().get();
    }
}
