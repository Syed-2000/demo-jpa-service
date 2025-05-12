package com.example.demo_jpa_service.repository;

import com.example.demo_jpa_service.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("1. Save user - should return saved user with ID")
    public void saveUser_shouldReturnSavedUser() {
        User user = new User.Builder().name("john").age(30).status("active").build();
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("john", savedUser.getName());
    }

    @Test
    @DisplayName("2. Find all users - should return saved users")
    public void findAll_shouldReturnAllUsers() {
        User user1 = new User.Builder().name("alice").age(25).status("active").build();
        User user2 = new User.Builder().name("bob").age(35).status("active").build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("3. Find by ID - should return correct user")
    public void findById_shouldReturnUser() {
        User user = new User.Builder().name("carol").age(28).status("active").build();
        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("carol", foundUser.get().getName());
    }

    @Test
    @DisplayName("5. Update user - should return updated user")
    public void updateUser_shouldReturnUpdatedUser() {
        User user = new User.Builder().name("dave").age(40).status("active").build();
        User savedUser = userRepository.save(user);

        savedUser.setName("david");
        User updatedUser = userRepository.save(savedUser);

        assertEquals("david", updatedUser.getName());
    }

    @Test
    @DisplayName("6. Delete user - should result in empty findById")
    public void deleteUser_shouldResultInEmptyMono() {
        User user = new User.Builder().name("emma").age(22).status("active").build();
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    @DisplayName("7. Find non-existent user - should return empty")
    public void findNonExistentUser_shouldReturnEmpty() {
        Optional<User> user = userRepository.findById(999L);
        assertFalse(user.isPresent());
    }
}
