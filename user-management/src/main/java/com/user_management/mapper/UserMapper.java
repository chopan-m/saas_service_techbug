package com.user_management.mapper;

import com.user_management.dto.UserDTO;
import com.user_management.model.User;

public class UserMapper {

    public static UserDTO mapToUserDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmailId(user.getEmailId());
        dto.setEmailId2(user.getEmailId2());
        dto.setEmailId3(user.getEmailId3());
        dto.setPhoneNumber1(user.getPhoneNumber1());
        dto.setPhoneNumber2(user.getPhoneNumber2());
        dto.setPhoneNumber3(user.getPhoneNumber3());
        dto.setOrganization(user.getOrganization());
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        dto.setGotra(user.getGotra());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
        dto.setAvatar(user.getAvatar());
        dto.setProfileImg(user.getProfileImg() != null ? 
            "/api/v1/user/profile/download/" + user.getProfileImg() : "");
        return dto;
    }

    public static User mapToUser(UserDTO dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setName(dto.getName());
        user.setEmailId(dto.getEmailId());
        user.setEmailId2(dto.getEmailId2());
        user.setEmailId3(dto.getEmailId3());
        user.setPhoneNumber1(dto.getPhoneNumber1());
        user.setPhoneNumber2(dto.getPhoneNumber2());
        user.setPhoneNumber3(dto.getPhoneNumber3());
        user.setOrganization(dto.getOrganization());
        user.setRole(dto.getRole());
        user.setEnabled(dto.getEnabled());
        user.setGotra(dto.getGotra());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setAvatar(dto.getAvatar());
        return user;
    }
}
