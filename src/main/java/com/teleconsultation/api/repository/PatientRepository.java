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

    // Méthode pour trouver les patients validés et actifs
    List<Patient> findByCompteValideAndActif(boolean compteValide, boolean actif);
    
    // Méthode de recherche par nom, prénom ou email
    @Query("SELECT p FROM Patient p WHERE p.compteValide = true AND p.actif = true AND " +
           "(LOWER(p.nom) LIKE :query OR LOWER(p.prenom) LIKE :query OR LOWER(p.email) LIKE :query)")
    List<Patient> findBySearchQuery(@Param("query") String query);

    // NOUVELLE MÉTHODE: Patients sans dossier médical
    @Query("SELECT p FROM Patient p WHERE p.compteValide = true AND p.actif = true AND " +
           "p.id NOT IN (SELECT d.patient.id FROM DossierMedical d)")
    List<Patient> findPatientsWithoutDossier();
}