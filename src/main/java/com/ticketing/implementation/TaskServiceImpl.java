package com.ticketing.implementation;

import com.ticketing.dto.ProjectDTO;
import com.ticketing.dto.TaskDTO;
import com.ticketing.entity.Task;
import com.ticketing.entity.User;
import com.ticketing.enums.Status;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.repository.TaskRepository;
import com.ticketing.repository.UserRepository;
import com.ticketing.service.TaskService;
import com.ticketing.utils.MapperUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private MapperUtil mapperUtil;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public TaskServiceImpl(MapperUtil mapperUtil, TaskRepository taskRepository, UserRepository userRepository) {
        this.mapperUtil = mapperUtil;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO findById(Long id) throws TicketingProjectException {


        Task task = taskRepository.findById(id).orElseThrow(() -> new TicketingProjectException("No task founded by this id"));

        return mapperUtil.convert(task, new TaskDTO());

    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> listOfTasks = taskRepository.findAll();

        return listOfTasks.stream().map(each -> mapperUtil.convert(each, new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public TaskDTO save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = mapperUtil.convert(dto, new Task());


        Task savedTask = taskRepository.save(task);


        return mapperUtil.convert(savedTask, new TaskDTO());


    }

    @Override
    public TaskDTO update(TaskDTO dto) throws TicketingProjectException {

        taskRepository.findById(dto.getId()).orElseThrow(()-> new TicketingProjectException("Task doesn't exist"));
        //todo
        // Why we did not assigned to any local variable this founded task?
        // only checks request body task exist or not?

        Task convertedTask = mapperUtil.convert(dto,new Task());

        Task save = taskRepository.save(convertedTask);

        return mapperUtil.convert(save,new TaskDTO());
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public int totalNonCompletedTasks(String projectCode) {
        return 0;
    }

    @Override
    public int totalCompletedTasks(String projectCode) {
        return 0;
    }

    @Override
    public void deleteByProject(ProjectDTO project) {

    }

    @Override
    public List<TaskDTO> listAllByProject(ProjectDTO projectDTO) {
        return null;
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(ProjectDTO projectDTO) {
        return null;
    }

    @Override
    public List<TaskDTO> listAllTasksByProjectManager(ProjectDTO projectDTO) {
        return null;
    }

    @Override
    public TaskDTO updateStatus(TaskDTO dto) {
        return null;
    }

    @Override
    public List<TaskDTO> readAllByEmployee(User assignedEmployee) {
        return null;
    }
}
