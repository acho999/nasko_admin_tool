package com.angel.orchestrator_service.services.api;

import com.angel.orchestrator_service.DTO.UserDto;
import com.angel.orchestrator_service.pojo.Credentials;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface AuthService{

    UserDto register(UserDto dto, HttpServletRequest request, HttpServletResponse response);

    void login(Credentials credentials, HttpServletRequest request, HttpServletResponse response);

    boolean delete(String id);

    UserDto update(UserDto user);

    UserDto getUserById(String id);

    UserDto getUserByEmail(String email);

}
