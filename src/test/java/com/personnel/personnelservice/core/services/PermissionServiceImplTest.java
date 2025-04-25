package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.adapters.persistances.mappers.PermissionMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPermissionRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.PermissionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le service de gestion des permissions.
 * Cette classe teste toutes les méthodes du service PermissionServiceImpl
 * en utilisant Mockito pour simuler les dépendances et Faker pour générer des données de test.
 */
@ExtendWith(MockitoExtension.class)
public class PermissionServiceImplTest {

    @Mock
    private JpaPermissionRepository jpaPermissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Faker faker;
    private UUID permissionId;
    private String permissionName;
    private String permissionDescription;
    private Permission permission;
    private PermissionDto permissionDto;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        permissionId = UUID.randomUUID();
        permissionName = faker.lorem().word().toUpperCase() + "_PERMISSION";
        permissionDescription = faker.lorem().sentence();

        // Configuration de l'entité Permission
        permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);
        permission.setDescription(permissionDescription);

        // Configuration du DTO Permission
        permissionDto = new PermissionDto();
        permissionDto.setId(permissionId);
        permissionDto.setName(permissionName);
        permissionDto.setDescription(permissionDescription);
    }

    /**
     * Tests pour la méthode getPermissionById
     */
    @Nested
    @DisplayName("Tests pour getPermissionById")
    class GetPermissionByIdTests {

        @Test
        @DisplayName("Devrait retourner une permission lorsque l'ID existe")
        void shouldReturnPermissionWhenIdExists() {
            // Arrange
            when(jpaPermissionRepository.findById(permissionId)).thenReturn(Optional.of(permission));
            when(permissionMapper.toPermissionDto(permission)).thenReturn(permissionDto);

            // Act
            PermissionDto result = permissionService.getPermissionById(permissionId);

            // Assert
            assertNotNull(result);
            assertEquals(permissionId, result.getId());
            assertEquals(permissionName, result.getName());
            assertEquals(permissionDescription, result.getDescription());

            verify(jpaPermissionRepository).findById(permissionId);
            verify(permissionMapper).toPermissionDto(permission);
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque l'ID n'existe pas")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            // Arrange
            when(jpaPermissionRepository.findById(permissionId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> permissionService.getPermissionById(permissionId));
            assertEquals("Permission not found with id: " + permissionId, exception.getMessage());

            verify(jpaPermissionRepository).findById(permissionId);
            verifyNoInteractions(permissionMapper);
        }
    }

    /**
     * Tests pour la méthode getAllPermissions
     */
    @Nested
    @DisplayName("Tests pour getAllPermissions")
    class GetAllPermissionsTests {

        @Test
        @DisplayName("Devrait retourner toutes les permissions")
        void shouldReturnAllPermissions() {
            // Arrange
            List<Permission> permissions = Arrays.asList(permission, createAdditionalPermission());
            List<PermissionDto> permissionDtos = permissions.stream()
                    .map(p -> {
                        PermissionDto dto = new PermissionDto();
                        dto.setId(p.getId());
                        dto.setName(p.getName());
                        dto.setDescription(p.getDescription());
                        return dto;
                    })
                    .collect(Collectors.toList());

            when(jpaPermissionRepository.findAll()).thenReturn(permissions);
            when(permissionMapper.toPermissionDto(any(Permission.class))).thenAnswer(invocation -> {
                Permission p = invocation.getArgument(0);
                return permissionDtos.stream()
                        .filter(dto -> dto.getId().equals(p.getId()))
                        .findFirst()
                        .orElse(null);
            });

            // Act
            List<PermissionDto> result = permissionService.getAllPermissions();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());

            // Vérification des valeurs par position
            assertThat(result).extracting(PermissionDto::getId)
                    .containsExactlyInAnyOrder(
                            permissions.get(0).getId(),
                            permissions.get(1).getId()
                    );

            assertThat(result).extracting(PermissionDto::getName)
                    .containsExactlyInAnyOrder(
                            permissions.get(0).getName(),
                            permissions.get(1).getName()
                    );

            verify(jpaPermissionRepository).findAll();
            verify(permissionMapper, times(2)).toPermissionDto(any(Permission.class));
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand aucune permission n'existe")
        void shouldReturnEmptyListWhenNoPermissionsExist() {
            // Arrange
            when(jpaPermissionRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<PermissionDto> result = permissionService.getAllPermissions();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(jpaPermissionRepository).findAll();
            verifyNoInteractions(permissionMapper);
        }
    }

    /**
     * Méthode utilitaire pour créer une permission supplémentaire pour les tests
     */
    private Permission createAdditionalPermission() {
        Permission additionalPermission = new Permission();
        additionalPermission.setId(UUID.randomUUID());
        additionalPermission.setName(faker.lorem().word().toUpperCase() + "_PERMISSION");
        additionalPermission.setDescription(faker.lorem().sentence());
        return additionalPermission;
    }
}