package com.example.taskmanagersystem.service;

import com.example.taskmanagersystem.dto.LoginDto;
import com.example.taskmanagersystem.dto.RegisterDto;
import com.example.taskmanagersystem.exceptions.ErrorInputDataException;

public interface AuthService {
    String login(LoginDto loginDto);
    void register(RegisterDto registerDto) throws  ErrorInputDataException;
}
