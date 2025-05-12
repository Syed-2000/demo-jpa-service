package com.example.demo_jpa_service.repository;

import com.example.demo_jpa_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
