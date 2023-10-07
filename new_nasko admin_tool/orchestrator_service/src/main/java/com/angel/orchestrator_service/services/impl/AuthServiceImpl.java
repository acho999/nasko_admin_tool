package com.angel.orchestrator_service.services.impl;

import com.angel.orchestrator_service.ConstantsAndEnums.RolesEnum;
import com.angel.orchestrator_service.DTO.UserDto;
import com.angel.orchestrator_service.entities.Role;
import com.angel.orchestrator_service.entities.User;
import com.angel.orchestrator_service.pojo.Credentials;
import com.angel.orchestrator_service.repositories.RolesRepository;
import com.angel.orchestrator_service.repositories.UsersRepository;
import com.angel.orchestrator_service.services.api.AuthService;
import com.angel.orchestrator_service.services.api.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsersRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final RolesRepository rolesRepository;
    private final UserDetailsService usersDetailsService;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UsersRepository repository,
                           BCryptPasswordEncoder encoder,
                           RolesRepository rolesRepository,
                           UserDetailsService usersDetailsService,
                           AuthenticationManager manager,
                           JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.rolesRepository = rolesRepository;
        this.usersDetailsService = usersDetailsService;
        this.manager = manager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UserDto register(UserDto user, HttpServletRequest request,
                            HttpServletResponse response) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = format.format(new Date());
            Date date = format.parse(dateString);
            List<Role> authorities = rolesRepository.
                findAll()
                .stream()
                .filter(x -> x.getName().contains("USER"))
                .collect(Collectors.toList());

            User entity = User.builder()
                .userName(user.getUserName())
                .encryptedPassword(encoder.encode(user.getPassword()))
                .authorities(authorities)
                .email(user.getEmail())
                .date_created(date)
                .build();
            repository.saveAndFlush(entity);

            UserDto returnUser = UserDto.of(entity);

            return returnUser;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public UserDto update(UserDto user) {

        User userEntity = null;
        UserDto returnObject = null;

        try {

            userEntity = this.repository.findById(user.getId()).get();

            Role role = user.getAuthorities()
                .stream()
                .filter(x -> x.getName()
                    .equals(RolesEnum.ADMIN_UPDATE.name()))
                .findFirst().orElse(null);

            List<Role> roles = new ArrayList<>();
            roles.add(role);

            userEntity.setAuthorities(roles);

            returnObject = UserDto.builder()
                .authorities(userEntity.getRoles())
                .id(userEntity.getId())
                .date_created(userEntity.getDate_created())
                .userName(userEntity.getUsername())
                .setEncryptedPassword(userEntity.getEncryptedPassword())
                .email(userEntity.getEmail())
                .build();

            this.repository.saveAndFlush(userEntity);

            return returnObject;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        return returnObject;
    }

    @Override
    @Transactional
    public boolean delete(String id) {

        Optional<User> userOptional = null;

        try {
            this.repository.deleteById(id);

            userOptional = this.repository.findById(id);

            if (userOptional.get() != null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        return true;

    }

    @Override
    @Transactional
    public UserDto getUserById(String id) {

        UserDto userDetails = null;
        User user = null;
        try {

            user = this.repository.findUserById(id);

            if (user != null) {

                userDetails = UserDto.builder()
                    .password(user.getEncryptedPassword())
                    .setEncryptedPassword(user.getEncryptedPassword())
                    .email(user.getEmail())
                    .userName(user.getUsername())
                    .id(id)
                    .date_created(user.getDate_created())
                    .authorities(user.getRoles())
                    .build();

                return userDetails;

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {

        UserDto userDetails = null;
        User user = null;
        try {

            user = this.repository.findUserByEmail(email);

            if (user != null) {
                userDetails = UserDto.builder()
                    .password(user.getEncryptedPassword())
                    .setEncryptedPassword(user.getEncryptedPassword())
                    .email(user.getEmail())
                    .userName(user.getUsername())
                    .id(user.getId())
                    .date_created(user.getDate_created())
                    .authorities(user.getRoles())
                    .build();

                return userDetails;

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return null;
    }

    @Override
    @Transactional
    public void login(Credentials credentials, HttpServletRequest request,
                             HttpServletResponse response) {

        this.manager.authenticate(new UsernamePasswordAuthenticationToken(
            credentials.getUsername(),
            credentials.getPassword(),
            null));

        this.jwtService.setTokens(credentials, request, response);
    }
}