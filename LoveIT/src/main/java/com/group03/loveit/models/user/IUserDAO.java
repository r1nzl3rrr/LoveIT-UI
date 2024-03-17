/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group03.loveit.models.user;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author duyvu
 */
public interface IUserDAO {

    public CompletableFuture<List<UserDTO>> getUsers();

    public UserDTO getUserById(long id);

    public boolean insertUser(UserDTO user);

    public boolean updateUser(UserDTO user);

}
