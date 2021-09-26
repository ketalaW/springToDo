package com.toDo.demo.model;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    List<Notification> findAll();
    Notification save(Notification entity);
    Optional<Notification> findById(Integer id);
    boolean existsById(Integer id);
}
