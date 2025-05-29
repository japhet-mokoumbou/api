package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.ERole;
import com.teleconsultation.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findAllByCompteValide(boolean compteValide);
    long countByRoles_Name(ERole roleName);
}