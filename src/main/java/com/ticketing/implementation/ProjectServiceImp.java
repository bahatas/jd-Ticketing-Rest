package com.ticketing.implementation;

import com.ticketing.dto.ProjectDTO;
import com.ticketing.entity.Project;
import com.ticketing.mapper.ProjectMapper;
import com.ticketing.repository.ProjectRepository;
import com.ticketing.service.ProjectServise;
import com.ticketing.utils.Status;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImp implements ProjectServise
{

    private ProjectMapper projectMapper;
    private ProjectRepository projectRepository;

    public ProjectServiceImp(ProjectMapper projectMapper, ProjectRepository projectRepository) {
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return null;
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll(Sort.by("projectCode"))
                .stream().map(each->{return projectMapper.conertToDto(each);}).collect(Collectors.toList());
    }

    @Override
    public Project save(ProjectDTO dto) {

        dto.setProjectStatus(Status.OPEN);
        Project obj =projectMapper.conertToEntity(dto);

        Project project = projectRepository.save(obj);


        return project;
    }

    @Override
    public ProjectDTO update(ProjectDTO dto) {
        return null;
    }

    @Override
    public void delete(String code) {

    }
}
