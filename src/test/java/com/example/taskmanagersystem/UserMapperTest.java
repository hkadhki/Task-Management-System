package com.example.taskmanagersystem;


import com.example.taskmanagersystem.dto.RegisterDto;
import com.example.taskmanagersystem.mapper.UserMapper;
import com.example.taskmanagersystem.model.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper ;



    @Test
    void toUserEntityTest(){
        //given
        RegisterDto registerDto = new RegisterDto("User@gmail.com","User","password");

        //when
        UserEntity userEntity = userMapper.toUserEntity(registerDto);

        //then;
        Assertions.assertEquals(registerDto.getEmail(), userEntity.getEmail());
        Assertions.assertEquals(registerDto.getPassword(), userEntity.getPassword());
        Assertions.assertEquals(registerDto.getUsername(), userEntity.getUsername());
    }
}
