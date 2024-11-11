package com.user_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "name", length = 255)
    private String name;
    
    @Column(name = "gotra", length = 50)
    private String gotra;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "avatar", length = 255)
    private String avatar;
    
    @Column(name = "email_id", length = 255)
    private String emailId;
    
    @Column(name = "email_id_2", length = 255)
    private String emailId2;
    
    @Column(name = "email_id_3", length = 255)
    private String emailId3;
    
    @Column(name = "phone_number_1", length = 15)
    private String phoneNumber1;
    
    @Column(name = "phone_number_2", length = 15)
    private String phoneNumber2;
    
    @Column(name = "phone_number_3", length = 15)
    private String phoneNumber3;
    
    @Column(name = "id")
    private Long id;
    
    @Column(name = "organization", length = 255)
    private String organization;
    
    @Column(name = "password", length = 255)
    private String password;
    
    @Column(name = "profile_img", length = 255)
    private String profileImg;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 255)
    private Role role;
    
    @Column(name = "enabled")
    private Boolean enabled;
}
