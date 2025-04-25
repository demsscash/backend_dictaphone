package com.personnel.personnelservice.core.ports.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User save(User user);
    void deleteById(UUID id);
}
