package com.ticketing.controller;

import com.ticketing.annotation.DefaultExceptionMessage;
import com.ticketing.dto.TaskDTO;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.enums.Status;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "Read task by id")
    @PreAuthorize("hasAnyAuthority('Manager','Employee')")
    public ResponseEntity<ResponseWrapper> readTasksById(@PathVariable("id") Long id ) throws TicketingProjectException {
        TaskDTO byId = taskService.findById(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved task by id",byId));
    }



    @PostMapping("/create")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Create a new task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody  TaskDTO taskDTO ) throws TicketingProjectException {
        TaskDTO save = taskService.save(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully created an new task ",save));
    }

    @DeleteMapping("{id}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Delete task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id ) throws TicketingProjectException {
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully deleted."));
    }

    @PutMapping()
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Update Task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO ) throws TicketingProjectException {
        TaskDTO updatedTask = taskService.update(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully updated.",updatedTask));
    }


    @GetMapping("/employee")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Read all non completed tasks")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<ResponseWrapper> employeereadAllNonCompleteTask() throws TicketingProjectException {
        List<TaskDTO> taskDTOS = taskService.listAllTasksByStatusIsNot(Status.COMPLETE);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all non completed tasks.",taskDTOS));
    }

    @PutMapping("/employee/update")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong please try again")
    @Operation(summary = "Read employee task")
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@RequestBody TaskDTO taskDTO) throws TicketingProjectException {
        TaskDTO taskDTO1 = taskService.updateStatus(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully task status updated.",taskDTO1));
    }
}
