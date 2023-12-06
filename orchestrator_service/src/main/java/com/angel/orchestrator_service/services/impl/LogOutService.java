package com.angel.orchestrator_service.services.impl;

import com.angel.orchestrator_service.services.api.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.angel.orchestrator_service.ConstantsAndEnums.Constants.AUTHORIZATION_HEADER;
import static com.angel.orchestrator_service.ConstantsAndEnums.Constants.TOKEN_TYPE;

@Service
public class LogOutService implements LogoutHandler {

    private JwtService jwtService;

    @Autowired
    public LogOutService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_TYPE)) {
            return;
        }
        response.addCookie(this.jwtService.getCleanJwtRefreshCookie(request.getServletPath()));
        response.addHeader(AUTHORIZATION_HEADER, null);
        SecurityContextHolder.clearContext();
    }

}
