package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.ResetPasswordDto;
import com.personnel.personnelservice.core.ports.services.EmailSenderPort;
import com.personnel.personnelservice.core.ports.services.ForgotPasswordUseCase;
import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaUserRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPatientRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMedecinRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaAssistantRepository;

import com.personnel.personnelservice.security.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService implements ForgotPasswordUseCase {
    private final JpaUserRepository userRepository;
    private final JpaPatientRepository patientRepository;
    private final JpaMedecinRepository medecinRepository;
    private final JpaAssistantRepository assistantRepository;
    private final EmailSenderPort emailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public String forgotPassword(String email) {
        Optional<? extends User> userOptional = findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Utilisateur non trouvé avec l'email : " + email);
        }

        String token = jwtTokenUtil.generateResetPasswordToken(userOptional.get());
        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        emailSender.sendEmail(email, "Réinitialisation du mot de passe",
                "Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink);

        return "Un email de réinitialisation a été envoyé.";
    }

    private Optional<? extends User> findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) return user;

        Optional<Patient> patient = patientRepository.findByEmail(email);
        if (patient.isPresent()) return patient;

        Optional<Medecin> medecin = medecinRepository.findByEmail(email);
        if (medecin.isPresent()) return medecin;

        return assistantRepository.findByEmail(email);
    }
    @Override
    @Transactional
    public String resetPassword(ResetPasswordDto resetPasswordDto) {
        String email = jwtTokenUtil.extractUsername(resetPasswordDto.getToken());
        Optional<? extends User> userOptional = findUserByEmail(email);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Token invalide ou utilisateur non trouvé.");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        userRepository.save(user);

        return "Mot de passe réinitialisé avec succès.";
    }
}

