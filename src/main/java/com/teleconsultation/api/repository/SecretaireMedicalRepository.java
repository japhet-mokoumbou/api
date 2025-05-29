package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.SecretaireMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretaireMedicalRepository extends JpaRepository<SecretaireMedical, Long> {
}