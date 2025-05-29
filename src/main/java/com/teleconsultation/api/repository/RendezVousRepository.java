// RendezVousRepository.java - Mise à jour
package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.Patient;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.models.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    
    List<RendezVous> findByMedecinOrderByDateHeureDesc(Medecin medecin);
    
    List<RendezVous> findByPatientOrderByDateHeureDesc(Patient patient);
    
    List<RendezVous> findByPatientAndStatutOrderByDateHeureDesc(Patient patient, StatutRendezVous statut);
    
    List<RendezVous> findByDateHeureBetweenOrderByDateHeureAsc(LocalDateTime debut, LocalDateTime fin);
    
    List<RendezVous> findTop5ByMedecinAndDateHeureAfterAndStatutOrderByDateHeureAsc(
            Medecin medecin, LocalDateTime dateHeure, StatutRendezVous statut);
            
    List<RendezVous> findTop5ByPatientAndDateHeureAfterAndStatutOrderByDateHeureAsc(
            Patient patient, LocalDateTime dateHeure, StatutRendezVous statut);
    
    long countByMedecinAndStatut(Medecin medecin, StatutRendezVous statut);
    
    long countByMedecinAndDateHeureBetweenAndStatut(
            Medecin medecin, LocalDateTime debut, LocalDateTime fin, StatutRendezVous statut);
    
    long countByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);
    
    long countByStatut(StatutRendezVous statut);
    
    List<RendezVous> findAllByOrderByDateHeureDesc();
}