/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group03.loveit.models.user;

import com.group03.loveit.models.gender.GenderDTO;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author duyvu
 */
public class UserDTO implements Serializable {

    // ===========================
    // == Fields
    // ===========================
    private long id;
    private String email;
    private String password;
    private String fullName;
    private String nickName;
    private String imageUrl;
    private EStatus status;
    private EAccountRole role;
    private LocalDateTime createdAt;
    private int age;
    private GenderDTO gender;
    private GenderDTO preferenceGender;

    // ===========================
    // == Constructors
    // ===========================
    /**
     * Constructor for JavaBean
     */
    public UserDTO() {
    }

    /**
     * Constructor for Storing Session
     *
     * @param id
     * @param age
     * @param password
     * @param gender
     * @param preferenceGender
     * @param nickName
     * @param fullName
     * @param email
     * @param imageUrl
     * @param status
     * @param role
     */
    public UserDTO(
            long id,
            int age,
            String password,
            GenderDTO gender,
            GenderDTO preferenceGender,
            String nickName,
            String fullName,
            String email,
            String imageUrl,
            EStatus status,
            EAccountRole role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
        this.status = status;
        this.role = role;
        this.age = age;
        this.gender = gender;
        this.preferenceGender = preferenceGender;
        this.nickName = nickName;
    }

    /**
     * Constructor for getting user
     *
     * @param id
     * @param age
     * @param gender
     * @param preferenceGender
     * @param nickName
     * @param fullName
     * @param email
     * @param imageUrl
     * @param status
     * @param role
     * @param createdAt
     */
    public UserDTO(
            long id,
            int age,
            GenderDTO gender,
            GenderDTO preferenceGender,
            String nickName,
            String fullName,
            String email,
            String imageUrl,
            EAccountRole role,
            EStatus status,
            LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
        this.status = status;
        this.role = role;
        this.age = age;
        this.gender = gender;
        this.preferenceGender = preferenceGender;
        this.nickName = nickName;
        this.createdAt = createdAt;
    }

    /**
     * Constructor for register
     *
     * @param age
     * @param gender
     * @param preferenceGender
     * @param nickName
     * @param email
     * @param password
     * @param fullName
     */
    public UserDTO(String email, String password, String fullName, String nickName, int age, GenderDTO gender, GenderDTO preferenceGender) {
        this.email = email;
        this.fullName = fullName;
        this.nickName = nickName;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.preferenceGender = preferenceGender;
        this.status = EStatus.ACTIVE;
        this.role = EAccountRole.USER;
        this.createdAt = LocalDateTime.now();
        this.imageUrl = "https://picsum.photos/200/300"; // Set Default
    }

    // ===========================
    // == Methods
    // ===========================
    /**
     * Checking if the account is admin
     *
     * @return true if it's an admin
     */
    public boolean isAdmin() {
        return this.getRole().equals(EAccountRole.ADMIN);
    }

    /**
     * Checking if the account is user
     *
     * @return false if it's an user
     */
    public boolean isUser() {
        return this.getRole().equals(EAccountRole.USER);
    }

    // ===========================
    // == Getters & Setters
    // ===========================
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public EAccountRole getRole() {
        return role;
    }

    public void setRole(EAccountRole role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public GenderDTO getGender() {
        return gender;
    }

    public void setGender(GenderDTO gender) {
        this.gender = gender;
    }

    public GenderDTO getPreferenceGender() {
        return preferenceGender;
    }

    public void setPreferenceGender(GenderDTO preferenceGender) {
        this.preferenceGender = preferenceGender;
    }
}
