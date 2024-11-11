package com.user_management.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserEditRequest {
    private String name;
    private String email;
    private String organization;
}
