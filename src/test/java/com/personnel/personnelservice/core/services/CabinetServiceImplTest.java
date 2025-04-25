package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaCabinetRepository;
import com.personnel.personnelservice.core.exceptions.BaseException;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.PatientDto;
import com.personnel.personnelservice.adapters.persistances.mappers.PatientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CabinetServiceImplTest {

    @Mock
    private JpaCabinetRepository jpaCabinetRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private CabinetServiceImpl cabinetService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faker = new Faker();
    }

    /**
     * Teste la méthode searchPatients avec succès
     * en vérifiant que les bons paramètres sont passés aux méthodes mockées.
     */
    @Test
    void testSearchPatients_Success() {
        // Arrange
        UUID cabinetId = UUID.randomUUID();
        String query = faker.lorem().word();
        Pageable pageable = Pageable.unpaged();

        // Créer des entités patient simulées avec Faker
        Patient patient1 = new Patient();
        patient1.setId(UUID.randomUUID());
        patient1.setFirstName(faker.name().firstName());
        patient1.setLastName(faker.name().lastName());

        Patient patient2 = new Patient();
        patient2.setId(UUID.randomUUID());
        patient2.setFirstName(faker.name().firstName());
        patient2.setLastName(faker.name().lastName());

        List<Patient> patients = Arrays.asList(patient1, patient2);
        Page<Patient> patientPage = new PageImpl<>(patients);

        // Simuler le comportement de JpaCabinetRepository
        when(jpaCabinetRepository.existsById(cabinetId)).thenReturn(true);
        when(jpaCabinetRepository.searchPatientsByCabinetId(eq(cabinetId), anyString(), eq(pageable)))
                .thenReturn(patientPage);

        // Simuler le comportement de PatientMapper
        PatientDto patientDto1 = new PatientDto();
        PatientDto patientDto2 = new PatientDto();

        when(patientMapper.toDTO(patient1)).thenReturn(patientDto1);
        when(patientMapper.toDTO(patient2)).thenReturn(patientDto2);

        // Act
        Page<PatientDto> result = cabinetService.searchPatients(cabinetId, query, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(jpaCabinetRepository, times(1)).searchPatientsByCabinetId(eq(cabinetId), anyString(), eq(pageable));
    }

    /**
     * Teste la méthode searchPatients avec un cabinet inexistant
     */
    @Test
    void testSearchPatients_CabinetNotFound() {
        // Arrange
        UUID cabinetId = UUID.randomUUID();
        String query = faker.lorem().word();
        Pageable pageable = Pageable.unpaged();

        // Simuler que le cabinet n'existe pas
        when(jpaCabinetRepository.existsById(cabinetId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                cabinetService.searchPatients(cabinetId, query, pageable));
        String mssg = exception.getMessage();
        assertEquals("Cabinet with id "+ cabinetId +" not found", exception.getMessage());
    }

    /**
     * Teste la méthode searchPatients avec une exception
     */
    @Test
    void testSearchPatients_Exception() {
        // Arrange
        UUID cabinetId = UUID.randomUUID();
        String query = faker.lorem().word();
        Pageable pageable = Pageable.unpaged();

        // Simuler une exception générale lors de la recherche des patients
        when(jpaCabinetRepository.existsById(cabinetId)).thenReturn(true);
        when(jpaCabinetRepository.searchPatientsByCabinetId(eq(cabinetId), anyString(), eq(pageable)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        BaseException exception = assertThrows(BaseException.class, () ->
                cabinetService.searchPatients(cabinetId, query, pageable));
        assertEquals("Error while searching patients: Database error", exception.getMessage());
    }
}
