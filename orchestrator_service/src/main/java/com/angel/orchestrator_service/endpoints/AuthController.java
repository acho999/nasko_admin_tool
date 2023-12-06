package com.angel.orchestrator_service.endpoints;

import com.angel.orchestrator_service.DTO.UserDto;
import com.angel.orchestrator_service.pojo.Credentials;
import com.angel.orchestrator_service.services.impl.AuthServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/users")
@Api(value = "Users CRUD operations")
public class AuthController {

    private final AuthServiceImpl service;

    @Autowired
    public AuthController(AuthServiceImpl service) {
        this.service = service;
    }

    @PostMapping(
        value = "/register"
    )
    @ApiOperation(value = "Register and create user")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse response)
        throws URISyntaxException {
        UserDto dto = this.service.register(userDto,request,response);
        return ResponseEntity.created(new URI("/users/register")).body(dto);
    }

    @PostMapping(
        value = "/login",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Authentication of the user")
    public ResponseEntity<String> login(@RequestBody Credentials credentials, HttpServletRequest request, HttpServletResponse response){
        this.service.login(credentials,request,response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(
        value = "/getUserDetails/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Get details of an user")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable String userId){
        UserDto userDetails = this.service.getUserById(userId);
        return ResponseEntity.of(Optional.of(userDetails));
    }

    @GetMapping(
        value = "/getUserDetails",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation(value = "Get details of an user")
    public ResponseEntity<UserDto> getUserDetailsByEmail(@RequestParam(value = "email") String email){
        UserDto userDetails = this.service.getUserByEmail(email);
        return ResponseEntity.of(Optional.of(userDetails));
    }
}
