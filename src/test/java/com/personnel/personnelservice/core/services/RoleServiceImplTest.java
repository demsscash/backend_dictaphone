package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.adapters.persistances.entities.Role;
import com.personnel.personnelservice.adapters.persistances.mappers.RoleMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPermissionRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaRoleRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.RoleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le service de gestion des rôles.
 * Cette classe teste toutes les méthodes du service RoleServiceImpl
 * en utilisant Mockito pour simuler les dépendances et Faker pour générer des données de test.
 */
@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private JpaRoleRepository jpaRoleRepository;

    @Mock
    private JpaPermissionRepository jpaPermissionRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Faker faker;
    private UUID roleId;
    private String roleName;
    private String roleDescription;
    private Role role;
    private RoleDto roleDto;
    private Set<String> permissionNames;
    private Set<Permission> permissions;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        roleId = UUID.randomUUID();
        roleName = faker.company().profession() + " Role";
        roleDescription = faker.lorem().sentence();

        // Création d'un ensemble de noms de permissions
        permissionNames = new HashSet<>();
        permissionNames.add("READ");
        permissionNames.add("WRITE");

        // Création d'un ensemble de permissions
        permissions = new HashSet<>();
        for (String name : permissionNames) {
            Permission permission = new Permission();
            permission.setId(UUID.randomUUID());
            permission.setName(name);
            permissions.add(permission);
        }

        // Configuration de l'entité Role
        role = new Role();
        role.setId(roleId);
        role.setName(roleName);
        role.setDescription(roleDescription);
        role.setPermissions(permissions);

        // Configuration du DTO Role
        roleDto = new RoleDto();
        roleDto.setId(roleId);
        roleDto.setName(roleName);
        roleDto.setDescription(roleDescription);
        roleDto.setPermissions(permissionNames);
    }

    /**
     * Tests pour la méthode getRoleById
     */
    @Nested
    @DisplayName("Tests pour getRoleById")
    class GetRoleByIdTests {

        @Test
        @DisplayName("Devrait retourner un rôle lorsque l'ID existe")
        void shouldReturnRoleWhenIdExists() {
            // Arrange
            when(jpaRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
            when(roleMapper.toRoleDto(role)).thenReturn(roleDto);

            // Act
            RoleDto result = roleService.getRoleById(roleId);

            // Assert
            assertNotNull(result);
            assertEquals(roleId, result.getId());
            assertEquals(roleName, result.getName());
            assertEquals(roleDescription, result.getDescription());
            assertEquals(permissionNames, result.getPermissions());

            verify(jpaRoleRepository).findById(roleId);
            verify(roleMapper).toRoleDto(role);
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque l'ID n'existe pas")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            // Arrange
            when(jpaRoleRepository.findById(roleId)).thenReturn(Optional.empty());

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.getRoleById(roleId));
            assertEquals("Role not found with id: " + roleId, exception.getMessage());

            verify(jpaRoleRepository).findById(roleId);
            verifyNoInteractions(roleMapper);
        }
    }

    /**
     * Tests pour la méthode getAllRoles
     */
    @Nested
    @DisplayName("Tests pour getAllRoles")
    class GetAllRolesTests {

        @Test
        @DisplayName("Devrait retourner tous les rôles")
        void shouldReturnAllRoles() {
            // Arrange
            List<Role> roles = Arrays.asList(role, createAdditionalRole());
            List<RoleDto> roleDtos = roles.stream()
                    .map(r -> {
                        RoleDto dto = new RoleDto();
                        dto.setId(r.getId());
                        dto.setName(r.getName());
                        return dto;
                    })
                    .collect(Collectors.toList());

            when(jpaRoleRepository.findAll()).thenReturn(roles);
            when(roleMapper.toRoleDto(any(Role.class))).thenAnswer(invocation -> {
                Role r = invocation.getArgument(0);
                return roleDtos.stream()
                        .filter(dto -> dto.getId().equals(r.getId()))
                        .findFirst()
                        .orElse(null);
            });

            // Act
            List<RoleDto> result = roleService.getAllRoles();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());

            verify(jpaRoleRepository).findAll();
            verify(roleMapper, times(2)).toRoleDto(any(Role.class));
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand aucun rôle n'existe")
        void shouldReturnEmptyListWhenNoRolesExist() {
            // Arrange
            when(jpaRoleRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<RoleDto> result = roleService.getAllRoles();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(jpaRoleRepository).findAll();
            verifyNoInteractions(roleMapper);
        }
    }

    /**
     * Tests pour la méthode createRole
     */
    @Nested
    @DisplayName("Tests pour createRole")
    class CreateRoleTests {

        @Test
        @DisplayName("Devrait créer un rôle avec permissions")
        void shouldCreateRoleWithPermissions() {
            // Arrange
            for (String permName : permissionNames) {
                Permission perm = permissions.stream()
                        .filter(p -> p.getName().equals(permName))
                        .findFirst()
                        .orElse(null);
                when(jpaPermissionRepository.findByName(permName)).thenReturn(Optional.ofNullable(perm));
            }

            when(roleMapper.toEntity(roleDto)).thenReturn(role);
            when(jpaRoleRepository.save(role)).thenReturn(role);
            when(roleMapper.toRoleDto(role)).thenReturn(roleDto);

            // Act
            RoleDto result = roleService.createRole(roleDto);

            // Assert
            assertNotNull(result);
            assertEquals(roleId, result.getId());
            assertEquals(roleName, result.getName());
            assertEquals(permissionNames, result.getPermissions());

            verify(roleMapper).toEntity(roleDto);
            verify(jpaRoleRepository).save(role);
            verify(roleMapper).toRoleDto(role);
            for (String permName : permissionNames) {
                verify(jpaPermissionRepository).findByName(permName);
            }
        }

        @Test
        @DisplayName("Devrait créer un rôle sans permissions")
        void shouldCreateRoleWithoutPermissions() {
            // Arrange
            RoleDto roleWithoutPermissions = new RoleDto();
            roleWithoutPermissions.setName(roleName);
            roleWithoutPermissions.setDescription(roleDescription);
            roleWithoutPermissions.setPermissions(Collections.emptySet());

            Role roleEntity = new Role();
            roleEntity.setName(roleName);
            roleEntity.setDescription(roleDescription);

            when(roleMapper.toEntity(roleWithoutPermissions)).thenReturn(roleEntity);
            when(jpaRoleRepository.save(roleEntity)).thenReturn(roleEntity);
            when(roleMapper.toRoleDto(roleEntity)).thenReturn(roleWithoutPermissions);

            // Act
            RoleDto result = roleService.createRole(roleWithoutPermissions);

            // Assert
            assertNotNull(result);
            assertEquals(roleName, result.getName());
            assertEquals(roleDescription, result.getDescription());

            verify(roleMapper).toEntity(roleWithoutPermissions);
            verify(jpaRoleRepository).save(roleEntity);
            verify(roleMapper).toRoleDto(roleEntity);
            verifyNoInteractions(jpaPermissionRepository);
        }
    }

    /**
     * Tests pour la méthode updateRole
     */
    @Nested
    @DisplayName("Tests pour updateRole")
    class UpdateRoleTests {

        @Test
        @DisplayName("Devrait mettre à jour un rôle existant")
        void shouldUpdateExistingRole() {
            // Arrange
            String updatedName = faker.company().profession() + " Updated";
            String updatedDescription = faker.lorem().sentence();
            Set<String> updatedPermissions = new HashSet<>();
            updatedPermissions.add("READ");

            RoleDto updateRequest = new RoleDto();
            updateRequest.setName(updatedName);
            updateRequest.setDescription(updatedDescription);
            updateRequest.setPermissions(updatedPermissions);

            Permission readPermission = new Permission();
            readPermission.setName("READ");
            Set<Permission> updatedPermissionEntities = Collections.singleton(readPermission);

            when(jpaRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
            when(jpaPermissionRepository.findByName("READ")).thenReturn(Optional.of(readPermission));

            Role updatedRole = new Role();
            updatedRole.setId(roleId);
            updatedRole.setName(updatedName);
            updatedRole.setDescription(updatedDescription);
            updatedRole.setPermissions(updatedPermissionEntities);

            when(jpaRoleRepository.save(any(Role.class))).thenReturn(updatedRole);

            RoleDto updatedRoleDto = new RoleDto();
            updatedRoleDto.setId(roleId);
            updatedRoleDto.setName(updatedName);
            updatedRoleDto.setDescription(updatedDescription);
            updatedRoleDto.setPermissions(updatedPermissions);

            when(roleMapper.toRoleDto(updatedRole)).thenReturn(updatedRoleDto);

            // Act
            RoleDto result = roleService.updateRole(roleId, updateRequest);

            // Assert
            assertNotNull(result);
            assertEquals(roleId, result.getId());
            assertEquals(updatedName, result.getName());
            assertEquals(updatedDescription, result.getDescription());
            assertEquals(updatedPermissions, result.getPermissions());

            // Capture et vérification des mises à jour
            ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
            verify(jpaRoleRepository).save(roleCaptor.capture());
            Role capturedRole = roleCaptor.getValue();

            assertEquals(updatedName, capturedRole.getName());
            assertEquals(updatedDescription, capturedRole.getDescription());
            assertNotNull(capturedRole.getPermissions());
            assertEquals(1, capturedRole.getPermissions().size());
            assertEquals("READ", capturedRole.getPermissions().iterator().next().getName());
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque le rôle n'existe pas")
        void shouldThrowExceptionWhenRoleDoesNotExist() {
            // Arrange
            when(jpaRoleRepository.findById(roleId)).thenReturn(Optional.empty());

            RoleDto updateRequest = new RoleDto();
            updateRequest.setName(faker.company().profession());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> roleService.updateRole(roleId, updateRequest));
            assertEquals("Role not found with id: " + roleId, exception.getMessage());

            verify(jpaRoleRepository).findById(roleId);
            verify(jpaRoleRepository, never()).save(any(Role.class));
        }
    }

    /**
     * Tests pour la méthode deleteRole
     */
    @Nested
    @DisplayName("Tests pour deleteRole")
    class DeleteRoleTests {

        @Test
        @DisplayName("Devrait supprimer un rôle existant")
        void shouldDeleteExistingRole() {
            // Arrange
            when(jpaRoleRepository.existsById(roleId)).thenReturn(true);
            doNothing().when(jpaRoleRepository).deleteById(roleId);

            // Act
            roleService.deleteRole(roleId);

            // Assert
            verify(jpaRoleRepository).existsById(roleId);
            verify(jpaRoleRepository).deleteById(roleId);
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque le rôle n'existe pas")
        void shouldThrowExceptionWhenRoleDoesNotExist() {
            // Arrange
            when(jpaRoleRepository.existsById(roleId)).thenReturn(false);

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.deleteRole(roleId));
            assertEquals("Role not found with id: " + roleId, exception.getMessage());

            verify(jpaRoleRepository).existsById(roleId);
            verify(jpaRoleRepository, never()).deleteById(any());
        }
    }

    /**
     * Tests pour la méthode addPermissionsToRole
     */
    @Nested
    @DisplayName("Tests pour addPermissionsToRole")
    class AddPermissionsToRoleTests {

        @Test
        @DisplayName("Devrait ajouter des permissions à un rôle")
        void shouldAddPermissionsToRole() {
            // Arrange
            Set<UUID> permissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toSet());

            when(jpaRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
            when(jpaPermissionRepository.findAllById(permissionIds)).thenReturn(new ArrayList<>(permissions));
            when(jpaRoleRepository.save(role)).thenReturn(role);
            when(roleMapper.toRoleDto(role)).thenReturn(roleDto);

            // Act
            RoleDto result = roleService.addPermissionsToRole(roleId, permissionIds);

            // Assert
            assertNotNull(result);
            assertEquals(roleDto.getPermissions(), result.getPermissions());

            verify(jpaRoleRepository).findById(roleId);
            verify(jpaPermissionRepository).findAllById(permissionIds);
            verify(jpaRoleRepository).save(role);
            verify(roleMapper).toRoleDto(role);
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque le rôle n'existe pas")
        void shouldThrowExceptionWhenRoleDoesNotExist() {
            // Arrange
            Set<UUID> permissionIds = Collections.singleton(UUID.randomUUID());
            when(jpaRoleRepository.findById(roleId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> roleService.addPermissionsToRole(roleId, permissionIds));
            assertEquals("Role not found", exception.getMessage());

            verify(jpaRoleRepository).findById(roleId);
            verifyNoInteractions(jpaPermissionRepository);
        }
    }

    /**
     * Tests pour la méthode getRoleByName
     */
    @Nested
    @DisplayName("Tests pour getRoleByName")
    class GetRoleByNameTests {

        @Test
        @DisplayName("Devrait retourner un rôle lorsque le nom existe")
        void shouldReturnRoleWhenNameExists() {
            // Arrange
            String roleName = "PATIENT";
            when(jpaRoleRepository.findByName(roleName)).thenReturn(Optional.of(role));
            when(roleMapper.toRoleDto(role)).thenReturn(roleDto);

            // Act
            RoleDto result = roleService.getRoleByName(roleName);

            // Assert
            assertNotNull(result);
            assertEquals(roleDto.getId(), result.getId());
            assertEquals(roleDto.getName(), result.getName());

            verify(jpaRoleRepository).findByName(roleName);
            verify(roleMapper).toRoleDto(role);
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque le nom n'existe pas")
        void shouldThrowExceptionWhenNameDoesNotExist() {
            // Arrange
            String nonExistingRoleName = "NONEXISTING";
            when(jpaRoleRepository.findByName(nonExistingRoleName)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> roleService.getRoleByName(nonExistingRoleName));
            assertEquals("Role not found", exception.getMessage());

            verify(jpaRoleRepository).findByName(nonExistingRoleName);
            verifyNoInteractions(roleMapper);
        }
    }

    /**
     * Méthode utilitaire pour créer un rôle supplémentaire pour les tests
     */
    private Role createAdditionalRole() {
        Role additionalRole = new Role();
        additionalRole.setId(UUID.randomUUID());
        additionalRole.setName(faker.company().profession() + " Additional Role");
        additionalRole.setDescription(faker.lorem().sentence());
        return additionalRole;
    }
}