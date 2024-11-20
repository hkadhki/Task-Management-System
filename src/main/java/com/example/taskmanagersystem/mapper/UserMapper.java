package com.example.taskmanagersystem.mapper;

import com.example.taskmanagersystem.dto.RegisterDto;
import com.example.taskmanagersystem.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between {@link RegisterDto} and {@link UserEntity}.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Converts a {@link RegisterDto} to a {@link UserEntity}.
     * @param registerDto the DTO containing the user registration data.
     * @return a {@link UserEntity} with the mapped fields.
     */
    UserEntity toUserEntity(RegisterDto registerDto);

}
