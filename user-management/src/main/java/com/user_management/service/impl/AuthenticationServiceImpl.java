package com.user_management.service.impl;

import com.user_management.dto.request.SignInRequest;
import com.user_management.dto.UserDTO;
import com.user_management.mapper.UserMapper;
import com.user_management.service.AuthenticationService;
import com.user_management.service.JWTService;
import com.user_management.dto.response.SignInResponse;
import com.user_management.dto.request.SignUpRequest;
import com.user_management.model.Role;
import com.user_management.model.User;
import com.user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserDTO signUp(SignUpRequest signUpRequest){
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setOrganization(signUpRequest.getOrganization());
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setProfileImg(null);

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    public SignInResponse signin(SignInRequest signInRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword()));
        var user = userRepository.findByEmail(signInRequest.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken((UserDetails) user);
        
        String profileImg = user.getProfileImg() != null ? 
            "/api/v1/user/profile/download/" + user.getProfileImg() : "";
            
        UserDTO userDto = new UserDTO(
            user.getId(), 
            user.getName(), 
            user.getEmail(), 
            user.getOrganization(),  
            user.getRole(), 
            user.isEnabled(), 
            profileImg
        );

        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setToken(jwt);
        signInResponse.setUser(userDto);

        return signInResponse;
    }
}
