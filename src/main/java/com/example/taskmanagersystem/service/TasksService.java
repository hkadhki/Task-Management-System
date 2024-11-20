package com.example.taskmanagersystem.service;

import com.example.taskmanagersystem.dto.CommentCreateDto;
import com.example.taskmanagersystem.dto.CreateTaskDto;
import com.example.taskmanagersystem.dto.FindTasksDto;
import com.example.taskmanagersystem.dto.TaskResponseDto;
import com.example.taskmanagersystem.model.Priority;
import com.example.taskmanagersystem.model.Status;

import java.util.List;

public interface TasksService {

    void createTask(CreateTaskDto createTaskDto, String email);
    void deleteTask(String title);
    void editStatus(String title, Status newStatus, String email);
    void editPriority(String title, Priority newPriority) ;
    void editExecutor(String title, String newExecutor) ;
    void addComment(CommentCreateDto newComment, String email);
    TaskResponseDto showTaskByTitle(String title );
    List<TaskResponseDto> showTaskByExecutorUsername(String executor, Integer limit, Integer offset);
    List<TaskResponseDto> showTaskByExecutorEmail(String executor, Integer limit, Integer offset);
    List<TaskResponseDto> showAllTasks(Integer limit, Integer offset);
    List<TaskResponseDto> showAllTasksBySpecification(FindTasksDto findTasksDto, Integer limit, Integer offset);
}
