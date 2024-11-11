package com.user_management.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String organization;
}
