package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.User;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaBaseRepository<User>  {
    Optional<User> findByEmail(String email);
    List<User> findUserByCreatedBy(String username);
}
