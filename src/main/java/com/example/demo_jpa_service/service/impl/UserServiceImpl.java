package com.example.demo_jpa_service.service.impl;

import com.example.demo_jpa_service.dto.UserRequestDTO;
import com.example.demo_jpa_service.mapper.UserMapper;
import com.example.demo_jpa_service.model.User;
import com.example.demo_jpa_service.repository.UserRepository;
import com.example.demo_jpa_service.service.UserService;
import com.example.demo_jpa_service.utils.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserRequestDTO persistUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            throw new IllegalArgumentException(ErrorMessages.USER_REQUEST_NULL);
        }
        User user = userMapper.toEntity(userRequestDTO);
        if (user == null) {
            throw new IllegalStateException(ErrorMessages.USER_ENTITY_MAPPING_FAILED);
        }
        log.info("Creating user: {}", user);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserRequestDTO getUserById(Long id) {
       User user = userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.USER_NOT_FOUND, id)));
       UserRequestDTO userRequestDTO = userMapper.toResponseDto(user);
       if(userRequestDTO == null){
           throw new IllegalStateException(ErrorMessages.USER_RESPONSE_MAPPING_FAILED);
       }
        return userRequestDTO;
    }

    @Override
    public List<UserRequestDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponseDto).toList();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
