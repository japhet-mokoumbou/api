// PatientRepository.java - Mise à jour
package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Recherche par nom, prénom ou email avec filtres de validation
    @Query("SELECT p FROM Patient p WHERE " +
           "(LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%')) OR " +
           "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :prenom, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "p.compteValide = :compteValide AND p.actif = :actif")
    List<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrEmailContainingIgnoreCaseAndCompteValideAndActif(
            @Param("nom") String nom, 
            @Param("prenom") String prenom, 
            @Param("email") String email,
            @Param("compteValide") boolean compteValide,
            @Param("actif") boolean actif);
    
    // Tous les patients validés et actifs
    List<Patient> findByCompteValideAndActifOrderByNomAsc(boolean compteValide, boolean actif);
}
