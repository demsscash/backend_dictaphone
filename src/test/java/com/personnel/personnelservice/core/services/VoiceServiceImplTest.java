package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.adapters.persistances.entities.Voice;
import com.personnel.personnelservice.adapters.persistances.mappers.VoiceMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMedecinRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPatientRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaVoiceRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.VoiceDto;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoiceServiceImplTest {

    @Mock
    private JpaVoiceRepository jpaVoiceRepository;

    @Mock
    private JpaMedecinRepository jpaMedecinRepository;

    @Mock
    private JpaPatientRepository jpaPatientRepository;

    @Mock
    private VoiceMapper voiceMapper;

    @InjectMocks
    private VoiceServiceImpl voiceService;

    private Faker faker;
    private UUID voiceId;
    private UUID doctorId;
    private UUID patientId;
    private Voice voice;
    private VoiceDto voiceDto;
    private Medecin doctor;
    private Patient patient;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        voiceId = UUID.randomUUID();
        doctorId = UUID.randomUUID();
        patientId = UUID.randomUUID();
        pageable = Pageable.unpaged();

        // Setup doctor
        doctor = new Medecin();
        doctor.setId(doctorId);
        doctor.setFirstName(faker.name().firstName());
        doctor.setLastName(faker.name().lastName());
        doctor.setEmail(faker.internet().emailAddress());

        // Setup patient
        patient = new Patient();
        patient.setId(patientId);
        patient.setFirstName(faker.name().firstName());
        patient.setLastName(faker.name().lastName());
        patient.setEmail(faker.internet().emailAddress());

        // Setup voice entity
        voice = new Voice();
        voice.setId(voiceId);
        voice.setPath(faker.file().fileName());
        voice.setTitle(faker.lorem().sentence(3));
        voice.setRemarque(faker.lorem().paragraph());
        voice.setNoteCreated(LocalDateTime.now());
        voice.setDoctor(doctor);
        voice.setPatient(patient);

        // Setup voice DTO
        voiceDto = new VoiceDto();
        voiceDto.setId(voiceId);
        voiceDto.setPath(voice.getPath());
        voiceDto.setTitle(voice.getTitle());
        voiceDto.setRemarque(voice.getRemarque());
        voiceDto.setNoteCreated(voice.getNoteCreated());
        voiceDto.setDoctorId(doctorId);
        voiceDto.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
        voiceDto.setPatientId(patientId);
        voiceDto.setPatientName(patient.getFirstName() + " " + patient.getLastName());
    }

    @Nested
    @DisplayName("Tests for createVoice")
    class CreateVoiceTests {

        @Test
        @DisplayName("Should create a voice successfully")
        void shouldCreateVoiceSuccessfully() {
            // Arrange
            when(voiceMapper.toEntity(voiceDto)).thenReturn(voice);
            when(jpaMedecinRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
            when(jpaPatientRepository.findById(patientId)).thenReturn(Optional.of(patient));
            when(jpaVoiceRepository.save(any(Voice.class))).thenReturn(voice);
            when(voiceMapper.toDTO(voice)).thenReturn(voiceDto);

            // Act
            VoiceDto result = voiceService.createVoice(voiceDto);

            // Assert
            assertNotNull(result);
            assertEquals(voiceId, result.getId());
            assertEquals(voice.getTitle(), result.getTitle());
            assertEquals(doctorId, result.getDoctorId());
            assertEquals(patientId, result.getPatientId());

            verify(voiceMapper).toEntity(voiceDto);
            verify(jpaMedecinRepository).findById(doctorId);
            verify(jpaPatientRepository).findById(patientId);
            verify(jpaVoiceRepository).save(any(Voice.class));
            verify(voiceMapper).toDTO(voice);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when doctor not found")
        void shouldThrowExceptionWhenDoctorNotFound() {
            // Arrange
            when(voiceMapper.toEntity(voiceDto)).thenReturn(voice);
            when(jpaMedecinRepository.findById(doctorId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> voiceService.createVoice(voiceDto));
            assertEquals("Doctor not found with id: " + doctorId, exception.getMessage());

            verify(voiceMapper).toEntity(voiceDto);
            verify(jpaMedecinRepository).findById(doctorId);
            verify(jpaPatientRepository, never()).findById(any(UUID.class));
            verify(jpaVoiceRepository, never()).save(any(Voice.class));
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when patient not found")
        void shouldThrowExceptionWhenPatientNotFound() {
            // Arrange
            when(voiceMapper.toEntity(voiceDto)).thenReturn(voice);
            when(jpaMedecinRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
            when(jpaPatientRepository.findById(patientId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> voiceService.createVoice(voiceDto));
            assertEquals("Patient not found with id: " + patientId, exception.getMessage());

            verify(voiceMapper).toEntity(voiceDto);
            verify(jpaMedecinRepository).findById(doctorId);
            verify(jpaPatientRepository).findById(patientId);
            verify(jpaVoiceRepository, never()).save(any(Voice.class));
        }
    }

    @Nested
    @DisplayName("Tests for getVoiceById")
    class GetVoiceByIdTests {

        @Test
        @DisplayName("Should return a voice when ID exists")
        void shouldReturnVoiceWhenIdExists() {
            // Arrange
            when(jpaVoiceRepository.findById(voiceId)).thenReturn(Optional.of(voice));
            when(voiceMapper.toDTO(voice)).thenReturn(voiceDto);

            // Act
            VoiceDto result = voiceService.getVoiceById(voiceId);

            // Assert
            assertNotNull(result);
            assertEquals(voiceId, result.getId());
            assertEquals(voice.getTitle(), result.getTitle());

            verify(jpaVoiceRepository).findById(voiceId);
            verify(voiceMapper).toDTO(voice);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when voice not found")
        void shouldThrowExceptionWhenVoiceNotFound() {
            // Arrange
            when(jpaVoiceRepository.findById(voiceId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> voiceService.getVoiceById(voiceId));
            assertEquals("Voice not found with id: " + voiceId, exception.getMessage());

            verify(jpaVoiceRepository).findById(voiceId);
            verify(voiceMapper, never()).toDTO(any(Voice.class));
        }
    }

    @Nested
    @DisplayName("Tests for updateVoice")
    class UpdateVoiceTests {

        @Test
        @DisplayName("Should update a voice successfully")
        void shouldUpdateVoiceSuccessfully() {
            // Arrange
            when(jpaVoiceRepository.findById(voiceId)).thenReturn(Optional.of(voice));
            doNothing().when(voiceMapper).updateEntity(voiceDto, voice);
            when(jpaMedecinRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
            when(jpaPatientRepository.findById(patientId)).thenReturn(Optional.of(patient));
            when(jpaVoiceRepository.save(any(Voice.class))).thenReturn(voice);
            when(voiceMapper.toDTO(voice)).thenReturn(voiceDto);

            // Act
            VoiceDto result = voiceService.updateVoice(voiceDto);

            // Assert
            assertNotNull(result);
            assertEquals(voiceId, result.getId());

            verify(jpaVoiceRepository).findById(voiceId);
            verify(voiceMapper).updateEntity(voiceDto, voice);
            verify(jpaMedecinRepository).findById(doctorId);
            verify(jpaPatientRepository).findById(patientId);
            verify(jpaVoiceRepository).save(any(Voice.class));
            verify(voiceMapper).toDTO(voice);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when voice not found")
        void shouldThrowExceptionWhenVoiceNotFound() {
            // Arrange
            when(jpaVoiceRepository.findById(voiceId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> voiceService.updateVoice(voiceDto));
            assertEquals("Voice not found with id: " + voiceId, exception.getMessage());

            verify(jpaVoiceRepository).findById(voiceId);
            verify(voiceMapper, never()).updateEntity(any(VoiceDto.class), any(Voice.class));
            verify(jpaVoiceRepository, never()).save(any(Voice.class));
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when doctor not found")
        void shouldThrowExceptionWhenDoctorNotFound() {
            // Arrange
            when(jpaVoiceRepository.findById(voiceId)).thenReturn(Optional.of(voice));
            doNothing().when(voiceMapper).updateEntity(voiceDto, voice);
            when(jpaMedecinRepository.findById(doctorId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> voiceService.updateVoice(voiceDto));
            assertEquals("Doctor not found with id: " + doctorId, exception.getMessage());

            verify(jpaVoiceRepository).findById(voiceId);
            verify(voiceMapper).updateEntity(voiceDto, voice);
            verify(jpaMedecinRepository).findById(doctorId);
            verify(jpaVoiceRepository, never()).save(any(Voice.class));
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when patient not found")
        void shouldThrowExceptionWhenPatientNotFound() {
            // Arrange
            when(jpaVoiceRepository.findById(voiceId)).thenReturn(Optional.of(voice));
            doNothing().when(voiceMapper).updateEntity(voiceDto, voice);
            when(jpaMedecinRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
            when(jpaPatientRepository.findById(patientId)).thenReturn(Optional.empty());

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> voiceService.updateVoice(voiceDto));
            assertEquals("Patient not found with id: " + patientId, exception.getMessage());

            verify(jpaVoiceRepository).findById(voiceId);
            verify(voiceMapper).updateEntity(voiceDto, voice);
            verify(jpaMedecinRepository).findById(doctorId);
            verify(jpaPatientRepository).findById(patientId);
            verify(jpaVoiceRepository, never()).save(any(Voice.class));
        }
    }

    @Nested
    @DisplayName("Tests for deleteVoice")
    class DeleteVoiceTests {

        @Test
        @DisplayName("Should delete a voice successfully")
        void shouldDeleteVoiceSuccessfully() {
            // Arrange
            when(jpaVoiceRepository.existsById(voiceId)).thenReturn(true);
            doNothing().when(jpaVoiceRepository).deleteById(voiceId);

            // Act
            voiceService.deleteVoice(voiceId);

            // Assert
            verify(jpaVoiceRepository).existsById(voiceId);
            verify(jpaVoiceRepository).deleteById(voiceId);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when voice not found")
        void shouldThrowExceptionWhenVoiceNotFound() {
            // Arrange
            when(jpaVoiceRepository.existsById(voiceId)).thenReturn(false);

            // Act & Assert
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> voiceService.deleteVoice(voiceId));
            assertEquals("Voice not found with id: " + voiceId, exception.getMessage());

            verify(jpaVoiceRepository).existsById(voiceId);
            verify(jpaVoiceRepository, never()).deleteById(any(UUID.class));
        }
    }

    @Nested
    @DisplayName("Tests for getAllVoices")
    class GetAllVoicesTests {

        @Test
        @DisplayName("Should return all voices")
        void shouldReturnAllVoices() {
            // Arrange
            List<Voice> voices = Arrays.asList(voice, createAdditionalVoice());
            List<VoiceDto> voiceDtos = voices.stream()
                    .map(v -> {
                        VoiceDto dto = new VoiceDto();
                        dto.setId(v.getId());
                        dto.setTitle(v.getTitle());
                        return dto;
                    })
                    .collect(Collectors.toList());

            when(jpaVoiceRepository.findAll()).thenReturn(voices);
            when(voiceMapper.toDTO(any(Voice.class))).thenAnswer(invocation -> {
                Voice v = invocation.getArgument(0);
                return voiceDtos.stream()
                        .filter(dto -> dto.getId().equals(v.getId()))
                        .findFirst()
                        .orElse(null);
            });

            // Act
            List<VoiceDto> result = voiceService.getAllVoices();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());

            verify(jpaVoiceRepository).findAll();
            verify(voiceMapper, times(2)).toDTO(any(Voice.class));
        }

        @Test
        @DisplayName("Should return an empty list when no voices exist")
        void shouldReturnEmptyListWhenNoVoicesExist() {
            // Arrange
            when(jpaVoiceRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<VoiceDto> result = voiceService.getAllVoices();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(jpaVoiceRepository).findAll();
            verify(voiceMapper, never()).toDTO(any(Voice.class));
        }
    }

    @Nested
    @DisplayName("Tests for getAllVoices with pagination")
    class GetAllVoicesPaginatedTests {

        @Test
        @DisplayName("Should return paginated voices")
        void shouldReturnPaginatedVoices() {
            // Arrange
            List<Voice> voices = Arrays.asList(voice, createAdditionalVoice());
            Page<Voice> voicePage = new PageImpl<>(voices);

            when(jpaVoiceRepository.findAll(eq(pageable))).thenReturn(voicePage);
            when(voiceMapper.toDTO(any(Voice.class))).thenReturn(voiceDto);

            // Act
            Page<VoiceDto> result = voiceService.getAllVoices(pageable);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getTotalElements());

            verify(jpaVoiceRepository).findAll(eq(pageable));
            verify(voiceMapper, times(2)).toDTO(any(Voice.class));
        }
    }

    @Nested
    @DisplayName("Tests for getVoicesByDoctorId")
    class GetVoicesByDoctorIdTests {

        @Test
        @DisplayName("Should return voices by doctor ID")
        void shouldReturnVoicesByDoctorId() {
            // Arrange
            List<Voice> voices = Arrays.asList(voice, createAdditionalVoice());

            when(jpaVoiceRepository.findByDoctorId(doctorId)).thenReturn(voices);
            when(voiceMapper.toDTO(any(Voice.class))).thenReturn(voiceDto);

            // Act
            List<VoiceDto> result = voiceService.getVoicesByDoctorId(doctorId);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());

            verify(jpaVoiceRepository).findByDoctorId(doctorId);
            verify(voiceMapper, times(2)).toDTO(any(Voice.class));
        }
    }

    /**
     * Helper method to create an additional voice for testing
     */
    private Voice createAdditionalVoice() {
        Voice additionalVoice = new Voice();
        additionalVoice.setId(UUID.randomUUID());
        additionalVoice.setPath(faker.file().fileName());
        additionalVoice.setTitle(faker.lorem().sentence(3));
        additionalVoice.setRemarque(faker.lorem().paragraph());
        additionalVoice.setNoteCreated(LocalDateTime.now());
        additionalVoice.setDoctor(doctor);
        additionalVoice.setPatient(patient);
        return additionalVoice;
    }
}