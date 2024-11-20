package com.example.taskmanagersystem.service.impl;

import com.example.taskmanagersystem.dto.LoginDto;
import com.example.taskmanagersystem.dto.RegisterDto;
import com.example.taskmanagersystem.exceptions.ErrorInputDataException;

import com.example.taskmanagersystem.jwt.JwtGenerator;
import com.example.taskmanagersystem.mapper.UserMapper;
import com.example.taskmanagersystem.model.UserEntity;
import com.example.taskmanagersystem.repository.RoleRepository;
import com.example.taskmanagersystem.repository.UserRepository;
import com.example.taskmanagersystem.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;


    /**
     * Constructor for initializing the AuthServiceImpl with required dependencies.
     *
     * @param userRepository the repository for managing user entities
     * @param roleRepository the repository for managing user roles
     * @param authenticationManager the authentication manager for handling login authentication
     * @param jwtGenerator the JWT generator for generating access tokens
     * @param userMapper the mapper for converting between DTOs and entities
     * @param passwordEncoder the password encoder for hashing passwords
     */
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }



    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginDto the DTO containing login credentials (email and password)
     * @return a JWT token if authentication is successful
     */
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtGenerator.generateToken(authentication);
    }



    /**
     * Registers a new user with the provided registration details.
     * <p>
     * This method checks for duplicate email and username to ensure uniqueness. The password is hashed
     * using the {@link PasswordEncoder}. The user is assigned the default "USER" role.
     * </p>
     *
     * @param registerDto the DTO containing user registration details (email, username, password)
     * @throws ErrorInputDataException if the email or username is already in use
     */
    @Transactional
    public void register(RegisterDto registerDto) throws ErrorInputDataException {
        if(userRepository.existsByEmail(registerDto.getEmail())){
            log.error("Email {} is already in use", registerDto.getEmail());
            throw new ErrorInputDataException("Email " + registerDto.getEmail() + " is already in use");
        }

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            log.error("Username {} is already in use", registerDto.getEmail());
            throw new ErrorInputDataException("Username " + registerDto.getEmail() + " is already in use");
        }

        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        UserEntity user = userMapper.toUserEntity(registerDto);
        user.setRoles(List.of(roleRepository.findByName("USER")));

        userRepository.save(user);
    }
}

