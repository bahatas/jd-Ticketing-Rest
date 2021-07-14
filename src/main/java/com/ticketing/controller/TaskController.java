package com.ticketing.controller;

import com.ticketing.dto.TaskDTO;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name="Task Controller", description = "Task API")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping

    public ResponseEntity<ResponseWrapper> readAll(){
        List<TaskDTO> taskDTOS = taskService.listAllTasks();

        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all tasks",taskDTOS));


    }
}
