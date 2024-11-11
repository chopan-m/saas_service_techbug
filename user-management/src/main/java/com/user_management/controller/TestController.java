package com.user_management.controller;

import com.user_management.dto.response.GenericResponse;
import com.user_management.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "TEST APIs - Open & Secure",
        description = "Test APIs"
)
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(
            summary = "API to ping a SECURE (Authorized) dummy API endpoint",
            description = "GET (/auth/hello) API is used ping a Authorized API",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200"
    )
    @GetMapping("/auth/hello")
//    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationToken
    public ResponseEntity<GenericResponse<String>>  helloAuthorized() throws Exception{
        System.out.println("inside /auth/hello......................................");
        String entity = "Hi There, Hello from /auth/hello";
        GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
                true, "Success", "", entity);

    try {
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch(NullPointerException npe) {
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
    }

    @Operation(
            summary = "API to ping a PUBLIC (unauthorized) dummy API endpoint",
            description = "GET (/unauth/hello) API is used ping a Open API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200"
    )
    @GetMapping("/unauth/hello")
    public ResponseEntity<GenericResponse<String>>  helloUnauthorized() {
        String entity = "Hi There, Hello from /unath/hello";
        GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
                true, "Success", "", entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

}
