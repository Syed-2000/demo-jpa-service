package com.example.demo_jpa_service.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @Min(value = 0, message = "Id must be non-negative")
        Long id,
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Min(value = 0, message = "Value must be non-negative")
        Integer age,
        @NotNull(message = "Status cannot be null")
        @NotBlank(message = "Status cannot be blank")
        String status) { }
