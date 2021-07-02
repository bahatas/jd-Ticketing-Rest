package com.ticketing.controller;


import com.ticketing.dto.ProjectDTO;
import com.ticketing.service.ProjectServise;
import com.ticketing.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/project")
public class ProjectController {
    private ProjectServise projectServise;
    private UserService userService;

    public ProjectController(ProjectServise projectServise, UserService userService) {
        this.projectServise = projectServise;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createProject(Model model) {

        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("projects", projectServise.listAllProjects());
        model.addAttribute("managers", userService.listAllByRole("manager"));
        return "/project/create";
    }

    @PostMapping("/create")
    public String insertProject( ProjectDTO projectDTO) {
        projectServise.save(projectDTO);
        return "redirect:/project/create";
    }

    @GetMapping("delete{projectCode}")
    public String deleteProject(@PathVariable("projectCode") String projectCode){
        projectServise.delete(projectCode);

        return "redirect:/project/create";
    }

}
