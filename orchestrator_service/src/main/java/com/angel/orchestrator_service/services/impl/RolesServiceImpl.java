package com.angel.orchestrator_service.services.impl;

import com.angel.orchestrator_service.DTO.RoleDto;
import com.angel.orchestrator_service.entities.Role;
import com.angel.orchestrator_service.repositories.RolesRepository;
import com.angel.orchestrator_service.services.api.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RolesServiceImpl implements RolesService {

    private RolesRepository repository;

    @Autowired
    public RolesServiceImpl(RolesRepository repository){
        this.repository = repository;
    }

    @Override
    public RoleDto createRole(RoleDto role) {
        Role entity = Role.builder().name(role.getName()).build();
        this.repository.saveAndFlush(entity);
        role.setId(entity.getId());
        return role;
    }

    @Override
    public RoleDto getRole(UUID id) {
        return null;
    }

    @Override
    public boolean deleteRole(UUID id) {
        return false;
    }

    @Override
    public RoleDto updateRole(RoleDto role) {
        return null;
    }

    @Override
    public List<RoleDto> getAll() {
        List<RoleDto> roles = this.repository.findAll()
            .stream()
            .map(x->RoleDto.builder()
                .id(x.getId())
                .name(x.getName())
                .build())
            .collect(Collectors.toList());
        return roles;
    }

}