package com.ticketing.service;

import com.ticketing.dto.ProjectDTO;
import com.ticketing.entity.Project;

import java.util.List;

public interface ProjectServise {

    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();
    Project save(ProjectDTO dto);
    ProjectDTO update(ProjectDTO dto);
    void delete(String code);

}
