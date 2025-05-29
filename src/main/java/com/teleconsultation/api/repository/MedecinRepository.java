// MedecinRepository.java - Mise à jour
package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    
    // Recherche par nom, prénom ou spécialité avec filtres de validation
    @Query("SELECT m FROM Medecin m WHERE " +
           "(LOWER(m.nom) LIKE LOWER(CONCAT('%', :nom, '%')) OR " +
           "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :prenom, '%')) OR " +
           "LOWER(m.specialite) LIKE LOWER(CONCAT('%', :specialite, '%'))) AND " +
           "m.compteValide = :compteValide AND m.actif = :actif")
    List<Medecin> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrSpecialiteContainingIgnoreCaseAndCompteValideAndActif(
            @Param("nom") String nom, 
            @Param("prenom") String prenom, 
            @Param("specialite") String specialite,
            @Param("compteValide") boolean compteValide,
            @Param("actif") boolean actif);
    
    // Tous les médecins validés et actifs
    List<Medecin> findByCompteValideAndActifOrderByNomAsc(boolean compteValide, boolean actif);
}