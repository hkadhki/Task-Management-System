package com.example.taskmanagersystem;

import com.example.taskmanagersystem.dto.CommentCreateDto;
import com.example.taskmanagersystem.dto.CreateTaskDto;
import com.example.taskmanagersystem.dto.FindTasksDto;
import com.example.taskmanagersystem.dto.TaskResponseDto;
import com.example.taskmanagersystem.exceptions.ErrorInputDataException;
import com.example.taskmanagersystem.exceptions.ErrorPermissionException;
import com.example.taskmanagersystem.mapper.CommentMapper;
import com.example.taskmanagersystem.mapper.TaskMapper;
import com.example.taskmanagersystem.model.*;
import com.example.taskmanagersystem.repository.CommentRepository;
import com.example.taskmanagersystem.repository.TaskRepository;
import com.example.taskmanagersystem.repository.TaskSpecification;
import com.example.taskmanagersystem.repository.UserRepository;
import com.example.taskmanagersystem.service.impl.TasksServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.KeysetScrollSpecification;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskSpecification taskSpecification;

    @InjectMocks
    private TasksServiceImpl taskService;

    @Test
    void createTaskTest() {
        //given
        CreateTaskDto createTaskDto = new CreateTaskDto("title", "disc", "PENDING", "LOW", "username");
        CreateTaskDto createTaskDto2 = new CreateTaskDto("true", "disc", "PENDING", "LOW", "username");
        UserEntity userEntity = new UserEntity();
        when(taskRepository.existsByTitle(createTaskDto.getTitle())).thenReturn(false);
        when(taskRepository.existsByTitle("true")).thenReturn(true);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmail("user")).thenReturn(Optional.of(userEntity));
        when(taskMapper.toTaskEntity(createTaskDto)).thenReturn(new TaskEntity());


        //when
        taskService.createTask(createTaskDto,"user");
        Throwable exception = assertThrowsExactly(ErrorInputDataException.class,
                ()->{taskService.createTask(createTaskDto2, "user");} );

        //then
        verify(userRepository, times(1)).findByEmail("user");
        verify(userRepository, times(1)).findByUsername("username");
        verify(taskRepository, times(1)).existsByTitle("title");
        assertEquals(ErrorInputDataException.class, exception.getClass());
    }

    @Test
    void deleteTaskTest() {
        //given
        TaskEntity task = new TaskEntity();
        TaskEntity task2 = new TaskEntity();
        task.setTitle("title");
        task2.setTitle("title2");
        when(taskRepository.findByTitle("title")).thenReturn(Optional.of(task));
        when(taskRepository.findByTitle("title2")).thenReturn(Optional.empty());


        //when
        taskService.deleteTask("title");
        Throwable exception = assertThrowsExactly(ErrorInputDataException.class,
                ()->{taskService.deleteTask("title2");} );

        //then
        verify(taskRepository, times(1)).findByTitle("title");
        assertEquals(ErrorInputDataException.class, exception.getClass());
    }

    @Test
    void editStatusTest() {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("username");
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setEmail("username2");
        TaskEntity task = new TaskEntity();
        task.setExecutor(userEntity);
        RoleEntity role = new RoleEntity();
        role.setName("USER");
        userEntity2.setRoles(List.of(role));
        when(taskRepository.findByTitle("title")).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("username")).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmail("username2")).thenReturn(Optional.of(userEntity2));

        //when
        taskService.editStatus("title", Status.PENDING, "username");
        Throwable exception = assertThrowsExactly(ErrorPermissionException.class,
                ()->{taskService.editStatus("title", Status.PENDING, "username2");} );

        //then
        verify(taskRepository, times(2)).findByTitle("title");
        assertEquals(ErrorPermissionException.class, exception.getClass());
        verify(userRepository, times(1)).findByEmail("username");
        verify(userRepository, times(1)).findByEmail("username2");
    }

    @Test
    void editPriorityTest() {
        //given
        TaskEntity task = new TaskEntity();
        when(taskRepository.findByTitle("title")).thenReturn(Optional.of(task));

        //when
        taskService.editPriority("title", Priority.LOW);

        //then
        verify(taskRepository, times(1)).findByTitle("title");
    }

    @Test
    void editExecutorTest(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("username");
        TaskEntity task = new TaskEntity();
        when(taskRepository.findByTitle("title")).thenReturn(Optional.of(task));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));


        //when
        taskService.editExecutor("title", "username");


        //then
        verify(taskRepository, times(1)).findByTitle("title");
        verify(userRepository, times(1)).findByUsername("username");
    }

    @Test
    void addCommentTest() {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("username");
        RoleEntity role = new RoleEntity();
        role.setName("USER");
        userEntity.setRoles(List.of(role));
        TaskEntity task = new TaskEntity();
        task.setExecutor(userEntity);
        when(taskRepository.findByTitle("title")).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("username")).thenReturn(Optional.of(userEntity));
        CommentCreateDto commentCreateDto = new CommentCreateDto("title", "text");
        task.setComments(new ArrayList<>());
        when(commentMapper.toCommentEntity(commentCreateDto)).thenReturn(new CommentEntity());
        when(userRepository.findByEmail("username")).thenReturn(Optional.of(userEntity));


        //when
        taskService.addComment(commentCreateDto, "username");

        //then
        verify(taskRepository, times(1)).findByTitle("title");
        verify(userRepository, times(2)).findByEmail("username");
    }

    @Test
    void showTaskByTitleTest() {
        //given
        TaskEntity task = new TaskEntity();
        task.setTitle("title");
        task.setDescription("disc");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("username");
        task.setExecutor(userEntity);
        task.setAuthor(userEntity);
        when(taskRepository.findByTitle("title")).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponseDto(task)).thenReturn(new TaskResponseDto("title", "disc", Status.PENDING, Priority.HIGH, "username", "username", List.of()));

        //when
        TaskResponseDto taskResponseDto = taskService.showTaskByTitle("title");

        //then
        Assertions.assertEquals("title", taskResponseDto.getTitle());
        Assertions.assertEquals("disc", taskResponseDto.getDescription());
        Assertions.assertEquals(Priority.HIGH, taskResponseDto.getPriority());
        Assertions.assertEquals(Status.PENDING, taskResponseDto.getStatus());
        Assertions.assertEquals("username", taskResponseDto.getAuthorName());
        Assertions.assertEquals("username", taskResponseDto.getExecutorName());
    }

    @Test
    void showTaskByExecutorUsernameTest(){
        //given
        TaskEntity task = new TaskEntity();
        task.setTitle("title");
        task.setDescription("disc");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("username");
        task.setExecutor(userEntity);
        task.setAuthor(userEntity);
        Page<TaskEntity> taskEntities = new PageImpl<>(List.of(task));
        when(taskRepository.findByExecutor(userEntity, PageRequest.of(0, 20))).thenReturn(taskEntities);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userEntity));
        when(taskMapper.toTaskResponseDtoList(taskEntities.getContent())).thenReturn(List.of(new TaskResponseDto("title", "disc", Status.PENDING, Priority.HIGH, "username", "username", List.of())));

        //when
        List<TaskResponseDto> taskResponseDto = taskService.showTaskByExecutorUsername("username", 20, 0);

        //then
        Assertions.assertEquals("title", taskResponseDto.get(0).getTitle());
        Assertions.assertEquals("disc", taskResponseDto.get(0).getDescription());
        Assertions.assertEquals(Priority.HIGH, taskResponseDto.get(0).getPriority());
        Assertions.assertEquals(Status.PENDING, taskResponseDto.get(0).getStatus());
        Assertions.assertEquals("username", taskResponseDto.get(0).getAuthorName());
        Assertions.assertEquals("username", taskResponseDto.get(0).getExecutorName());
    }

    @Test
    void showTaskByExecutorEmailTest(){
        //given
        TaskEntity task = new TaskEntity();
        task.setTitle("title");
        task.setDescription("disc");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("username");
        task.setExecutor(userEntity);
        task.setAuthor(userEntity);
        Page<TaskEntity> taskEntities = new PageImpl<>(List.of(task));
        when(taskRepository.findByExecutor(userEntity, PageRequest.of(0, 20))).thenReturn(taskEntities);
        when(userRepository.findByEmail("username")).thenReturn(Optional.of(userEntity));
        when(taskMapper.toTaskResponseDtoList(taskEntities.getContent())).thenReturn(List.of(new TaskResponseDto("title", "disc", Status.PENDING, Priority.HIGH, "username", "username", List.of())));

        //when
        List<TaskResponseDto> taskResponseDto = taskService.showTaskByExecutorEmail("username", 20, 0);

        //then
        Assertions.assertEquals("title", taskResponseDto.get(0).getTitle());
        Assertions.assertEquals("disc", taskResponseDto.get(0).getDescription());
        Assertions.assertEquals(Priority.HIGH, taskResponseDto.get(0).getPriority());
        Assertions.assertEquals(Status.PENDING, taskResponseDto.get(0).getStatus());
        Assertions.assertEquals("username", taskResponseDto.get(0).getAuthorName());
        Assertions.assertEquals("username", taskResponseDto.get(0).getExecutorName());
    }

    @Test
    void showAllTasksTest(){
        //given
        TaskEntity task = new TaskEntity();
        task.setTitle("title");
        task.setDescription("disc");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("username");
        task.setExecutor(userEntity);
        task.setAuthor(userEntity);
        Page<TaskEntity> taskEntities = new PageImpl<>(List.of(task));
        when(taskRepository.findAll(PageRequest.of(0, 20))).thenReturn(taskEntities);
        when(taskMapper.toTaskResponseDtoList(taskEntities.getContent())).thenReturn(List.of(new TaskResponseDto("title", "disc", Status.PENDING, Priority.HIGH, "username", "username", List.of())));

        //when
        List<TaskResponseDto> taskResponseDto = taskService.showAllTasks( 20, 0);

        //then
        Assertions.assertEquals("title", taskResponseDto.get(0).getTitle());
        Assertions.assertEquals("disc", taskResponseDto.get(0).getDescription());
        Assertions.assertEquals(Priority.HIGH, taskResponseDto.get(0).getPriority());
        Assertions.assertEquals(Status.PENDING, taskResponseDto.get(0).getStatus());
        Assertions.assertEquals("username", taskResponseDto.get(0).getAuthorName());
        Assertions.assertEquals("username", taskResponseDto.get(0).getExecutorName());
    }


}
