package com.angel.orchestrator_service.services.api;

import com.angel.orchestrator_service.pojo.Credentials;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(String userName);
    String generateToken(Map<String, Object> extraClaims, String userName);
    String generateRefreshToken(String userName);
    boolean isTokenValid(String token, String userName);
    Cookie generateRefreshJwtCookie(String refreshToken, String path);
    String getJwtRefreshFromCookies(HttpServletRequest request);
    Cookie getCleanJwtRefreshCookie(String path);
    void setTokens(Credentials credentials, HttpServletRequest request, HttpServletResponse response);
}
