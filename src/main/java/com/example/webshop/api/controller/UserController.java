package com.example.webshop.api.controller;

import com.example.webshop.api.model.User;
import com.example.webshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/webshop/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/save")
    private User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    private User getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @GetMapping("/getAll")
    private Iterable<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(value = "/update/{id}")
    private User updateUser(@RequestBody User user, @PathVariable(name="id") String id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping(value = "/delete/{id}")
    private void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }
}
