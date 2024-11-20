package com.user_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String name;
    private String emailId;
    private String password;
    private String organization;
    private String role;           // Added field for role
    private Boolean enabled;       // Added field for enabled status
    private String profileImg;     // Added field for profile image URL
    private String gotra;          // Added field for gotra
    private LocalDate dateOfBirth; // Changed to LocalDate for better date handling
    private String gender;         // Added field for gender
    private String avatar;         // Added field for avatar URL
    private String emailId2;       // Added field for secondary email
    private String emailId3;       // Added field for tertiary email
    private String phoneNumber1;   // Added field for primary phone number
    private String phoneNumber2;   // Added field for secondary phone number
    private String phoneNumber3;   // Added field for tertiary phone number
}

