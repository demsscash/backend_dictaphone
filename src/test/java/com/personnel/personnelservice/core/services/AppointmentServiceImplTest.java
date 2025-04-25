package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.adapters.persistances.entities.Appointment;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.adapters.persistances.mappers.AppointmentMapper;
import com.personnel.personnelservice.core.exceptions.BaseException;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.AppointmentDto;
import com.personnel.personnelservice.core.models.enums.PeriodEnum;
import com.personnel.personnelservice.core.ports.repositories.AppointmentRepository;
import com.personnel.personnelservice.core.ports.repositories.CabinetRepository;
import com.personnel.personnelservice.core.ports.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private CabinetRepository cabinetRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Faker faker;
    private UUID appointmentId;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        appointmentId = UUID.randomUUID();
    }

    /**
     * Tests pour la méthode getAllAppointments
     */
    @Nested
    @DisplayName("Tests pour getAllAppointments")
    class GetAllAppointmentsTests {

        @Test
        @DisplayName("Devrait retourner une liste de rendez-vous lorsque le cabinet existe")
        void shouldReturnAppointmentsWhenCabinetExists() {
            // Arrange
            UUID cabinetId = UUID.randomUUID();
            PeriodEnum period = PeriodEnum.DAILY;
            LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);

            Appointment appointment = new Appointment();
            appointment.setId(UUID.randomUUID());
            appointment.setDateTime(startDate.plusHours(1));

            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointment.getId());
            appointmentDto.setDateTime(appointment.getDateTime());

            Page<Appointment> appointmentPage = new PageImpl<>(Collections.singletonList(appointment));

            when(cabinetRepository.existsById(cabinetId)).thenReturn(true);
            when(appointmentRepository.findByCabinetIdAndDateTimeBetween(cabinetId, startDate, endDate, pageable))
                    .thenReturn(appointmentPage);
            when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

            // Act
            List<AppointmentDto> result = appointmentService.getAllAppointments(cabinetId, period, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(appointmentDto, result.get(0));

            verify(cabinetRepository, times(1)).existsById(cabinetId);
            verify(appointmentRepository, times(1))
                    .findByCabinetIdAndDateTimeBetween(cabinetId, startDate, endDate, pageable);
            verify(appointmentMapper, times(1)).toDto(appointment);
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque le cabinet n'existe pas")
        void shouldThrowExceptionWhenCabinetDoesNotExist() {
            // Arrange
            UUID cabinetId = UUID.randomUUID();
            PeriodEnum period = PeriodEnum.DAILY;

            when(cabinetRepository.existsById(cabinetId)).thenReturn(false);

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                appointmentService.getAllAppointments(cabinetId, period, pageable);
            });
            assertEquals("Cabinet with id " + cabinetId + " not found", exception.getMessage());

            verify(cabinetRepository, times(1)).existsById(cabinetId);
            verifyNoInteractions(appointmentRepository);
            verifyNoInteractions(appointmentMapper);
        }

        @Test
        @DisplayName("Devrait retourner une liste vide lorsque aucun rendez-vous n'existe pour le cabinet")
        void shouldReturnEmptyListWhenNoAppointmentsExist() {
            // Arrange
            UUID cabinetId = UUID.randomUUID();
            PeriodEnum period = PeriodEnum.DAILY;
            LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);

            Page<Appointment> emptyAppointmentPage = new PageImpl<>(Collections.emptyList());

            when(cabinetRepository.existsById(cabinetId)).thenReturn(true);
            when(appointmentRepository.findByCabinetIdAndDateTimeBetween(cabinetId, startDate, endDate, pageable))
                    .thenReturn(emptyAppointmentPage);

            // Act
            List<AppointmentDto> result = appointmentService.getAllAppointments(cabinetId, period, pageable);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(cabinetRepository, times(1)).existsById(cabinetId);
            verify(appointmentRepository, times(1))
                    .findByCabinetIdAndDateTimeBetween(cabinetId, startDate, endDate, pageable);
            verifyNoInteractions(appointmentMapper);
        }
    }

    /**
     * Tests pour la méthode deleteAppointment
     */
    @Nested
    @DisplayName("Tests pour deleteAppointment")
    class DeleteAppointmentTests {

        @Test
        @DisplayName("Devrait supprimer un rendez-vous existant")
        void shouldDeleteExistingAppointment() {
            // Arrange
            when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
            doNothing().when(appointmentRepository).deleteById(appointmentId);

            // Act
            appointmentService.deleteAppointment(appointmentId);

            // Assert
            verify(appointmentRepository).existsById(appointmentId);
            verify(appointmentRepository).deleteById(appointmentId);
        }

        @Test
        @DisplayName("Devrait lancer une exception lorsque le rendez-vous n'existe pas")
        void shouldThrowExceptionWhenAppointmentDoesNotExist() {
            // Arrange
            when(appointmentRepository.existsById(appointmentId)).thenReturn(false);

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                appointmentService.deleteAppointment(appointmentId);
            });
            assertEquals("Appointment with id " + appointmentId + " not found", exception.getMessage());

            verify(appointmentRepository).existsById(appointmentId);
            verify(appointmentRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Devrait lancer une exception en cas d'erreur interne lors de la suppression")
        void shouldThrowExceptionWhenInternalErrorOccurs() {
            // Arrange
            when(appointmentRepository.existsById(appointmentId)).thenReturn(true);
            doThrow(new RuntimeException("Erreur interne")).when(appointmentRepository).deleteById(appointmentId);

            // Act & Assert
            BaseException exception = assertThrows(BaseException.class, () -> {
                appointmentService.deleteAppointment(appointmentId);
            });
            assertEquals("Erreur interne lors de la suppression de l'appointment", exception.getMessage());

            verify(appointmentRepository).existsById(appointmentId);
            verify(appointmentRepository).deleteById(appointmentId);
        }
    }


    /**
     * Tests pour la méthode createAppointment
     */
    @Nested
    @DisplayName("Tests pour createAppointment")
    class CreateAppointmentTests {

        @Test
        @DisplayName("Devrait créer un rendez-vous valide et retourner l'AppointmentDto correspondant")
        void createAppointment_ValidAppointment_ReturnsSavedAppointmentDto() {
            // Arrange
            UUID patientId = UUID.randomUUID();
            UUID appointmentId = UUID.randomUUID();
            LocalDateTime appointmentDateTime = LocalDateTime.now().plusDays(1);

            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointmentId);
            appointmentDto.setDateTime(appointmentDateTime);

            Appointment appointment = new Appointment();
            appointment.setId(appointmentId);
            appointment.setDateTime(appointmentDateTime);
            appointment.setPatient(new Patient());
            appointment.getPatient().setId(patientId);

            Patient patient = new Patient();
            patient.setId(patientId);

            when(appointmentMapper.toEntity(appointmentDto)).thenReturn(appointment);
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
            when(appointmentRepository.save(appointment)).thenReturn(appointment);
            when(appointmentMapper.toDto(appointment)).thenReturn(appointmentDto);

            // Act
            AppointmentDto result = appointmentService.createAppointment(appointmentDto);

            // Assert
            assertNotNull(result);
            assertEquals(appointmentId, result.getId());
            assertEquals(appointmentDateTime, result.getDateTime());

            verify(appointmentMapper, times(1)).toEntity(appointmentDto);
            verify(patientRepository, times(1)).findById(patientId);
            verify(appointmentRepository, times(1)).save(appointment);
            verify(appointmentMapper, times(1)).toDto(appointment);
        }

        @Test
        @DisplayName("Devrait lever une exception si le patient n'est pas trouvé")
        void createAppointment_PatientNotFound_ThrowsEntityNotFoundException() {
            // Arrange
            UUID patientId = UUID.randomUUID();
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setPatientId(patientId);

            Appointment appointment = new Appointment();
            appointment.setPatient(new Patient());
            appointment.getPatient().setId(patientId);

            when(appointmentMapper.toEntity(appointmentDto)).thenReturn(appointment);
            when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> appointmentService.createAppointment(appointmentDto));

            verify(appointmentMapper, times(1)).toEntity(appointmentDto);
            verify(patientRepository, times(1)).findById(patientId);
            verify(appointmentRepository, never()).save(any(Appointment.class));
            verify(appointmentMapper, never()).toDto(any(Appointment.class));
        }
    }
    @Nested
    @DisplayName("Tests pour updateAppointment")
    class UpdateAppointmentTests {

        @Test
        @DisplayName("Devrait mettre à jour un rendez-vous valide et retourner l'AppointmentDto correspondant")
        void updateAppointment_ValidAppointment_ReturnsUpdatedAppointmentDto() {
            // Arrange
            UUID appointmentId = UUID.randomUUID();
            UUID patientId = UUID.randomUUID();
            LocalDateTime appointmentDateTime = LocalDateTime.now().plusDays(1);

            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointmentId);
            appointmentDto.setDateTime(appointmentDateTime);
            appointmentDto.setPatientId(patientId);
            appointmentDto.setSubject("Updated Subject");

            Appointment existingAppointment = new Appointment();
            existingAppointment.setId(appointmentId);
            existingAppointment.setDateTime(LocalDateTime.now());
            existingAppointment.setSubject("Original Subject");

            Patient patient = new Patient();
            patient.setId(patientId);

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));

            doAnswer(invocation -> {
                Appointment appointment = invocation.getArgument(1);
                appointment.setDateTime(appointmentDateTime);
                appointment.setSubject("Updated Subject");
                Patient p = new Patient();
                p.setId(patientId);
                appointment.setPatient(p);
                return appointment;
            }).when(appointmentMapper).updateEntity(eq(appointmentDto), any(Appointment.class));

            when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
            when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(appointmentDto);

            AppointmentDto result = appointmentService.updateAppointment(appointmentDto);

            assertNotNull(result);
            assertEquals(appointmentId, result.getId());
            assertEquals(appointmentDateTime, result.getDateTime());
            assertEquals("Updated Subject", result.getSubject());

            verify(appointmentRepository, times(1)).findById(appointmentId);
            verify(appointmentMapper, times(1)).updateEntity(eq(appointmentDto), any(Appointment.class));
            verify(patientRepository, times(1)).findById(patientId);
            verify(appointmentRepository, times(1)).save(any(Appointment.class));
            verify(appointmentMapper, times(1)).toDto(any(Appointment.class));
        }

        @Test
        @DisplayName("Devrait lever une exception si le rendez-vous n'est pas trouvé")
        void updateAppointment_AppointmentNotFound_ThrowsEntityNotFoundException() {
            // Arrange
            UUID appointmentId = UUID.randomUUID();
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointmentId);

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> appointmentService.updateAppointment(appointmentDto));

            verify(appointmentRepository, times(1)).findById(appointmentId);
            verify(appointmentMapper, never()).updateEntity(any(), any());
            verify(patientRepository, never()).findById(any());
            verify(appointmentRepository, never()).save(any());
        }

        @Test
        @DisplayName("Devrait lever une exception si le patient n'est pas trouvé")
        void updateAppointment_PatientNotFound_ThrowsEntityNotFoundException() {
            // Arrange
            UUID appointmentId = UUID.randomUUID();
            UUID patientId = UUID.randomUUID();

            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setId(appointmentId);
            appointmentDto.setPatientId(patientId);

            Appointment existingAppointment = new Appointment();
            existingAppointment.setId(appointmentId);
            existingAppointment.setPatient(new Patient());
            existingAppointment.getPatient().setId(patientId);

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
            when(appointmentMapper.updateEntity(appointmentDto, existingAppointment)).thenReturn(existingAppointment);
            when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> appointmentService.updateAppointment(appointmentDto));

            verify(appointmentRepository, times(1)).findById(appointmentId);
            verify(appointmentMapper, times(1)).updateEntity(appointmentDto, existingAppointment);
            verify(patientRepository, times(1)).findById(patientId);
            verify(appointmentRepository, never()).save(any());
        }
    }
}