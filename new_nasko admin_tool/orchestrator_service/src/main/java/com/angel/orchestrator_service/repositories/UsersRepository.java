package com.angel.orchestrator_service.repositories;

import com.angel.orchestrator_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {

    User findUserByUserName(String username) throws UsernameNotFoundException;
    User findUserById(String id);
    User findUserByEmail(String email);

}
