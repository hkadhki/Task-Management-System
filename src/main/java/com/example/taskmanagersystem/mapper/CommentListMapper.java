package com.example.taskmanagersystem.mapper;

import com.example.taskmanagersystem.dto.CommentResponseDto;
import com.example.taskmanagersystem.model.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;


/**
 * Mapper interface for converting a list of {@link CommentEntity} objects to a list of {@link CommentResponseDto} objects.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,  unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapper.class})
public interface CommentListMapper {


    /**
     * Converts a list of {@link CommentEntity} objects to a list of {@link CommentResponseDto} objects.
     *
     * @param commentsEntityList the list of comment entities to be mapped.
     * @return a list of {@link CommentResponseDto} objects.
     */
    List<CommentResponseDto> toCommentResponseDtoList(List<CommentEntity> commentsEntityList);
}
