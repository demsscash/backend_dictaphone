package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.adapters.persistances.entities.Assistant;
import com.personnel.personnelservice.adapters.persistances.mappers.AssistantMapper;
import com.personnel.personnelservice.core.ports.repositories.AssistantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AssistantServiceImplTest {
    @Mock
    private AssistantRepository jpaAssistantRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AssistantMapper assistantMapper;
    @InjectMocks
    private AssistantServiceImpl assistantService;
    private Faker faker;
    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void updateAssistant() {
        UUID assistantId = UUID.randomUUID();
        AssistantDto assistantDto = new AssistantDto();
        assistantDto.setId(assistantId);

        Assistant assistant = new Assistant();
        assistant.setId(assistantId);

        // When
        when(jpaAssistantRepository.findById(assistantId)).thenReturn(Optional.of(assistant));
        doNothing().when(assistantMapper).updateEntity(assistantDto, assistant);
        when(assistantMapper.toDTO(assistant)).thenReturn(assistantDto);

        AssistantDto updatedAssistant = assistantService.updateAssistant(assistantDto);

        // Then
        assertNotNull(updatedAssistant);
        assertEquals(assistantDto.getId(), updatedAssistant.getId());

        verify(jpaAssistantRepository).findById(assistantId);
        verify(assistantMapper).updateEntity(assistantDto, assistant);
        verify(assistantMapper).toDTO(assistant);
    }

    @Test
    void deleteAssistant() {
        UUID assistantId = UUID.randomUUID();

        when(jpaAssistantRepository.existsById(assistantId)).thenReturn(true);
        doNothing().when(jpaAssistantRepository).deleteById(assistantId);

        assistantService.deleteAssistant(assistantId);
        verify(jpaAssistantRepository, times(1)).deleteById(assistantId);
    }

    @Test
    void getAllAssistants() {
        List<Assistant> assistants = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Assistant assistant = new Assistant();
            assistant.setId(UUID.randomUUID());
            assistant.setLastName(faker.name().lastName());
            assistant.setFirstName(faker.name().firstName());
            assistants.add(assistant);
        }

        when(jpaAssistantRepository.findAll()).thenReturn(assistants);
        when(assistantMapper.toDTO(any(Assistant.class))).thenReturn(new AssistantDto());

        List<AssistantDto> assistantDtos = assistantService.getAllAssistants();

        assertFalse(assistantDtos.isEmpty());
        assertEquals(3, assistantDtos.size());
        verify(jpaAssistantRepository, times(1)).findAll();
    }

    @Test
    void getAssistantsByMedecin() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(faker.internet().emailAddress());
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String createdBy = authentication.getName();
        List<Assistant> assistants = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Assistant assistant = new Assistant();
            assistant.setId(UUID.randomUUID());
            assistant.setLastName(faker.name().lastName());
            assistant.setFirstName(faker.name().firstName());
            assistant.setEmail(faker.internet().emailAddress());
            assistant.setCreatedBy(createdBy);
            assistants.add(assistant);
        }
        when(jpaAssistantRepository.findAssistantByCreatedBy(createdBy)).thenReturn(assistants);
        List<Assistant> result = assistantService.getAssistantsByMedecin();
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(jpaAssistantRepository, times(1)).findAssistantByCreatedBy(createdBy);
    }
}