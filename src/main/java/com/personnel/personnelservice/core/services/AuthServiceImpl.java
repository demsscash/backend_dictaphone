package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.*;
import com.personnel.personnelservice.adapters.persistances.repositories.*;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.*;
import com.personnel.personnelservice.core.ports.repositories.*;
import com.personnel.personnelservice.core.ports.services.AuthService;
import com.personnel.personnelservice.core.exceptions.AuthenticationFailedException;
import com.personnel.personnelservice.core.exceptions.UserAlreadyExistsException;
import com.personnel.personnelservice.adapters.persistances.mappers.AssistantMapper;
import com.personnel.personnelservice.adapters.persistances.mappers.MedecinMapper;
import com.personnel.personnelservice.adapters.persistances.mappers.PatientMapper;
import com.personnel.personnelservice.adapters.persistances.mappers.UserMapper;
import com.personnel.personnelservice.core.ports.services.UserService;
import com.personnel.personnelservice.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository jpaUserRepository;
    private final PatientRepository jpaPatientRepository;
    private final MedecinRepository jpaMedecinRepository;
    private final AssistantRepository jpaAssistantRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleRepository jpaRoleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final PatientMapper patientMapper;
    private final MedecinMapper medecinMapper;
    private final AssistantMapper assistantMapper;
    @Override
    public UserDto registerUser(UserDto userRequest) {
        if (jpaUserRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists.");
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user = jpaUserRepository.save(user);

        UserDto userDto = userMapper.toDTO(user);

        return userDto;
    }

    @Override
    public PatientDto registerPatient(PatientDto patientRequest) {
        if (jpaPatientRepository.findByEmail(patientRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + patientRequest.getEmail() + " already exists.");
        }

        Patient patient = patientMapper.toEntity(patientRequest);
        patient.setPassword(passwordEncoder.encode("Patient98@"));
        patient = jpaPatientRepository.save(patient);
        assignDefaultRole(patient.getId(), "PATIENT");
        PatientDto patientDto = patientMapper.toDTO(patient);

        return patientDto;
    }

    @Override
    public MedecinDto registerMedecin(MedecinDto medecinRequest)  {
        if (jpaMedecinRepository.findByEmail(medecinRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + medecinRequest.getEmail() + " already exists.");
        }
        Medecin medecin = medecinMapper.toEntity(medecinRequest);

        medecin.setPassword(passwordEncoder.encode(medecinRequest.getPassword()));
        medecin = jpaMedecinRepository.save(medecin);
        assignDefaultRole(medecin.getId(), "MEDECIN");
        MedecinDto medecinDto = medecinMapper.toDTO(medecin);
        return medecinDto;
    }

    @Override
    public AssistantDto registerAssistant(AssistantDto assistantRequest) {
        if (jpaUserRepository.findByEmail(assistantRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + assistantRequest.getEmail() + " already exists.");
        }

        Assistant assistant = assistantMapper.toEntity(assistantRequest);
        assistant.setPassword(passwordEncoder.encode(assistantRequest.getPassword()));
        assistant = jpaAssistantRepository.save(assistant);
        assignDefaultRole(assistant.getId(), "ASSISTANT");
        AssistantDto assistantDto = assistantMapper.toDTO(assistant);

        return assistantDto;
    }

    @Override
    public JwtResponseDto login(LoginDto loginDto)  {
        JwtResponseDto jwtResponseDto= new JwtResponseDto();
        User user = jpaUserRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + loginDto.getEmail()));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        } catch (Exception e) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        boolean rememberMe = loginDto.isRememberMe();
        String jwt = jwtTokenUtil.generateToken(user,rememberMe);
        jwtResponseDto.setToken(jwt);
        return jwtResponseDto;
    }

    @Override
    public JwtResponseDto loginPatient(LoginDto loginDto) {
        JwtResponseDto jwtResponseDto= new JwtResponseDto();
        Patient patient = jpaPatientRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + loginDto.getEmail()));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        } catch (Exception e) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        boolean rememberMe = loginDto.isRememberMe();
        String jwt = jwtTokenUtil.generateToken(patient,rememberMe,true);
        jwtResponseDto.setToken(jwt);
        return jwtResponseDto;
    }

    @Override
    public JwtResponseDto loginMedecin(LoginDto loginDto)  {
        JwtResponseDto jwtResponseDto= new JwtResponseDto();
        Medecin user = jpaMedecinRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + loginDto.getEmail()));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        } catch (Exception e) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        boolean rememberMe = loginDto.isRememberMe();
        String jwt = jwtTokenUtil.generateToken(user,rememberMe);
        jwtResponseDto.setToken(jwt);
        return jwtResponseDto;
    }

    @Override
    public JwtResponseDto loginAssistant(LoginDto loginDto) {
        JwtResponseDto jwtResponseDto= new JwtResponseDto();
        Assistant user = jpaAssistantRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + loginDto.getEmail()));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        } catch (Exception e) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        boolean rememberMe = loginDto.isRememberMe();
        String jwt = jwtTokenUtil.generateToken(user,rememberMe);
        jwtResponseDto.setToken(jwt);
        return jwtResponseDto;
    }

    @Override
    public PatientDto getCurrentUser(String email) {
        Patient user = jpaPatientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return patientMapper.toDTO(user);
    }
    private void assignDefaultRole(UUID userId, String roleName) {
        Role role = jpaRoleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role " + roleName + " not found."));
        userService.addRoleToUser(userId, role.getId());
    }


}
