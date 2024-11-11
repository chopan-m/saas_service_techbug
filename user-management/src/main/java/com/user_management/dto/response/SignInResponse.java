package com.user_management.dto.response;

import com.user_management.dto.UserDTO;
import lombok.Data;

@Data
public class SignInResponse {
    private String token;
//    private String refreshToken;
    private UserDTO user;

}
