package com.angel.orchestrator_service.repositories;

import com.angel.orchestrator_service.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RolesRepository extends JpaRepository<Role, String> {

    Role findRoleByName(String name);

}
