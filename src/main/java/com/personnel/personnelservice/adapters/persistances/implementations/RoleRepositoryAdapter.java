package com.personnel.personnelservice.adapters.persistances.implementations;

import com.personnel.personnelservice.adapters.persistances.entities.Role;
import com.personnel.personnelservice.core.ports.repositories.RoleRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {
    private final JpaRoleRepository jpaRoleRepository;
    @Override
    public Role save(Role role) {
        return jpaRoleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRoleRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRoleRepository.deleteById(id);
    }

    @Override
    public List<Role> findAll() {
        return jpaRoleRepository.findAll();
    }

    @Override
    public Optional<Role> findByName(String roleName) {
        return jpaRoleRepository.findByName(roleName);
    }
}
