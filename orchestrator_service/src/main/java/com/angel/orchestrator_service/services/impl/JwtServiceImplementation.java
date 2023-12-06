package com.angel.orchestrator_service.services.impl;

import com.angel.orchestrator_service.pojo.Credentials;
import com.angel.orchestrator_service.services.api.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.angel.orchestrator_service.ConstantsAndEnums.Constants.AUTHORIZATION_HEADER;

@Service
public class JwtServiceImplementation implements JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh-token.cookie}")
    private String refreshCookie;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(String userName) {
        return generateToken(new HashMap<>(), userName);
    }

    @Override
    public String generateToken(
        Map<String, Object> extraClaims,
        String userName
    ) {
        return buildToken(extraClaims, userName, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(
        String userName
    ) {
        return buildToken(new HashMap<>(), userName, refreshExpiration);
    }

    private String buildToken(
        Map<String, Object> extraClaims,
        String userName,
        long expiration
    ) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userName)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public boolean isTokenValid(String token, String userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Cookie generateRefreshJwtCookie(String refreshToken, String path) {
        return generateCookie(refreshCookie, refreshToken, path);
    }

    @Override
    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, refreshCookie);
    }

    @Override
    public Cookie getCleanJwtRefreshCookie(String path) {
        Cookie cookie = new Cookie(refreshCookie, null);
        cookie.setPath(path);
        return cookie;
    }

    @Override
    public void setTokens(Credentials credentials, HttpServletRequest request, HttpServletResponse response) {
        String token = this.generateToken(credentials.getUsername());
        String refreshToken = this.generateRefreshToken(credentials.getUsername());
        response.addHeader(AUTHORIZATION_HEADER, token);
        System.out.println(response.getHeader(AUTHORIZATION_HEADER));
        response.addCookie(this.generateRefreshJwtCookie(refreshToken,
                                                                    request.getServletPath()));
    }

    private Cookie generateCookie(String name, String value, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    protected String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
}
