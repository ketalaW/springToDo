package com.toDo.demo.model;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User save(User entity);
}
