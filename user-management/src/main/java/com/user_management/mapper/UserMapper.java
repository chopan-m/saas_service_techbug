package com.user_management.mapper;

import com.user_management.dto.UserDTO;
import com.user_management.model.User;

public class UserMapper {

    public static UserDTO mapToUserDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setOrganization(user.getOrganization());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setProfileImg(user.getProfileImg() != null ? 
            "/api/v1/user/profile/download/" + user.getProfileImg() : "");
        return dto;
    }

    public static User mapToUser(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setOrganization(dto.getOrganization());
        user.setRole(dto.getRole());
        user.setEnabled(dto.isEnabled());
        return user;
    }
}
