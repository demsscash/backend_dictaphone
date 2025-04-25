package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.core.models.dtos.UserDto;

import java.util.List;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    /**
     * update user details
     * @param userDto the user details to update
     * @return the updated user details
     */
    UserDto updateUser(UserDto userDto);

    /**
     * delete user by id
     * @param id the user id to delete
     */
    void deleteUser(UUID id);

    /**
     * get all users
     * @return a list of all users
     */
    List<UserDto> getAllUsers();

    /**
     * add role to user
     * @param userId the user id to add role to
     * @param roleId the role id to add
     * @return userDto
     */
    UserDto addRoleToUser(UUID userId, UUID roleId);

    /**
     * remove role from user
     * @param userId the user id to remove role from
     * @param roleId the role id to remove
     * @return userDto
     */
    UserDto removeRoleFromUser(UUID userId, UUID roleId);

    /**
     * update user roles
     * @param userId the user id to update
     * @param roleIds the role ids to update
     * @return userDto
     */
    UserDto updateUserRoles(UUID userId, Set<UUID> roleIds);

    /**
     * get user roles
     * @param userId the user id to get roles for
     * @return a set of RoleDto
     */
    Set<RoleDto> getUserRoles(UUID userId);
}
