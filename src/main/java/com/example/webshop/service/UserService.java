package com.example.webshop.service;

import com.example.webshop.api.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private List<User> userList;

    public UserService() {
        userList = new ArrayList<>();
        User user = new User("asfdf", "me", "test@email.com");
        User user2 = new User("asfdsasf", "me2", "more@email.com");
        userList.addAll(Arrays.asList(user, user2));
    }

    public User getUser(String id) {
        return userList.stream().filter(u -> Objects.equals(u.getId(), id)).findFirst().orElse(null);
    }
}
