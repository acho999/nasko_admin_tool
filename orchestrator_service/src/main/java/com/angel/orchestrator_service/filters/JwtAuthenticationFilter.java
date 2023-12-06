package com.angel.orchestrator_service.filters;

import com.angel.orchestrator_service.services.api.JwtService;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.angel.orchestrator_service.ConstantsAndEnums.Constants.AUTHORIZATION_HEADER;
import static com.angel.orchestrator_service.ConstantsAndEnums.Constants.TOKEN_TYPE;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        final String userName;
        final boolean isNotBearer = authHeader == null ? true : !authHeader.startsWith(TOKEN_TYPE);
        if (authHeader == null || isNotBearer) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userName = jwtService.extractUsername(jwt);
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            String refreshToken = this.jwtService.getJwtRefreshFromCookies(request);
            if (jwtService.isTokenValid(jwt, userName)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities().stream().map(x->new SimpleGrantedAuthority("ROLE_" + x.getAuthority())).collect(
                        Collectors.toList())
                );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                response.addHeader(AUTHORIZATION_HEADER, jwt);
                response.addCookie(this.jwtService.generateRefreshJwtCookie(refreshToken,
                                                                            request.getContextPath()));
            } else {
                if (jwtService.isTokenValid(refreshToken, userName)){
                    response.addHeader(AUTHORIZATION_HEADER, this.jwtService.generateToken(userName));
                    response.addCookie(this.jwtService.generateRefreshJwtCookie(refreshToken,
                                                                                request.getContextPath()));
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
