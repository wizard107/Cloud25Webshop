package com.example.webshop.repo;


import com.example.webshop.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User,String> {

}
