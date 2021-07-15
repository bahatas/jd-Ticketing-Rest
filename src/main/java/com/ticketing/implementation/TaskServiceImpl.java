package com.ticketing.implementation;

import com.ticketing.dto.ProjectDTO;
import com.ticketing.dto.TaskDTO;
import com.ticketing.entity.Project;
import com.ticketing.entity.Task;
import com.ticketing.entity.User;
import com.ticketing.enums.Status;
import com.ticketing.exception.TicketingProjectException;
import com.ticketing.repository.TaskRepository;
import com.ticketing.repository.UserRepository;
import com.ticketing.service.TaskService;
import com.ticketing.utils.MapperUtil;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void delete(long id) throws TicketingProjectException {

        Task foundedTask = taskRepository.findById(id).orElseThrow(() -> new TicketingProjectException("Task doesn't exist"));

        foundedTask.setIsDeleted(true);

        taskRepository.save(foundedTask);
    }

    @Override
    public int totalNonCompletedTasks(String projectCode) {
        return taskRepository.totalNonCompletedTask(projectCode);
    }

    @Override
    public int totalCompletedTasks(String projectCode) {
        return taskRepository.totalCompletedTask(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO project) {

        List<TaskDTO> taskDTOs = listAllByProject(project);

        taskDTOs.forEach(taskDTO -> {
            try {
                delete(taskDTO.getId());
            } catch (TicketingProjectException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public List<TaskDTO> listAllByProject(ProjectDTO projectDTO) {
        List<Task> list = taskRepository.findAllByProject(mapperUtil.convert(projectDTO, new Project()));

        return list.stream().map(each -> mapperUtil.convert(each, new TaskDTO())).collect(Collectors.toList());


    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) throws TicketingProjectException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(()->new TicketingProjectException("This user does not exist"));
       List<Task> list = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,user);

       return list.stream().map(each-> mapperUtil.convert(each, new TaskDTO())).collect(Collectors.toList());


    }

    @Override
    public List<TaskDTO> listAllTasksByProjectManager() throws TicketingProjectException {

        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(()->new TicketingProjectException("This user does not exist"));

        List<Task> allByAssignedEmployee = taskRepository.findAllByAssignedEmployee(user);

        return allByAssignedEmployee.stream().map(each-> mapperUtil.convert(each, new TaskDTO())).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateStatus(TaskDTO dto) throws TicketingProjectException {
        Task task = taskRepository.findById(dto.getId()).orElseThrow(()->new TicketingProjectException("Task does not exist"));

        task.setTaskStatus(dto.getTaskStatus());

        Task savedTask = taskRepository.save(task);

        return mapperUtil.convert(savedTask, new TaskDTO());

    }

    @Override
    public List<TaskDTO> readAllByEmployee(User assignedEmployee) {

        List<Task> allByAssignedEmployee = taskRepository.findAllByAssignedEmployee(assignedEmployee);

        return allByAssignedEmployee.stream().
                map(each-> mapperUtil.convert(each, new TaskDTO())).collect(Collectors.toList());



    }
}
