package com.example.taskmanagersystem.mapper;

import com.example.taskmanagersystem.dto.CommentCreateDto;
import com.example.taskmanagersystem.dto.CommentResponseDto;
import com.example.taskmanagersystem.model.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between Comment-related DTOs and Entities.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    /**
     * Converts a {@link CommentCreateDto} to a {@link CommentEntity}.
     *
     * @param commentCreateDto the DTO containing comment creation data.
     * @return a {@link CommentEntity} with the mapped fields.
     */
    CommentEntity toCommentEntity(CommentCreateDto commentCreateDto);


    /**
     * Converts a {@link CommentEntity} to a {@link CommentResponseDto}.
     *
     * @param commentEntity the comment entity to be mapped.
     * @return a {@link CommentResponseDto} with the mapped and computed fields.
     */
    @Mapping(target = "author", expression = "java(commentEntity.getAuthor().getUsername())")
    CommentResponseDto toCommentResponseDto(CommentEntity commentEntity);

}
