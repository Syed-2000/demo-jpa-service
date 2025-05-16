package com.example.demo_jpa_service.integration;

import com.example.demo_jpa_service.dto.UserRequestDTO;
import com.example.demo_jpa_service.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testGetUserById() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO(null,"John Doe", 30, "Active");

        MockHttpServletResponse response = mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO))
        ).andReturn().getResponse();

        UserRequestDTO savedUser = objectMapper.readValue(response.getContentAsString(), UserRequestDTO.class);

        assertThat(userRepository.count()).isEqualTo(1);

        mockMvc.perform(get("/api/user/" + savedUser.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.status").value("Active"))
                .andExpect(jsonPath("$.age").value(30));

    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequestDTO(null, "John", 20, "ACTIVE"))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequestDTO(null, "Jane", 22, "INACTIVE"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testDeleteUser() throws Exception {
        UserRequestDTO user = new UserRequestDTO(null, "Mark", 28, "ACTIVE");
        String response = mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        UserRequestDTO savedUser = objectMapper.readValue(response, UserRequestDTO.class);

        mockMvc.perform(delete("/api/user/" + savedUser.id()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user/" + savedUser.id()))
                .andExpect(status().isBadRequest());
    }
}
