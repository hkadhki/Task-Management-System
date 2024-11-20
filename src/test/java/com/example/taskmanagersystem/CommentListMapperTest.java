package com.example.taskmanagersystem;

import com.example.taskmanagersystem.dto.CommentResponseDto;
import com.example.taskmanagersystem.mapper.CommentListMapper;
import com.example.taskmanagersystem.model.CommentEntity;
import com.example.taskmanagersystem.model.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CommentListMapperTest {

    @Autowired
    private CommentListMapper commentListMapper;

    @Test
    void toCommentResponseDtoListTest(){
        //given
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setDate(Date.valueOf(LocalDate.now()));
        commentEntity.setText("text");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        commentEntity.setAuthor(userEntity);
        List<CommentEntity> commentEntities = new ArrayList<>();
        commentEntities.add(commentEntity);


        //when
        List<CommentResponseDto> commentResponseDtoList = commentListMapper.toCommentResponseDtoList(commentEntities);

        //then
        Assertions.assertEquals(commentEntity.getDate(), commentResponseDtoList.get(0).getDate());
        Assertions.assertEquals(commentEntity.getText(), commentResponseDtoList.get(0).getText());
        Assertions.assertEquals(userEntity.getUsername(), commentResponseDtoList.get(0).getAuthor());
    }
}
