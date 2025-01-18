package com.example.webshop.api.controller;

import com.example.webshop.api.model.User;
import com.example.webshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/webshop/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/save")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/getAll")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(value = "/update/{id}")
    public User updateUser(@RequestBody User user, @PathVariable(name = "id") Long id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
