package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.Patient;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.service.SecretaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/secretaire")
public class SecretaireController {
    
    @Autowired
    private SecretaireService secretaireService;
    
    @GetMapping("/rendez-vous/aujourd-hui")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<RendezVous>> getRendezVousAujourdhui() {
        try {
            List<RendezVous> rendezVous = secretaireService.findRendezVousAujourdhui();
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/statistiques")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<Map<String, Object>> getStatistiques() {
        try {
            Map<String, Object> stats = secretaireService.getStatistiques();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "totalRendezVous", 0,
                "rendezVousEnAttente", 0,
                "rendezVousValides", 0,
                "rendezVousRejetes", 0
            ));
        }
    }
    
    @GetMapping("/rendez-vous")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        try {
            List<RendezVous> rendezVous = secretaireService.findAllRendezVous();
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/patients/search")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam(required = false, defaultValue = "") String q) {
        try {
            List<Patient> patients = secretaireService.searchPatients(q);
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/medecins/search")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<Medecin>> searchMedecins(@RequestParam(required = false, defaultValue = "") String q) {
        try {
            List<Medecin> medecins = secretaireService.searchMedecins(q);
            return ResponseEntity.ok(medecins);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
}