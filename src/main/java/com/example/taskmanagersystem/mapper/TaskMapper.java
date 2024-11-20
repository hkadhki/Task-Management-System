package com.example.taskmanagersystem.mapper;

import com.example.taskmanagersystem.dto.CreateTaskDto;
import com.example.taskmanagersystem.dto.RegisterDto;
import com.example.taskmanagersystem.dto.TaskResponseDto;
import com.example.taskmanagersystem.model.CommentEntity;
import com.example.taskmanagersystem.model.TaskEntity;
import com.example.taskmanagersystem.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting between Task-related DTOs and Entities.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentListMapper.class})
public interface TaskMapper {

    /**
     * Converts a {@link CreateTaskDto} to a {@link TaskEntity}.
     *
     * @param createTaskDto the DTO containing the task creation data.
     * @return a {@link TaskEntity} with the mapped fields.
     */
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "executor", ignore = true)
    TaskEntity toTaskEntity(CreateTaskDto createTaskDto);


    /**
     * Converts  a {@link TaskEntity} to a {@link TaskResponseDto}.
     *
     * @param taskEntity the task entity to be mapped.
     * @return a {@link TaskResponseDto} with the mapped and computed fields.
     */
    @Mapping(target = "authorName", expression = "java(taskEntity.getAuthor().getUsername())")
    @Mapping(target = "executorName", expression = "java(taskEntity.getExecutor().getUsername())")
    TaskResponseDto toTaskResponseDto(TaskEntity taskEntity);


    /**
     * Converts a list of {@link TaskEntity} objects to a list of {@link TaskResponseDto} objects.
     *
     * @param taskEntities the list of task entities to be mapped.
     * @return a list of {@link TaskResponseDto} objects.
     */
    List<TaskResponseDto> toTaskResponseDtoList(List<TaskEntity> taskEntities);
}
