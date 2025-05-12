package com.example.demo_jpa_service.mapper;

import com.example.demo_jpa_service.dto.UserRequestDTO;
import com.example.demo_jpa_service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO dto);
    UserRequestDTO toResponseDto(User entity);
}
