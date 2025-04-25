package com.personnel.personnelservice.config;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.adapters.persistances.entities.Role;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPermissionRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaRoleRepository;
import com.personnel.personnelservice.core.models.enums.PermissionEnum;
import com.personnel.personnelservice.core.models.enums.RoleEnum;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Configuration permettant l'initialisation et la mise à jour des permissions et des rôles dans la base de données.
 */
@Configuration
public class DataInitializerConfig {

    /**
     * Initialise et met à jour les permissions et les rôles au démarrage de l'application.
     *
     * @param jpaPermissionRepository Repository pour les permissions.
     * @param jpaRoleRepository Repository pour les rôles.
     * @return CommandLineRunner exécutant l'initialisation.
     */
    @Bean
    @Transactional
    public CommandLineRunner initializePermissionsAndRoles(
            JpaPermissionRepository jpaPermissionRepository,
            JpaRoleRepository jpaRoleRepository) {

        return args -> {
            initializePermissions(jpaPermissionRepository);
            initializeOrUpdateRoles(jpaRoleRepository, jpaPermissionRepository);
        };
    }

    /**
     * Initialise les permissions en base de données si elles n'existent pas encore.
     *
     * @param jpaPermissionRepository Repository pour la gestion des permissions.
     */
    private void initializePermissions(JpaPermissionRepository jpaPermissionRepository) {
        Set<String> existingPermissionNames = jpaPermissionRepository.findAll().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        List<Permission> newPermissions = RolePermissionsConfig.getRolePermissionsMap().values().stream()
                .flatMap(EnumSet::stream)
                .distinct()
                .filter(permissionEnum -> !existingPermissionNames.contains(permissionEnum.toString()))
                .map(Permission::new)
                .collect(Collectors.toList());

        if (!newPermissions.isEmpty()) {
            jpaPermissionRepository.saveAll(newPermissions);
            System.out.println("✅ Permissions mises à jour : " + newPermissions.size() + " nouvelles permissions ajoutées.");
        }
    }

    /**
     * Initialise les rôles s'ils n'existent pas et met à jour les permissions des rôles existants.
     *
     * @param jpaRoleRepository Repository pour la gestion des rôles.
     * @param jpaPermissionRepository Repository pour la gestion des permissions.
     */
    private void initializeOrUpdateRoles(JpaRoleRepository jpaRoleRepository, JpaPermissionRepository jpaPermissionRepository) {
        Map<RoleEnum, EnumSet<PermissionEnum>> rolePermissionsMap = RolePermissionsConfig.getRolePermissionsMap();

        for (RoleEnum roleEnum : RoleEnum.values()) {
            jpaRoleRepository.findByName(roleEnum.name()).ifPresentOrElse(role -> {
                // 🔄 Mettre à jour les permissions du rôle existant
                updateRolePermissions(role, rolePermissionsMap.getOrDefault(roleEnum, EnumSet.noneOf(PermissionEnum.class)), jpaPermissionRepository);
                jpaRoleRepository.save(role);
                System.out.println("🔄 Rôle " + roleEnum.name() + " mis à jour avec " + role.getPermissions().size() + " permissions.");
            }, () -> {
                // ➕ Créer un nouveau rôle si inexistant
                Role newRole = new Role();
                newRole.setName(roleEnum.name());

                Set<Permission> permissions = getPermissionsForRole(rolePermissionsMap, roleEnum, jpaPermissionRepository);
                newRole.setPermissions(permissions);

                jpaRoleRepository.save(newRole);
                System.out.println("✅ Rôle " + roleEnum.name() + " ajouté avec " + permissions.size() + " permissions.");
            });
        }
    }

    /**
     * Met à jour les permissions d'un rôle existant en ajoutant les nouvelles permissions et en supprimant les anciennes.
     *
     * @param role Le rôle à mettre à jour.
     * @param assignedPermissionEnums Ensemble des permissions assignées à ce rôle.
     * @param jpaPermissionRepository Repository des permissions.
     */
    private void updateRolePermissions(Role role, EnumSet<PermissionEnum> assignedPermissionEnums, JpaPermissionRepository jpaPermissionRepository) {
        Set<String> assignedPermissionNames = assignedPermissionEnums.stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());

        Set<Permission> updatedPermissions = jpaPermissionRepository.findAll().stream()
                .filter(permission -> assignedPermissionNames.contains(permission.getName()))
                .collect(Collectors.toSet());

        role.setPermissions(updatedPermissions);
    }

    /**
     * Récupère les permissions correspondant à un rôle à partir du repository.
     *
     * @param rolePermissionsMap La map des rôles et permissions.
     * @param roleEnum Le rôle pour lequel récupérer les permissions.
     * @param jpaPermissionRepository Repository des permissions.
     * @return Un ensemble de permissions associées au rôle.
     */
    private Set<Permission> getPermissionsForRole(Map<RoleEnum, EnumSet<PermissionEnum>> rolePermissionsMap, RoleEnum roleEnum, JpaPermissionRepository jpaPermissionRepository) {
        Set<String> permissionNames = rolePermissionsMap.getOrDefault(roleEnum, EnumSet.noneOf(PermissionEnum.class))
                .stream().map(Enum::toString)
                .collect(Collectors.toSet());

        return jpaPermissionRepository.findAll().stream()
                .filter(permission -> permissionNames.contains(permission.getName()))
                .collect(Collectors.toSet());
    }
}