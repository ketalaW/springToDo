package com.toDo.demo.controller;


import com.toDo.demo.model.User;
import com.toDo.demo.model.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository repository;

    UserController(final UserRepository repository) {
        this.repository = repository;
    }


    @PostMapping("/create")
    ResponseEntity<User> createUser(@Valid @RequestBody User toCreate) {
        User result =  repository.save(toCreate);
        return ResponseEntity.created(URI.create("/user/" + result.getId())).body(result);
    }


}
