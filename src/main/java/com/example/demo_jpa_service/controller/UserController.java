package com.example.demo_jpa_service.controller;

import com.example.demo_jpa_service.dto.UserRequestDTO;
import com.example.demo_jpa_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserRequestDTO createUser(@Valid @RequestBody UserRequestDTO userCreationRequest) {
        return userService.persistUser(userCreationRequest);
    }

    @PutMapping
    public UserRequestDTO updateUser(@RequestBody UserRequestDTO userUpdationRequest) {
        return userService.persistUser(userUpdationRequest);
    }

    @GetMapping("/{id}")
    public UserRequestDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserRequestDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
