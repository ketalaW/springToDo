package com.toDo.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toDo.demo.logic.NotificationsService;
import com.toDo.demo.model.*;


import java.awt.image.BufferedImage;
import java.io.*;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    private final NotificationsService service;
    private final NotificationRepository repository;
    private final UserRepository repositoryUser;
    ObjectMapper objectMapper = new ObjectMapper();

    public NotificationsController(NotificationsService service, NotificationRepository repository, UserRepository repositoryUser) {
        this.service = service;
        this.repository = repository;
        this.repositoryUser = repositoryUser;
    }

    @PostMapping("/create")
    ResponseEntity<?>createNotifications(@Valid @RequestBody Notification toCreate){
        Notification result =  repository.save(toCreate);
        return ResponseEntity.created(URI.create("/notification/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<Notification>> readAllTasks() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Notification> readAllTasks(@PathVariable int id) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return repository.findById(id)
                .map(notification -> ResponseEntity.ok(notification))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?>createNotificationsWithImage(@RequestParam(required = false, value = "file") Optional<MultipartFile> file, @RequestParam(required = true, value = "jsondata")String jsondata) throws IOException {
        Notification resultData = objectMapper.readValue(jsondata, Notification.class);
        resultData.setDate(LocalDateTime.now());
        String nameFile = service.generateNameFile(resultData.getTitle());
        if(!file.isPresent()){
            resultData.setImage(null);
            repository.save(resultData);
        }
        else{
            resultData.setImage(nameFile);
            service.savefile( file.get(), nameFile);
            repository.save(resultData);
        }
        return ResponseEntity.created(URI.create("/notification/" + resultData.getId())).body(resultData);
    }


    @PostMapping("/send/{id}")
    ResponseEntity<?>repeatSendNotification(@PathVariable int id){

        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        Stream<String> tokens = repositoryUser.findAll().stream()
                .map(user -> user.getToken())
                .filter(token -> !token.isBlank());

        repository.findById(id).ifPresent(notification ->
                service.sendNotifications(notification.getTitle(), notification.getMessage(), tokens));

        return ResponseEntity.noContent().build();
    }

}
