package com.teleconsultation.api.service;

import com.teleconsultation.api.models.*;
import com.teleconsultation.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecretaireService {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private MedecinRepository medecinRepository;

    public List<RendezVous> findRendezVousAujourdhui() {
        try {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1);
            return rendezVousRepository.findByDateHeureBetweenOrderByDateHeureAsc(startOfDay, endOfDay);
        } catch (Exception e) {
            return List.of();
        }
    }

    public Map<String, Object> getStatistiques() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("totalRendezVous", rendezVousRepository.count());
            stats.put("rendezVousEnAttente", rendezVousRepository.countByStatut(StatutRendezVous.PLANIFIE));
            stats.put("rendezVousValides", rendezVousRepository.countByStatut(StatutRendezVous.VALIDE));
            stats.put("rendezVousRejetes", rendezVousRepository.countByStatut(StatutRendezVous.REJETE));
        } catch (Exception e) {
            stats.put("totalRendezVous", 0);
            stats.put("rendezVousEnAttente", 0);
            stats.put("rendezVousValides", 0);
            stats.put("rendezVousRejetes", 0);
        }
        
        return stats;
    }

    public List<RendezVous> findAllRendezVous() {
        try {
            return rendezVousRepository.findAllByOrderByDateHeureDesc();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Patient> searchPatients(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                // Si pas de requête, retourner tous les patients validés
                return patientRepository.findByCompteValideAndActifOrderByNomAsc(true, true);
            } else {
                // Recherche avec le terme saisi
                return patientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrEmailContainingIgnoreCaseAndCompteValideAndActif(
                        query, query, query, true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Medecin> searchMedecins(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                // Si pas de requête, retourner tous les médecins validés
                return medecinRepository.findByCompteValideAndActifOrderByNomAsc(true, true);
            } else {
                // Recherche avec le terme saisi
                return medecinRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrSpecialiteContainingIgnoreCaseAndCompteValideAndActif(
                        query, query, query, true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}