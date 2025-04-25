package com.personnel.personnelservice.security;

import com.personnel.personnelservice.adapters.persistances.repositories.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final JpaUserRepository jpaUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jpaUserRepository.findByEmail(username).orElseThrow();
    }
}
