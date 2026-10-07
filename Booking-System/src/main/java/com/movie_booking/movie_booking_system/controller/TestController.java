package com.movie_booking.movie_booking_system.controller;


import com.movie_booking.movie_booking_system.dto.AuthResponseDTO;
import com.movie_booking.movie_booking_system.dto.LoginDTO;
import com.movie_booking.movie_booking_system.dto.RegisterDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/EPs")
public class TestController {
    @PostMapping("/EP1")
    public String register(){
       return "EndPoint1";
    }

    @PostMapping("/EP2")
    public String login(){
        return "EndPoint2";
    }
}
