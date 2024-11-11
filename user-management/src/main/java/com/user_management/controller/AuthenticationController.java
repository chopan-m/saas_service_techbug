package com.user_management.controller;

import com.user_management.dto.*;
import com.user_management.dto.request.SignInRequest;
import com.user_management.dto.request.SignUpRequest;
import com.user_management.dto.response.GenericResponse;
import com.user_management.dto.response.SignInResponse;
import com.user_management.service.AuthenticationService;
import com.user_management.service.JWTService;
import com.user_management.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "APIs of USER MANAGEMENT",
        description = "APIs - Signup, SignIn, Validate"
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JWTService jwtService;


    @Operation(
            summary = "Create User REST API",
            description = "POST (/signup) API is used to save User in a database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @PostMapping("/signup")
    public ResponseEntity<GenericResponse<UserDTO>> signup(@RequestBody SignUpRequest signUpRequest){
        System.out.println(signUpRequest + " signup request");
        UserDTO userDTO = authenticationService.signUp(signUpRequest);

        GenericResponse<UserDTO> responseBody = GenericResponse.build(CommonUtils.getPath(), HttpStatus.CREATED.value(),
                true, "Success", "", userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @Operation(
            summary = "SignIn User REST API",
            description = "POST (/signin) API is used to signIn a user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 CREATED"
    )
    @PostMapping("/signin")
    public ResponseEntity<GenericResponse<SignInResponse>> signin(@RequestBody SignInRequest signInRequest){
        System.out.println(signInRequest + "signin request");
        SignInResponse signInResponse = authenticationService.signin(signInRequest);
        GenericResponse<SignInResponse> responseBody = GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
                true, "Success", "", signInResponse);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


 /*
    @Operation(
            summary = "Validate Token REST API",
            description = "POST (/validate) API is used to validate a token"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 CREATED"
    )
    @PostMapping("/validate")
    public ResponseEntity<GenericResponse<String>> validateToken(@RequestBody User user, @RequestParam("token") String token) {
        jwtService.isTokenValid(token, user);
        String responseEntity = "Token is Valid";
        GenericResponse<String> responseBody = GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
                true, "Success", "", responseEntity);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
  */
}
