package com.ticketing.controller;

import com.ticketing.annotation.DefaultExceptionMessage;
import com.ticketing.dto.TaskDTO;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name="Task Controller", description = "Task API")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrogn please try again")
    @Operation(summary = "Read all tasks")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> readAll(){

        List<TaskDTO> taskDTOS = taskService.listAllTasks();

        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all tasks",taskDTOS));

    }

    @GetMapping("/project-manager")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Read all tasks by project manager")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> readAllProjectsByManager() throws TicketingProjectException {
        List<TaskDTO> taskList = taskService.listAllTasksByProjectManager();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved task by project manager",taskList));
    }

    @GetMapping("/{id}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Read all tasks by project id")
    @PreAuthorize("hasAnyAuthority('Manager','Employee')")
    public ResponseEntity<ResponseWrapper> readTasksById(@PathVariable("id") Long id ) throws TicketingProjectException {
        TaskDTO byId = taskService.findById(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved task by id",byId));
    }



    @PostMapping("/create")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Create new Task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody  TaskDTO taskDTO ) throws TicketingProjectException {
        TaskDTO save = taskService.save(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully created an new task ",save));
    }

    @DeleteMapping("{id}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Delete Task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id ) throws TicketingProjectException {
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully deleted."));
    }

}
