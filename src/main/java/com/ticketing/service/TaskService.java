package com.ticketing.service;


import com.ticketing.dto.ProjectDTO;
import com.ticketing.dto.TaskDTO;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.User;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.implementation.TaskServiceImpl;

import java.util.List;


public interface TaskService {

    TaskDTO findById(Long id);

    List<TaskDTO> listAllTasks();

    TaskDTO save(TaskDTO dto);

    TaskDTO update(TaskDTO dto);

    void delete(long id);

    int totalNonCompletedTasks(String projectCode);

    int totalCompletedTasks(String projectCode);

    void deleteByProject(ProjectDTO project);

    List<TaskDTO> listAllByProject(ProjectDTO projectDTO);
    List<TaskDTO> listAllTasksByStatusIsNot(ProjectDTO projectDTO);
    List<TaskDTO> listAllTasksByProjectManager(ProjectDTO projectDTO);

    TaskDTO updateStatus(TaskDTO dto);

    List<TaskDTO> readAllByEmployee(User assignedEmployee);








}
