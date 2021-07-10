package com.ticketing.controller;


import com.ticketing.annotation.DefaultExceptionMessage;
import com.ticketing.dto.ProjectDTO;
import com.ticketing.entity.ResponseWrapper;
import com.ticketing.enums.Status;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.service.ProjectServise;
import com.ticketing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.ToDoubleBiFunction;


@RestController
@RequestMapping("/project")
@Tag(name="Project Controller", description = "Project API")
public class ProjectController {

    private ProjectServise projectServise;
    private UserService userService;

    public ProjectController(ProjectServise projectServise, UserService userService) {
        this.projectServise = projectServise;
        this.userService = userService;
    }

    @PostMapping("/create")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Project Create")
    @PreAuthorize("hasAnyAuthority('Manager','Admin')")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {

        ProjectDTO projectDTO1 = projectServise.save(projectDTO);

        return ResponseEntity.ok(new ResponseWrapper("Successfully project created",projectDTO1));
    }


    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Read All Project")
    @PreAuthorize("hasAnyAuthority('Manager','Admin')")
    public ResponseEntity<ResponseWrapper> readAll(){
        List<ProjectDTO> listOfProjects = projectServise.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("Projects are retrieved",listOfProjects));
    }


    @GetMapping("/{projectCode}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Read project by project Code")
    @PreAuthorize("hasAnyAuthority('Manager','Admin')")
    public ResponseEntity<ResponseWrapper> readByProjectCode(@RequestParam String projectCode){
       ProjectDTO projectDTO = projectServise.getByProjectCode(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved",projectDTO));
    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Update Project")
    @PreAuthorize("hasAnyAuthority('Manager','Admin')")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
       ProjectDTO updatedProject = projectServise.update(projectDTO);

        return ResponseEntity.ok(new ResponseWrapper("Project is updated",updatedProject));
    }



    @PutMapping("/complete/{projectCode}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Complete Project")
    @PreAuthorize("hasAnyAuthority('Manager','Admin')")
    public ResponseEntity<ResponseWrapper> completeProject(@RequestParam String projectCode) throws TicketingProjectException {

        ProjectDTO projectDTO = projectServise.complete(projectCode);
        projectDTO.setProjectStatus(Status.COMPLETE);

        return ResponseEntity.ok(new ResponseWrapper("Project is completed",projectDTO));
    }

    @DeleteMapping("/{projectCode}")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Delete Project")
    @PreAuthorize("hasAnyAuthority('Manager','Admin')")
    public ResponseEntity<ResponseWrapper> deleteProject(@RequestParam String projectCode) throws TicketingProjectException {

        projectServise.delete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("Project is deleted"));
    }

    @GetMapping("/details")
    @DefaultExceptionMessage(defaultMessage = "Something went wrong try again")
    @Operation(summary = "Project Details")
    @PreAuthorize("hasAnyAuthority('Manager','Admin')")
    public ResponseEntity<ResponseWrapper> readAllProjectDetails()  {

        projectServise.read
        return ResponseEntity.ok(new ResponseWrapper("Project is deleted"));
    }

}
