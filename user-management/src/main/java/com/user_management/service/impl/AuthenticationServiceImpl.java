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
        user.setEmailId(signUpRequest.getEmailId());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setOrganization(signUpRequest.getOrganization());
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setProfileImg(signUpRequest.getProfileImg());
        user.setGotra(signUpRequest.getGotra());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setGender(signUpRequest.getGender());
        user.setAvatar(signUpRequest.getAvatar());
        user.setEmailId2(signUpRequest.getEmailId2());
        user.setEmailId3(signUpRequest.getEmailId3());
        user.setPhoneNumber1(signUpRequest.getPhoneNumber1());
        user.setPhoneNumber2(signUpRequest.getPhoneNumber2());
        user.setPhoneNumber3(signUpRequest.getPhoneNumber3());

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    public SignInResponse signin(SignInRequest signInRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmailId(),
                signInRequest.getPassword()));
        var user = userRepository.findByEmailId(signInRequest.getEmailId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtService.generateToken((UserDetails) user);
        
        String profileImg = user.getProfileImg() != null ? 
            "/api/v1/user/profile/download/" + user.getProfileImg() : "";
            
        UserDTO userDto = new UserDTO(
            user.getUserId(),
            user.getUserId(),
            user.getName(), 
            user.getEmailId(), 
            user.getOrganization(),  
            user.getRole(), 
            user.getEnabled(), 
            profileImg,
            user.getGotra(),
            user.getDateOfBirth(),
            user.getGender(),
            user.getAvatar(),
            user.getEmailId2(),
            user.getEmailId3(),
            user.getPhoneNumber1(),
            user.getPhoneNumber2(),
            user.getPhoneNumber3(),
            user.getPassword()
        );

        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setToken(jwt);
        signInResponse.setUser(userDto);

        return signInResponse;
    }
}
