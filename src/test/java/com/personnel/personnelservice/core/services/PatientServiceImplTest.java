package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.core.services.PatientServiceImpl;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.core.models.dtos.PatientDto;
import com.personnel.personnelservice.adapters.persistances.mappers.PatientMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPatientRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {
    @InjectMocks
    private PatientServiceImpl patientService;
    @Mock
    private JpaPatientRepository jpaPatientRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private PatientMapper patientMapper;
    private Faker faker;
    @BeforeEach
    void setUp() {
        faker = new Faker();
    }
    @Test
    void updatePatient() {
        UUID userId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(userId);
        patient.setLastName(faker.name().lastName());
        patient.setFirstName(faker.name().firstName());
        patient.setEmail(faker.internet().emailAddress());
        patient.setPassword(passwordEncoder.encode("oldPassword"));

        PatientDto patientDto = new PatientDto();
        patientDto.setId(userId);
        patientDto.setLastName(faker.name().lastName());
        patientDto.setFirstName(faker.name().firstName());
        patientDto.setEmail(faker.internet().emailAddress());
        patientDto.setPassword("newPassword");

        // Simulation des comportements
        when(jpaPatientRepository.findById(userId)).thenReturn(Optional.of(patient));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(patientMapper.toDTO(any(Patient.class))).thenReturn(patientDto);

        PatientDto updatedUser = patientService.updatePatient(patientDto);

        assertNotNull(updatedUser);
        verify(jpaPatientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void deletePatient() {
        UUID userId = UUID.randomUUID();

        when(jpaPatientRepository.existsById(userId)).thenReturn(true);
        doNothing().when(jpaPatientRepository).deleteById(userId);

        patientService.deletePatient(userId);
        verify(jpaPatientRepository, times(1)).deleteById(userId);
    }

    @Test
    void getAllPatient() {
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Patient patient = new Patient();
            patient.setId(UUID.randomUUID());
            patient.setLastName(faker.name().lastName());
            patient.setFirstName(faker.name().firstName());
            patients.add(patient);
        }

        when(jpaPatientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toDTO(any(Patient.class))).thenReturn(new PatientDto());

        List<PatientDto> patientDtos = patientService.getAllPatient();

        assertFalse(patientDtos.isEmpty());
        assertEquals(3, patientDtos.size());
        verify(jpaPatientRepository, times(1)).findAll();
    }

    @Test
    void getPatientCreatedBy() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        String username = faker.internet().emailAddress();
        when(authentication.getName()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        Patient patient1 = new Patient();
        patient1.setId(UUID.randomUUID());
        patient1.setLastName(faker.name().fullName());
        patient1.setCreatedBy(username);
        Patient patient2 = new Patient();
        patient2.setId(UUID.randomUUID());
        patient2.setFirstName(faker.name().fullName());
        patient2.setCreatedBy(username);

        List<Patient> expectedPatients = Arrays.asList(patient1, patient2);
        when(jpaPatientRepository.findPatientByCreatedBy(username)).thenReturn(expectedPatients);

        List<Patient> result = patientService.getPatientCreatedBy();

        assertEquals(2, result.size());
        assertEquals(username, result.get(0).getCreatedBy());
        assertEquals(username, result.get(1).getCreatedBy());

        verify(jpaPatientRepository, times(1)).findPatientByCreatedBy(username);
    }
}