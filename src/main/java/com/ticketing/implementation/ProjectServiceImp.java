package com.ticketing.implementation;

import com.ticketing.dto.ProjectDTO;
import com.ticketing.entity.Project;
import com.ticketing.entity.User;
import com.ticketing.enums.Status;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.mapper.ProjectMapper;
import com.ticketing.repository.ProjectRepository;
import com.ticketing.repository.UserRepository;
import com.ticketing.service.ProjectServise;

import com.ticketing.utils.MapperUtil;
import org.apache.catalina.mapper.Mapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImp implements ProjectServise
{

    private ProjectMapper projectMapper;
    private ProjectRepository projectRepository;
    private MapperUtil mapperUtil;
    private UserRepository userRepository;

    public ProjectServiceImp(ProjectMapper projectMapper, ProjectRepository projectRepository,
                             MapperUtil mapperUtil, UserRepository userRepository) {
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return null;
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll(Sort.by("projectCode"))
                .stream().map(each->mapperUtil.convert(each, new ProjectDTO())).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO save(ProjectDTO dto) throws TicketingProjectException {

        Project foundProject = projectRepository.findByProjectCode(dto.getProjectCode());
        if (foundProject != null) {
            throw new TicketingProjectException("Project with this code already exist");
        }

        Project obj = mapperUtil.convert(dto, new Project());

        Project createdProject = projectRepository.save(obj);

        return mapperUtil.convert(createdProject, new ProjectDTO());
    }


    @Override
    public ProjectDTO update(ProjectDTO dto) {
        return null;
    }

    @Override
    public void delete(String code) {

    }

    @Override
    public ProjectDTO complete(String projectCode) throws TicketingProjectException {
        Project project = projectRepository.findByProjectCode(projectCode);
        if(project != null){
            throw new TicketingProjectException("Project does not exist");
        }

        assert false;
        project.setProjectStatus(Status.COMPLETE);
        Project completedProject = projectRepository.save(project);

        return mapperUtil.convert(completedProject,new ProjectDTO());

    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() throws TicketingProjectException {

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentId = Long.parseLong(id);

         User user = userRepository.findById(currentId).orElseThrow(()->new TicketingProjectException("This manager doesn't exist"));


        List<Project> allByAssignedManager = projectRepository.findAllByAssignedManager(user);

        if(allByAssignedManager.size()==0){
           throw new TicketingProjectException("This manager does not have any project assigned")
        }

        return allByAssignedManager.stream().map(project -> {
            ProjectDTO obj = mapperUtil.convert(project,new ProjectDTO());
            obj.setUnfinishedTaskCounts();



        })



    }

    @Override
    public List<ProjectDTO> readAllByAssignedManager(User user) {
        return null;
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedProjects() {
        return null;
    }
}
