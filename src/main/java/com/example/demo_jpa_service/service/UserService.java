package com.example.demo_jpa_service.service;


import com.example.demo_jpa_service.dto.UserRequestDTO;

import java.util.List;

public interface UserService {
    UserRequestDTO persistUser(UserRequestDTO userRequestDTO);
    UserRequestDTO getUserById(Long id);
    List<UserRequestDTO> getAllUsers();
    void deleteUserById(Long id);
}
