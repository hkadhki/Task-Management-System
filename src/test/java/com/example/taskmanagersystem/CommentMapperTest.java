package com.example.taskmanagersystem;

import com.example.taskmanagersystem.dto.CommentCreateDto;
import com.example.taskmanagersystem.dto.CommentResponseDto;
import com.example.taskmanagersystem.mapper.CommentMapper;
import com.example.taskmanagersystem.model.CommentEntity;
import com.example.taskmanagersystem.model.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootTest
public class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;


    @Test
    void toCommentEntityTest() {

    }

    @Test
    void toCommentResponseDtoTest() {
        //given
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setDate(Date.valueOf(LocalDate.now()));
        commentEntity.setText("text");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        commentEntity.setAuthor(userEntity);

        //when
        CommentResponseDto commentResponseDto = commentMapper.toCommentResponseDto(commentEntity);

        //then
        Assertions.assertEquals(commentEntity.getDate(), commentResponseDto.getDate());
        Assertions.assertEquals("text", commentResponseDto.getText());
        Assertions.assertEquals("username", commentResponseDto.getAuthor());
    }


    @Test
    void toCommentEntityDtoTest() {
        //given
        CommentCreateDto commentCreateDto = new CommentCreateDto("title", "text");

        //when
        CommentEntity commentEntity = commentMapper.toCommentEntity(commentCreateDto);

        //then
        Assertions.assertEquals(commentEntity.getText(), commentCreateDto.getText());

    }
}
