package com.angel.orchestrator_service.DTO;

import com.angel.orchestrator_service.entities.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class RoleDto {
    private String id;
    private List<User> users;
    private String name;
}
