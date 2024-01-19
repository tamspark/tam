package com.sparklab.TAM.repositories;

import com.sparklab.TAM.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByConfirmationToken(String token);

    boolean existsByEmail(String email);

    Optional<User> findUsersByEmail(String email);

    Optional<User> findUserByEmailAndIsEnabledTrue(String email);

    User findByForgetPasswordToken(String forgetPasswordToken);


}
