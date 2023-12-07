package com.angel.orchestrator_service.security;

import com.angel.orchestrator_service.ConstantsAndEnums.RolesEnum;
import com.angel.orchestrator_service.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final LogoutHandler logoutHandler;
    private final BCryptPasswordEncoder encoder;
    private final Environment environment;
    private final JwtAuthenticationFilter filter;

    @Autowired
    public SecurityConfig(
        LogoutHandler logoutHandler,
        BCryptPasswordEncoder encoder,
        Environment environment,
        JwtAuthenticationFilter filter) {
        this.logoutHandler = logoutHandler;
        this.encoder = encoder;
        this.environment = environment;
        this.filter = filter;
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control", "Content-Type","Access-Control-Allow-Origin"));
//        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//        //configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().cors().disable();
        http.headers().frameOptions().disable();
        http.authorizeHttpRequests()
            .antMatchers(this.environment.getProperty("api.users.register.url.path")).permitAll()
            .antMatchers(this.environment.getProperty("api.users.login.url.path")).permitAll()
            .antMatchers(this.environment.getProperty("api.roles.create.url.path")).permitAll()
            .antMatchers(this.environment.getProperty("api.roles.getAll.url.path")).permitAll()
            .antMatchers( this.environment.getProperty("api.users.userDetails.url.path")).hasAnyRole(RolesEnum.USER_READ.name(), RolesEnum.USER_DELETE.name(), RolesEnum.USER_CREATE.name(), RolesEnum.USER_UPDATE.name())
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class)
            .logout()
            .logoutUrl("/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.filter.getUserDetailsService());
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
