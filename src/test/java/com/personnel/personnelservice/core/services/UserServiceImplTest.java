package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.core.services.UserServiceImpl;
import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.core.models.dtos.UserDto;
import com.personnel.personnelservice.adapters.persistances.mappers.UserMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaRoleRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock private JpaUserRepository jpaUserRepository;
    @Mock private JpaRoleRepository jpaRoleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;
    private Faker faker;
    @BeforeEach
    void setUp() {
        faker = new Faker();
    }
    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setLastName(faker.name().lastName());
        user.setFirstName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(passwordEncoder.encode("oldPassword"));

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setLastName(faker.name().lastName());
        userDto.setFirstName(faker.name().firstName());
        userDto.setEmail(faker.internet().emailAddress());
        userDto.setPassword("newPassword");

        // Simulation des comportements
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userMapper.toDTO(any(User.class))).thenReturn(userDto);

        UserDto updatedUser = userService.updateUser(userDto);

        assertNotNull(updatedUser);
        verify(jpaUserRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();

        when(jpaUserRepository.existsById(userId)).thenReturn(true);
        doNothing().when(jpaUserRepository).deleteById(userId);

        userService.deleteUser(userId);
        verify(jpaUserRepository, times(1)).deleteById(userId);
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setLastName(faker.name().lastName());
            user.setFirstName(faker.name().firstName());
            users.add(user);
        }

        when(jpaUserRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(any(User.class))).thenReturn(new UserDto());

        List<UserDto> userDtos = userService.getAllUsers();

        assertFalse(userDtos.isEmpty());
        assertEquals(3, userDtos.size());
        verify(jpaUserRepository, times(1)).findAll();
    }

    @Test
    void addRoleToUser() {
    }

    @Test
    void removeRoleFromUser() {
    }

    @Test
    void updateUserRoles() {
    }

    @Test
    void getUserRoles() {
    }
}