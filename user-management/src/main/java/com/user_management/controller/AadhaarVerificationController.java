package com.user_management.controller;

import com.user_management.dto.request.AadhaarVerificationRequest;
import com.user_management.dto.response.AadhaarVerificationResponse;
import com.user_management.dto.response.GenericResponse;
import com.user_management.service.AadhaarVerificationService;
import com.user_management.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Aadhaar Verification APIs",
    description = "APIs for Aadhaar verification using Setu OKYC"
)
@RestController
@RequestMapping("/api/v1/aadhaar")
@RequiredArgsConstructor
public class AadhaarVerificationController {

    private final AadhaarVerificationService aadhaarVerificationService;

    @Operation(
        summary = "Initiate Aadhaar Verification",
        description = "Initiates the Aadhaar verification process and sends OTP",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/verify/initiate")
    public ResponseEntity<GenericResponse<AadhaarVerificationResponse>> initiateVerification(
            @RequestBody AadhaarVerificationRequest request) {
        AadhaarVerificationResponse response = aadhaarVerificationService.initiateVerification(request);
        
        GenericResponse<AadhaarVerificationResponse> genericResponse = GenericResponse.build(
            CommonUtils.getPath(),
            HttpStatus.OK.value(),
            true,
            "Verification initiated successfully",
            "",
            response
        );
        
        return ResponseEntity.ok(genericResponse);
    }

    @Operation(
        summary = "Verify Aadhaar OTP",
        description = "Verifies the OTP sent to Aadhaar-linked mobile number",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/verify/otp")
    public ResponseEntity<GenericResponse<AadhaarVerificationResponse>> verifyOTP(
            @RequestParam String referenceId,
            @RequestParam String otp) {
        AadhaarVerificationResponse response = aadhaarVerificationService.verifyOTP(referenceId, otp);
        
        GenericResponse<AadhaarVerificationResponse> genericResponse = GenericResponse.build(
            CommonUtils.getPath(),
            HttpStatus.OK.value(),
            true,
            "OTP verification successful",
            "",
            response
        );
        
        return ResponseEntity.ok(genericResponse);
    }
} 