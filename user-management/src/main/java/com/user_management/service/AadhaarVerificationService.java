package com.user_management.service;

import com.user_management.dto.request.AadhaarVerificationRequest;
import com.user_management.dto.response.AadhaarVerificationResponse;

public interface AadhaarVerificationService {
    AadhaarVerificationResponse initiateVerification(AadhaarVerificationRequest request);
    AadhaarVerificationResponse verifyOTP(String referenceId, String otp);
} 