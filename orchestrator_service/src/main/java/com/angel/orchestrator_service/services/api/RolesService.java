package com.angel.orchestrator_service.services.api;

import com.angel.orchestrator_service.DTO.RoleDto;
import com.angel.orchestrator_service.entities.Role;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RolesService {

    RoleDto createRole(RoleDto role);
    RoleDto getRole(UUID id);
    boolean deleteRole(UUID id);
    RoleDto updateRole(RoleDto role);
    Collection<RoleDto> getAll();

}
