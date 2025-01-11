package com.example.webshop.service;

import com.example.webshop.api.model.User;
import com.example.webshop.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public User getUser(String id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

    public Iterable<User> getUsers() {
        return userRepo.findAll();
    }

    public User updateUser(User user, String id) {
        Optional<User> updateUser = userRepo.findById(id);
        if(updateUser.isEmpty())
            return null;
        User getUpdateUser = updateUser.get();
        getUpdateUser.setEmail(user.getEmail());
        getUpdateUser.setName(user.getName());
        return userRepo.save(getUpdateUser);
    }

    public void deleteUser(String id) {
        System.out.println("Attempting to delete user with ID: " + id);
        userRepo.deleteById(id);
    }
}
