package com.angel.orchestrator_service.DTO;

import com.angel.orchestrator_service.entities.Role;
import com.angel.orchestrator_service.entities.User;
import com.angel.orchestrator_service.pojo.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDto {
    private String id;
    private String userName;
    private String email;
    private String setEncryptedPassword;
    private String password;
    private List<Role> authorities;
    private Date date_created;
    private Token jwtToken;
    private Token refreshToken;

    public static UserDto of(User user){
        return UserDto.builder()
            .id(user.getId())
            .userName(user.getUsername())
            .email(user.getEmail())
            .authorities(user.getRoles())
            .setEncryptedPassword(user.getEncryptedPassword())
            .date_created(user.getDate_created())
            .build();
    }
}
