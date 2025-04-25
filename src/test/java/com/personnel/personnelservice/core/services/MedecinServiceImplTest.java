package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.core.services.MedecinServiceImpl;
import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import com.personnel.personnelservice.core.models.dtos.MedecinDto;
import com.personnel.personnelservice.adapters.persistances.mappers.MedecinMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMedecinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedecinServiceImplTest {
    @Mock private JpaMedecinRepository jpaMedecinRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private MedecinMapper medecinMapper;
    @InjectMocks private MedecinServiceImpl medecinService;
    private Faker faker;
    @BeforeEach
    void setUp() {
        faker = new Faker();
    }
    @Test
    void updateMedecin() {
        UUID medecinId = UUID.randomUUID();
        Medecin medecin = new Medecin();
        medecin.setId(medecinId);
        medecin.setLastName(faker.name().lastName());
        medecin.setFirstName(faker.name().firstName());
        medecin.setEmail(faker.internet().emailAddress());
        medecin.setPassword(passwordEncoder.encode("oldPassword"));

        MedecinDto medecinDto = new MedecinDto();
        medecinDto.setId(medecinId);
        medecinDto.setLastName(faker.name().lastName());
        medecinDto.setFirstName(faker.name().firstName());
        medecinDto.setEmail(faker.internet().emailAddress());
        medecinDto.setPassword("newPassword");

        when(jpaMedecinRepository.findById(medecinId)).thenReturn(Optional.of(medecin));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(medecinMapper.toDTO(any(Medecin.class))).thenReturn(medecinDto);

        MedecinDto updatedUser = medecinService.updateMedecin(medecinDto);

        assertNotNull(updatedUser);
        verify(jpaMedecinRepository, times(1)).save(any(Medecin.class));
    }

    @Test
    void deleteMedecin() {
        UUID medecinId = UUID.randomUUID();

        when(jpaMedecinRepository.existsById(medecinId)).thenReturn(true);
        doNothing().when(jpaMedecinRepository).deleteById(medecinId);

        medecinService.deleteMedecin(medecinId);
        verify(jpaMedecinRepository, times(1)).deleteById(medecinId);
    }

    @Test
    void getAllMedecin() {
        List<Medecin> medecins = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Medecin medecin = new Medecin();
            medecin.setId(UUID.randomUUID());
            medecin.setLastName(faker.name().lastName());
            medecin.setFirstName(faker.name().firstName());
            medecins.add(medecin);
        }

        when(jpaMedecinRepository.findAll()).thenReturn(medecins);
        when(medecinMapper.toDTO(any(Medecin.class))).thenReturn(new MedecinDto());

        List<MedecinDto> medecinDtos = medecinService.getAllMedecin();

        assertFalse(medecinDtos.isEmpty());
        assertEquals(3, medecinDtos.size());
        verify(jpaMedecinRepository, times(1)).findAll();
    }
}