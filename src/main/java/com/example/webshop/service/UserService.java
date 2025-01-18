package com.example.webshop.service;

import com.example.webshop.api.model.User;
import com.example.webshop.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    // Updated to use String as ID
    public User getUser(Long id) {
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null); // Return user or null if not found
    }

    public Iterable<User> getUsers() {
        return userRepo.findAll();
    }


    public User updateUser(User user, Long id) {
        Optional<User> updateUser = userRepo.findById(id);
        if (updateUser.isEmpty())
            return null; // User not found
        User getUpdateUser = updateUser.get();
        getUpdateUser.setEmail(user.getEmail());
        getUpdateUser.setName(user.getName());
        return userRepo.save(getUpdateUser);
    }

    // Updated to use String as ID
    public void deleteUser(Long id) {
        System.out.println("Attempting to delete user with ID: " + id);
        userRepo.deleteById(id);
    }
}
