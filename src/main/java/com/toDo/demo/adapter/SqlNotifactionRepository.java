package com.toDo.demo.adapter;

import com.toDo.demo.model.Notification;
import com.toDo.demo.model.NotificationRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SqlNotifactionRepository extends NotificationRepository, JpaRepository<Notification, Integer> {

    @Override
    @Query(nativeQuery = true, value = "select  count(*) > 0 from notifications where id=:id")
    boolean existsById(@Param("id") Integer id);

}
