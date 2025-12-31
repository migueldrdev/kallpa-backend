package dev.migueldr.kallpa_ecommerce.config.security;

import dev.migueldr.kallpa_ecommerce.persistence.entity.UserEntity;
import dev.migueldr.kallpa_ecommerce.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Convertimos nuestra UserEntity al objeto UserDetails que entiende Spring Security
        return new User(
                user.getEmail(),
                user.getPassword(),
                // Convertimos el rol (String) a GrantedAuthority
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}