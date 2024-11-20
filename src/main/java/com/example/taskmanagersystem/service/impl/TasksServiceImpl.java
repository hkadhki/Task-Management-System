package com.example.taskmanagersystem.service.impl;

import com.example.taskmanagersystem.dto.CommentCreateDto;
import com.example.taskmanagersystem.dto.CreateTaskDto;
import com.example.taskmanagersystem.dto.FindTasksDto;
import com.example.taskmanagersystem.dto.TaskResponseDto;
import com.example.taskmanagersystem.exceptions.*;
import com.example.taskmanagersystem.mapper.CommentMapper;
import com.example.taskmanagersystem.mapper.TaskMapper;
import com.example.taskmanagersystem.model.*;
import com.example.taskmanagersystem.repository.CommentRepository;
import com.example.taskmanagersystem.repository.TaskRepository;
import com.example.taskmanagersystem.repository.TaskSpecification;
import com.example.taskmanagersystem.repository.UserRepository;
import com.example.taskmanagersystem.service.TasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the {@link TasksService} interface.
 * This service provides the core business logic for managing tasks
 */
@Slf4j
@Service
public class TasksServiceImpl implements TasksService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final TaskSpecification taskSpecification;


    /**
     * Constructor to inject dependencies.
     *
     * @param userRepository     repository for managing {@link UserEntity} data
     * @param taskRepository     repository for managing {@link TaskEntity} data
     * @param taskMapper         mapper for converting DTOs to {@link TaskEntity}
     * @param commentMapper      mapper for converting DTOs to {@link CommentEntity}
     * @param commentRepository  repository for managing {@link CommentEntity} data
     * @param taskSpecification  utility for building task query specifications
     */
    public TasksServiceImpl(UserRepository userRepository, TaskRepository taskRepository, TaskMapper taskMapper, CommentMapper commentMapper, CommentRepository commentRepository, TaskSpecification taskSpecification) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.taskSpecification = taskSpecification;
    }


    /**
     * Creates a new task.
     *
     * @param createTaskDto the DTO containing task details
     * @param email         the email of the user creating the task
     * @throws ErrorInputDataException if a task with the same title already exists
     */
    @Override
    @Transactional
    public void createTask(CreateTaskDto createTaskDto, String email) {
        if(taskRepository.existsByTitle(createTaskDto.getTitle())){
            log.error("Task '{}' already exist", createTaskDto.getTitle());
            throw new ErrorInputDataException("Task "+ createTaskDto.getTitle() +" already exist");
        }


        TaskEntity task = taskMapper.toTaskEntity(createTaskDto);
        task.setExecutor(findUserByUsername(createTaskDto.getExecutor()));
        task.setAuthor(findUserByEmail(email));

        taskRepository.save(task);
        log.info("Success create Task '{}'", createTaskDto.getTitle() );
    }

    /**
     * Deletes an existing task.
     *
     * @param title the title of the task to delete
     */
    @Override
    @Transactional
    @CacheEvict(value = "tasks", key = "#title")
    public void deleteTask(String title){
        TaskEntity task = findTaskByTitle(title);

        taskRepository.delete(task);
        log.info("Success delete Task '{}'", title);
    }

    /**
     * Updates the status of a task.
     *
     * @param title     the title of the task
     * @param newStatus the new status to set
     * @param email     the email of the user making the update
     * @throws ErrorPermissionException if the user lacks permission to update the task
     */
    @Transactional
    @Override
    @CachePut(value = "tasks", key = "#title")
    public void editStatus(String title, Status newStatus, String email){
        TaskEntity task = getTaskIfUserHasPermission(title, email);
        task.setStatus(newStatus);
        log.info("Task '{}' status updated to '{}' by '{}'", title, newStatus, email);
    }

    /**
     * Updates the priority of a task.
     *
     * @param title       the title of the task
     * @param newPriority the new priority to set
     */
    @Transactional
    @Override
    @CachePut(value = "tasks", key = "#title")
    public void editPriority(String title, Priority newPriority){
        TaskEntity task = findTaskByTitle(title);
        task.setPriority(newPriority);
        log.info("Task '{}' priority updated to '{}'", title, newPriority);
    }

    /**
     * Updates the executor of a task.
     *
     * @param title       the title of the task
     * @param newExecutor the username of the new executor
     */
    @Transactional
    @Override
    @CachePut(value = "tasks", key = "#title")
    public void editExecutor(String title, String newExecutor){
        TaskEntity task = findTaskByTitle(title);
        UserEntity user = findUserByUsername(newExecutor);
        task.setExecutor(user);
        log.info("Task '{}' executor changed to '{}'", title, newExecutor);
    }

    /**
     * Adds a comment to a task.
     *
     * @param newComment the DTO containing comment details
     * @param email      the email of the user adding the comment
     * @throws ErrorPermissionException if the user lacks permission to comment on the task
     */
    @Transactional
    @Override
    @CachePut(value = "tasks", key = "#newComment.taskTitle")
    public void addComment(CommentCreateDto newComment, String email) throws ErrorPermissionException{
        TaskEntity task = getTaskIfUserHasPermission(newComment.getTaskTitle(), email);

        CommentEntity comment = commentMapper.toCommentEntity(newComment);
        comment.setDate(Date.valueOf(LocalDate.now()));
        comment.setAuthor(findUserByEmail(email));
        commentRepository.save(comment);

        task.getComments().add(comment);

        log.info("Success create comment to task '{}'", newComment.getTaskTitle() );
    }

    /**
     * Retrieves a task by its title.
     *
     * @param title the title of the task to retrieve
     * @return a {@link TaskResponseDto} representing the task details
     * @throws ErrorInputDataException if the task is not found
     */
    @Override
    @Cacheable(value = "tasks", key = "#title")
    public TaskResponseDto showTaskByTitle(String title) {
        TaskEntity task = findTaskByTitle(title);
        return taskMapper.toTaskResponseDto(task);
    }

    /**
     * Retrieves tasks assigned to a specific executor by their username.
     *
     * @param executor the username of the executor
     * @param limit    the number of tasks to return
     * @param offset   the starting point for the result set
     * @return a list of {@link TaskResponseDto} representing the tasks
     * @throws ErrorInputDataException if the executor is not found
     */
    @Override
    public List<TaskResponseDto> showTaskByExecutorUsername(String executor, Integer limit, Integer offset) {
        UserEntity executorUser = findUserByUsername(executor);
        Page<TaskEntity> tasks = taskRepository.findByExecutor(executorUser, PageRequest.of(offset, limit));
        return taskMapper.toTaskResponseDtoList(tasks.getContent());
    }

    /**
     * Retrieves tasks assigned to a specific executor by their email.
     *
     * @param executor the email of the executor
     * @param limit    the number of tasks to return
     * @param offset   the starting point for the result set
     * @return a list of {@link TaskResponseDto} representing the tasks
     * @throws ErrorInputDataException if the executor is not found
     */
    @Override
    public List<TaskResponseDto> showTaskByExecutorEmail(String executor, Integer limit, Integer offset) {
        UserEntity executorUser = findUserByEmail(executor);
        Page<TaskEntity> tasks = taskRepository.findByExecutor(executorUser, PageRequest.of(offset, limit));
        return taskMapper.toTaskResponseDtoList(tasks.getContent());
    }

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param limit  the number of tasks to return
     * @param offset the starting point for the result set
     * @return a list of {@link TaskResponseDto} representing the tasks
     */
    @Override
    public List<TaskResponseDto> showAllTasks(Integer limit, Integer offset) {
        Page<TaskEntity> tasks = taskRepository.findAll(PageRequest.of(offset, limit));
        return taskMapper.toTaskResponseDtoList(tasks.getContent());
    }

    /**
     * Retrieves tasks based on specified criteria with pagination support.
     *
     * @param findTasksDto the DTO containing filter criteria
     * @param limit        the number of tasks to return
     * @param offset       the starting point for the result set
     * @return a list of {@link TaskResponseDto} representing the filtered tasks
     */
    @Override
    public List<TaskResponseDto> showAllTasksBySpecification(FindTasksDto findTasksDto, Integer limit, Integer offset) {

        Page<TaskEntity> tasks = taskRepository.findAll(taskSpecification.build(findTasksDto), PageRequest.of(offset, limit));
        return taskMapper.toTaskResponseDtoList(tasks.getContent());
    }


    /**
     * Retrieves a task if the user has the necessary permissions to view or modify it.
     *
     * @param title the title of the task
     * @param email the email of the user requesting access
     * @return the {@link TaskEntity} if the user has permissions
     * @throws ErrorPermissionException if the user lacks the necessary permissions
     */
    private TaskEntity getTaskIfUserHasPermission(String title, String email) {
        UserEntity user = findUserByEmail(email);
        TaskEntity taskEntity = findTaskByTitle(title);

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
        if (isAdmin || taskEntity.getExecutor().equals(user)) {
            return taskEntity;
        }

        log.error("User '{}' does not have permission to modify task '{}'", email, title);
        throw new ErrorPermissionException("You do not have permission to change task");
    }



    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the {@link UserEntity} if found
     * @throws UnauthorizedErrorException if the user is not found
     */
    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedErrorException("User with email '" + email + "' not found"));
    }



    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return the {@link UserEntity} if found
     * @throws ErrorInputDataException if the user is not found
     */
    private UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorInputDataException("User with username '" + username + "' not found"));
    }


    /**
     * Retrieves a task by its title.
     *
     * @param title the title of the task
     * @return the {@link TaskEntity} if found
     * @throws ErrorInputDataException if the task is not found
     */
        private TaskEntity findTaskByTitle(String title) {
        return taskRepository.findByTitle(title)
                .orElseThrow(() -> new ErrorInputDataException("Task with title '" + title + "' not found"));
    }
}
