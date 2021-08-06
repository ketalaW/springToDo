package com.toDo.demo.controller;

import com.toDo.demo.TaskConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
class InfoController {

//    @Value("${spring.datasource.url}")
//    private String url;

    private DataSourceProperties dataSource;
    private TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dataSource, TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping(path = "/info/url")
    String url() {
        return dataSource.getUrl();
    }

    @GetMapping(path = "/info/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
