package com.personnel.personnelservice.core.ports.services;


import com.personnel.personnelservice.core.models.dtos.*;
/**
 * Service for handling authentication and user registration.
 * This service provides methods to register users, patients, doctors, and assistants,
 * as well as to authenticate them and generate JWT tokens upon successful login.
 *
 * @author AICHA CHAKIR
 */
public interface AuthService {
    /**
     * Registers a user in the system.
     * @param userDTO The user information to be registered.
     * @return The registered user with created information.
     */
    UserDto registerUser(UserDto userDTO);

    /**
     * Registers a patient in the system.
     * @param patientDto The patient information to be registered.
     * @return The registered patient with created information.
     */
    PatientDto registerPatient(PatientDto patientDto);

    /**
     * Registers a doctor in the system.
     * @param medecinDto The doctor information to be registered.
     * @return The registered doctor with created information.
     */
    MedecinDto registerMedecin(MedecinDto medecinDto);

    /**
     * Registers an assistant in the system.
     * @param assistantDto The assistant information to be registered.
     * @return The registered assistant with created information.
     */
    AssistantDto registerAssistant(AssistantDto assistantDto);

    /**
     * Logs in a user and generates JWT tokens.
     * @param loginDto The login credentials(email and password).
     * @return The logged-in user's information.
     */
    JwtResponseDto login(LoginDto loginDto);

    /**
     * Logs in a patient and generates JWT tokens.
     * @param loginDto The login credentials(email & password).
     * @return The logged-in patient's information.
     */
    JwtResponseDto loginPatient(LoginDto loginDto);

    /**
     * Logs in a doctor and generates JWT tokens.
     * @param loginDto The login credentials(email & password).
     * @return The logged-in doctor's information.
     */
    JwtResponseDto loginMedecin(LoginDto loginDto);

    /**
     * Logs in an assistant and generates JWT tokens.
     * @param loginDto The login credentials(email & password).
     * @return The logged-in assistant's information.
     */
    JwtResponseDto loginAssistant(LoginDto loginDto);

    /**
     * Get the currently logged-in user.
     * @param email The email of the currently logged-in user.
     * @return The currently logged-in user's information.
     */
    PatientDto getCurrentUser(String email);
}
