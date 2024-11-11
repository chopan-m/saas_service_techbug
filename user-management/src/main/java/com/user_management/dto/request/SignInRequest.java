package com.user_management.dto.request;

import lombok.Data;

@Data
public class SignInRequest {

    private String email;
    private String password;
}
