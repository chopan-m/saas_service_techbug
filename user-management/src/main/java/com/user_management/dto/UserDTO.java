package com.user_management.dto;

import com.user_management.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String organization;
    private Role role;
    private boolean enabled;
    private String profileImg;
}
