package com.example.demo_jpa_service.controller;

import com.example.demo_jpa_service.dto.UserRequestDTO;
import com.example.demo_jpa_service.exception.GlobalExceptionHandler;
import com.example.demo_jpa_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUser_ValidInput() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO(1L,"John Doe", 30, "Active");

        when(userService.persistUser(any(UserRequestDTO.class))).thenReturn(userRequestDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDTO);
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.status").value("Active"));

        verify(userService, times(1)).persistUser(any(UserRequestDTO.class));
    }

    @Test
    void testCreateUser_InvalidNameNull() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO(1L,null, 30, "Active");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDTO);


        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).persistUser(any(UserRequestDTO.class));
    }



    @Test
    void testCreateUser_InvalidStatusNull() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO(1L,"O.Syed", 30, null);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDTO);


        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).persistUser(any(UserRequestDTO.class));
    }

    @Test
    void testCreateUser_InvalidAge() throws Exception {

        UserRequestDTO userRequestDTO = new UserRequestDTO(1L,"User", -1, "Active");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userRequestDTO);


        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).persistUser(any(UserRequestDTO.class));
    }

    @Test
    void testGetUserById() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO(1L, "John Doe", 30, "Active");
        when(userService.getUserById(1L)).thenReturn(userRequestDTO);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.status").value("Active"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testDeleteUserById() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(1L);
    }


    @Test
    public void testGetAllUsers_returnsListOfUsers() throws Exception {

        List<UserRequestDTO> mockUsers = List.of(
                new UserRequestDTO(1L, "Alice", 25, "ACTIVE"),
                new UserRequestDTO(2L, "Bob", 30, "INACTIVE")
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/api/user")) // Assuming @GetMapping maps to /users
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].status").value("INACTIVE"));
    }

    @Test
    public void testGetAllUsers_returnsEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
