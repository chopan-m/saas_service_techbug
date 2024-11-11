package com.user_management.service;

import com.user_management.dto.response.SignInResponse;
import com.user_management.dto.request.SignInRequest;
import com.user_management.dto.request.SignUpRequest;
import com.user_management.dto.UserDTO;

public interface AuthenticationService {
    UserDTO signUp(SignUpRequest signUpRequest);
    SignInResponse signin(SignInRequest signInRequest);
}
