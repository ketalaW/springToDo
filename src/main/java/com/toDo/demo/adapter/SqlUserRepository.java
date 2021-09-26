package com.toDo.demo.adapter;

import com.toDo.demo.model.User;
import com.toDo.demo.model.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SqlUserRepository extends UserRepository, JpaRepository<User, Integer> {

}
