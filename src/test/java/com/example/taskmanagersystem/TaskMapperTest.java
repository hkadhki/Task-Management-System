package com.example.taskmanagersystem;

import com.example.taskmanagersystem.dto.CreateTaskDto;
import com.example.taskmanagersystem.dto.TaskResponseDto;
import com.example.taskmanagersystem.mapper.TaskMapper;
import com.example.taskmanagersystem.model.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TaskMapperTest {

    @Autowired
    private TaskMapper taskMapper;

    @Test
    void toTaskEntityTest() {
        //given
        CreateTaskDto createTaskDto = new CreateTaskDto("title", "disc", "PENDING", "LOW", "username");

        //when
        TaskEntity taskEntity = taskMapper.toTaskEntity(createTaskDto);

        //then
        Assertions.assertNotNull(taskEntity);
        Assertions.assertEquals(createTaskDto.getTitle(), taskEntity.getTitle());
        Assertions.assertEquals(Status.PENDING, taskEntity.getStatus());
        Assertions.assertEquals(createTaskDto.getDescription(), taskEntity.getDescription());
        Assertions.assertEquals(Priority.LOW, taskEntity.getPriority());
    }

    @Test
    void toTaskResponseDtoTest(){
        //given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle("title");
        taskEntity.setDescription("description");
        taskEntity.setStatus(Status.PENDING);
        taskEntity.setPriority(Priority.LOW);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        taskEntity.setAuthor(userEntity);
        taskEntity.setExecutor(userEntity);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setDate(Date.valueOf(LocalDate.now()));
        commentEntity.setText("text");
        commentEntity.setAuthor(userEntity);
        List<CommentEntity> commentEntities = new ArrayList<>();
        commentEntities.add(commentEntity);
        taskEntity.setComments(commentEntities);

        //when
        TaskResponseDto taskResponseDto = taskMapper.toTaskResponseDto(taskEntity);

        //then
        Assertions.assertNotNull(taskResponseDto);
        Assertions.assertEquals(taskEntity.getTitle(), taskResponseDto.getTitle());
        Assertions.assertEquals(taskEntity.getDescription(), taskResponseDto.getDescription());
        Assertions.assertEquals(Status.PENDING, taskResponseDto.getStatus());
        Assertions.assertEquals(Priority.LOW, taskResponseDto.getPriority());
        Assertions.assertEquals("username", taskResponseDto.getAuthorName());
        Assertions.assertEquals("username", taskResponseDto.getExecutorName());
        Assertions.assertEquals("username", taskResponseDto.getComments().get(0).getAuthor());
        Assertions.assertEquals("text", taskResponseDto.getComments().get(0).getText());
        Assertions.assertEquals(commentEntity.getDate(), taskResponseDto.getComments().get(0).getDate());

    }

    @Test
    void toTaskResponseDtoList(){
        //given
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle("title");
        taskEntity.setDescription("description");
        taskEntity.setStatus(Status.PENDING);
        taskEntity.setPriority(Priority.LOW);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        taskEntity.setAuthor(userEntity);
        taskEntity.setExecutor(userEntity);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setDate(Date.valueOf(LocalDate.now()));
        commentEntity.setText("text");
        commentEntity.setAuthor(userEntity);
        List<CommentEntity> commentEntities = new ArrayList<>();
        commentEntities.add(commentEntity);
        taskEntity.setComments(commentEntities);
        List<TaskEntity> taskEntities = new ArrayList<>();
        taskEntities.add(taskEntity);

        //when
        List<TaskResponseDto> taskResponseDtoList = taskMapper.toTaskResponseDtoList(taskEntities);


        //then
        Assertions.assertEquals(taskEntity.getTitle(), taskResponseDtoList.get(0).getTitle());
        Assertions.assertEquals(taskEntity.getDescription(), taskResponseDtoList.get(0).getDescription());
        Assertions.assertEquals(Status.PENDING, taskResponseDtoList.get(0).getStatus());
        Assertions.assertEquals(Priority.LOW, taskResponseDtoList.get(0).getPriority());
        Assertions.assertEquals("username", taskResponseDtoList.get(0).getAuthorName());
        Assertions.assertEquals("username", taskResponseDtoList.get(0).getExecutorName());
        Assertions.assertEquals("username", taskResponseDtoList.get(0).getComments().get(0).getAuthor());
        Assertions.assertEquals("text", taskResponseDtoList.get(0).getComments().get(0).getText());
        Assertions.assertEquals(commentEntity.getDate(), taskResponseDtoList.get(0).getComments().get(0).getDate());
    }
}
