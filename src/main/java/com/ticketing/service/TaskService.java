package com.ticketing.service;


import com.ticketing.dto.ProjectDTO;
import com.ticketing.dto.TaskDTO;
import com.ticketing.dto.UserDTO;
import com.ticketing.entity.User;
import com.ticketing.enums.Status;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.implementation.TaskServiceImpl;

import java.util.List;


public interface TaskService {

    TaskDTO findById(Long id) throws TicketingProjectException;

    List<TaskDTO> listAllTasks();

    TaskDTO save(TaskDTO dto);

    TaskDTO update(TaskDTO dto) throws TicketingProjectException;

    void delete(long id) throws TicketingProjectException;

    int totalNonCompletedTasks(String projectCode);

    int totalCompletedTasks(String projectCode);

    void deleteByProject(ProjectDTO project);

    List<TaskDTO> listAllByProject(ProjectDTO projectDTO);
    List<TaskDTO> listAllTasksByStatusIsNot(Status status) throws TicketingProjectException;
    List<TaskDTO> listAllTasksByProjectManager() throws TicketingProjectException;

    TaskDTO updateStatus(TaskDTO dto) throws TicketingProjectException;

    List<TaskDTO> readAllByEmployee(User assignedEmployee);








}
