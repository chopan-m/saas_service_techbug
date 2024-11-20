package com.user_management.dto;

import com.user_management.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private Integer id;
    private String name;
    private String emailId;
    private String organization;
    private Role role;
    private Boolean enabled;
    private String profileImg;
    private String gotra;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatar;
    private String emailId2;
    private String emailId3;
    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumber3;
    private String password;
}

