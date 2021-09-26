package com.toDo.demo.controller;

import com.toDo.demo.logic.TaskGroupService;
import com.toDo.demo.model.Task;
import com.toDo.demo.model.TaskRepository;
import com.toDo.demo.model.projection.GroupReadModel;
import com.toDo.demo.model.projection.GroupTaskWriteModel;
import com.toDo.demo.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@IllegalExceptionProcessing
@RequestMapping("/groups")
class TaskGroupController {
    private final TaskGroupService service;
    private final TaskRepository repository;
    public static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<GroupReadModel>> readAllGroup() {
        return ResponseEntity.ok(service.readAll());
    }

    @Transactional
    @PatchMapping(value = "/{id}")
    @ResponseBody
    ResponseEntity<?> toogleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(produces =  MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<GroupReadModel> createGroup(@Valid @RequestBody GroupWriteModel toCreate){
        GroupReadModel group = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + group.getId())).body(group);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
         return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    //Zapytania potrzebne do widoku
    @GetMapping(produces =  MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model) {
        var taskGroupEdit = new GroupWriteModel();
        model.addAttribute("group", taskGroupEdit);
        return "groups";
    }

    @PostMapping(produces =  MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(
            @ModelAttribute("group") @Valid GroupWriteModel current,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }
        service.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę.");
        return "groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return service.readAll();
    }

    @PostMapping(params = "addTask", produces =  MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }


    @GetMapping(value = "provider/{provider}")
    public ResponseEntity<?> getAsciiCode(@PathVariable String provider){
        service.getProviderCodeAsAscii(provider);
        return ResponseEntity.ok("dasdasd");
    }

}
