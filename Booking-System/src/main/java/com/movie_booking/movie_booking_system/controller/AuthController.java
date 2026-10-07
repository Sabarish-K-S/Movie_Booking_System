package com.movie_booking.movie_booking_system.controller;

import com.movie_booking.movie_booking_system.dto.AuthResponseDTO;
import com.movie_booking.movie_booking_system.dto.LoginDTO;
import com.movie_booking.movie_booking_system.dto.RegisterDTO;
import com.movie_booking.movie_booking_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO request){
         AuthResponseDTO response=authService.register(request);
         return ResponseEntity
                 .status(HttpStatus.CREATED)
                 .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO request){
        AuthResponseDTO response=authService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
