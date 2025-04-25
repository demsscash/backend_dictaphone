package com.personnel.personnelservice.core.seeders;

import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UserSeeder {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(JpaUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedDatabase() {
        if (userRepository.findByEmail("amina@example.com").isEmpty()) {
            User user = new User();
            user.setLastName("Chakir");
            user.setFirstName("Aicha");
            user.setEmail("amina@example.com");
            user.setPassword(passwordEncoder.encode("Aicha123@"));
            user.setImageId(UUID.fromString("7b04999e-cc82-43c4-a9f6-3dd44d479ef7"));
            user.setPhoneNumber("0600000134");
            user.setCreationDate(LocalDateTime.now());
            user.setLastModifiedDate(LocalDateTime.now());
            user.setBirthDate(LocalDate.now());
            userRepository.save(user);
        }
    }
}
