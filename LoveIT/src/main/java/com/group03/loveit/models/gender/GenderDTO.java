/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group03.loveit.models.gender;

import java.io.Serializable;

/**
 *
 * @author duyvu
 */
public class GenderDTO implements Serializable {

    // ===========================
    // == Fields
    // ===========================
    private long id;
    private String name;

    // ===========================
    // == Constructor
    // ===========================
    public GenderDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * For comparing based on id
     * @param id 
     */
    public GenderDTO(long id) {
        this.id = id;
    }

    // ===========================
    // == Methods
    // ===========================
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    /**
     * For comparing 2 gender
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GenderDTO other = (GenderDTO) obj;
        return this.id == other.id;
    }

    // ===========================
    // == Getters & Setters
    // ===========================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
