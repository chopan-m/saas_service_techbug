package com.user_management.mapper;

import com.user_management.dto.UserDTO;
import com.user_management.model.User;

public class UserMapper {

    public static UserDTO mapToUserDto(User user) {
        return new UserDTO(
            user.getUserId(),
            user.getUserId(),
            user.getName(),
            user.getEmailId(),
            user.getOrganization(),
            user.getRole(),
            user.getEnabled(),
            user.getProfileImg(),
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
    }

    public static User mapToUser(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());
        user.setEmailId(userDTO.getEmailId());
        user.setOrganization(userDTO.getOrganization());
        user.setRole(userDTO.getRole());
        user.setEnabled(userDTO.getEnabled());
        user.setProfileImg(userDTO.getProfileImg());
        user.setGotra(userDTO.getGotra());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setGender(userDTO.getGender());
        user.setAvatar(userDTO.getAvatar());
        return user;
    }
}
