package com.movie_booking.movie_booking_system.service;

import com.movie_booking.movie_booking_system.dto.AuthResponseDTO;
import com.movie_booking.movie_booking_system.dto.LoginDTO;
import com.movie_booking.movie_booking_system.dto.RegisterDTO;
import com.movie_booking.movie_booking_system.entity.Role;
import com.movie_booking.movie_booking_system.entity.User;
import com.movie_booking.movie_booking_system.exception.EmailAlreadyExistsException;
import com.movie_booking.movie_booking_system.exception.InvalidCredentialsException;
import com.movie_booking.movie_booking_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDTO register(RegisterDTO request){
        String email=request.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String phone = (request.getPhoneNumber()==null || request.getPhoneNumber().isBlank())
                       ? null : request.getPhoneNumber().trim();

        User user=new User();

        user.setName(request.getName());
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String jwtToken=jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(jwtToken, "Bearer", user.getName(), email);

    }

    public AuthResponseDTO login(LoginDTO request){
        try{
            String email=request.getEmail().trim().toLowerCase();
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String jwtToken=jwtService.generateToken(email);
            return new AuthResponseDTO(jwtToken, "Bearer", user.getName(), email);
        }
        catch(BadCredentialsException e){
            throw new InvalidCredentialsException("Incorrect username or password");
        }

    }
}
