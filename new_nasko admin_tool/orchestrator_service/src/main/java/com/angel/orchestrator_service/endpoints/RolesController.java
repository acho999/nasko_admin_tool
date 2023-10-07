package com.angel.orchestrator_service.endpoints;

import com.angel.orchestrator_service.DTO.RoleDto;
import com.angel.orchestrator_service.services.api.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

@RestController
@RequestMapping(value = "/roles")
public class RolesController {

    private RolesService service;

    @Autowired
    public RolesController(RolesService service) {
        this.service = service;
    }

    @PostMapping(value = "/create",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto role) throws URISyntaxException {
        RoleDto dto = this.service.createRole(role);
        return ResponseEntity.created(new URI("/roles/create")).body(dto);
    }

    @GetMapping(value = "/getAll",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<RoleDto>> getAll(){
        Collection<RoleDto> roles = this.service.getAll();
        return ResponseEntity.ok().body(roles);
    }

}
