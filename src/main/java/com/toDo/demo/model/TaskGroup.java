package com.toDo.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@Column(name = "description")
    @NotBlank(message = "Opis grupy nie może, byc pusty")
    private String description;
    private boolean done;

//    @Embedded
//    private Audit audit = new Audit();

    //cascade = CascadeType.ALL - operacje bedą przeprowadzane na wszystkich taskach
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


    public TaskGroup() {
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
