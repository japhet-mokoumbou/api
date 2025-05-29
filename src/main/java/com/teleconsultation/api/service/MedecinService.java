package com.teleconsultation.api.service;

import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.models.StatutRendezVous;
import com.teleconsultation.api.repository.DossierMedicalRepository;
import com.teleconsultation.api.repository.MedecinRepository;
import com.teleconsultation.api.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;
    
    public Medecin findById(Long id) {
        return medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + id));
    }
    
   /*  public List<RendezVous> findProchainRendezVous(Long medecinId) {
        Medecin medecin = findById(medecinId);
        LocalDateTime now = LocalDateTime.now();
        
        // Récupérer les 5 prochains rendez-vous validés
        return rendezVousRepository.findTop5ByMedecinAndDateHeureAfterAndStatutOrderByDateHeureAsc(
                medecin, now, StatutRendezVous.VALIDE);
    } */
    
   /*  public Map<String, Object> getStatistiques(Long medecinId) {
        Medecin medecin = findById(medecinId);
        Map<String, Object> stats = new HashMap<>();
        
        // Nombre total de patients suivis
        long totalPatients = dossierMedicalRepository.countDistinctPatientsByMedecin(medecin);
        stats.put("totalPatients", totalPatients);
        
        // Nombre total de consultations (rendez-vous terminés)
        long totalConsultations = rendezVousRepository.countByMedecinAndStatut(medecin, StatutRendezVous.TERMINE);
        stats.put("totalConsultations", totalConsultations);
        
        // Nombre de rendez-vous aujourd'hui
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        long rendezVousAujourdhui = rendezVousRepository.countByMedecinAndDateHeureBetweenAndStatut(
                medecin, startOfDay, endOfDay, StatutRendezVous.VALIDE);
        stats.put("rendezVousAujourdhui", rendezVousAujourdhui);
        
        return stats;
    } */
    
    public List<RendezVous> findAllRendezVous(Long medecinId) {
        Medecin medecin = findById(medecinId);
        return rendezVousRepository.findByMedecinOrderByDateHeureDesc(medecin);
    }

    // Dans MedecinService.java, ajoutez ces méthodes pour les données simulées

public List<RendezVous> findProchainRendezVous(Long medecinId) {
    // Pour le moment, retournez une liste vide ou des données simulées
    return new ArrayList<>(); // Liste vide temporaire
}

public Map<String, Object> getStatistiques(Long medecinId) {
    // Données simulées pour le tableau de bord
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalPatients", 0);
    stats.put("totalConsultations", 0);
    stats.put("rendezVousAujourdhui", 0);
    return stats;
}
}