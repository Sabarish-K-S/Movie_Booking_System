package com.movie_booking.movie_booking_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password is Required")
    private String password;

    private String phoneNumber;

}
