package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.Role;
import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.adapters.persistances.mappers.RoleMapper;
import com.personnel.personnelservice.core.exceptions.BaseException;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.exceptions.RoleAlreadyAssignedException;
import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.core.models.dtos.UserDto;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaRoleRepository;
import com.personnel.personnelservice.adapters.persistances.mappers.UserMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaUserRepository;
import com.personnel.personnelservice.core.ports.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaRoleRepository jpaRoleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User user = jpaUserRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User non trouvé"));

        try {
            userMapper.updateEntity(userDto,user);
            return userMapper.toDTO(user);
        } catch (Exception e) {
            throw new BaseException("Erreur interne lors de la mise à jour de l'utilisateur");
        }
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!jpaUserRepository.existsById(id)) {
            throw new EntityNotFoundException("User non trouvé avec ID: " + id);
        }
        try {
            jpaUserRepository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException("Erreur interne lors de la suppression de l'utilisateur");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = jpaUserRepository.findAll();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Aucun utilisateur trouvé");
        }
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto addRoleToUser(UUID userId, UUID roleId) {
        User user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        Role role = jpaRoleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID"+ roleId));
        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyAssignedException(userId);
        }
        user.getRoles().add(role);

        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDto removeRoleFromUser(UUID userId, UUID roleId) {
        User user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        Role role = jpaRoleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role", userId));

        user.getRoles().remove(role);
        return userMapper.toDTO(jpaUserRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUserRoles(UUID userId, Set<UUID> roleIds) {
        User user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        Set<Role> roles = new HashSet<>(jpaRoleRepository.findAllById(roleIds));
        user.setRoles(roles);

        return userMapper.toDTO(user);
    }

    @Override
    public Set<RoleDto> getUserRoles(UUID userId) {
        User user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));
        return user.getRoles().stream()
                .map(roleMapper::toRoleDto)
                .collect(Collectors.toSet());
    }
}
