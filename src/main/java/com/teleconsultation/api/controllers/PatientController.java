package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.DossierMedical;
import com.teleconsultation.api.models.Patient;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.repository.PatientRepository;
import com.teleconsultation.api.service.DossierMedicalService;
import com.teleconsultation.api.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private DossierMedicalService dossierMedicalService;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Crée un nouveau rendez-vous pour le patient connecté
     */

    /**
     * Récupère tous les rendez-vous du patient connecté
     */
    @GetMapping("/{patientId}/rendez-vous")
    @PreAuthorize("hasRole('PATIENT') and #patientId == authentication.principal.id")
    public ResponseEntity<List<RendezVous>> getRendezVousPatient(@PathVariable Long patientId) {
        List<RendezVous> rendezVous = rendezVousService.findByPatientId(patientId);
        return ResponseEntity.ok(rendezVous);
    }

    /**
     * Récupère le dossier médical du patient connecté
     */
    @GetMapping("/{patientId}/dossier-medical")
    @PreAuthorize("hasRole('PATIENT') and #patientId == authentication.principal.id")
    public ResponseEntity<DossierMedical> getDossierMedical(@PathVariable Long patientId) {
        Optional<DossierMedical> dossier = dossierMedicalService.findByPatientId(patientId);
        return dossier.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les informations du dossier médical (statistiques)
     */
    @GetMapping("/{patientId}/dossier-medical/info")
    @PreAuthorize("hasRole('PATIENT') and #patientId == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> getDossierMedicalInfo(@PathVariable Long patientId) {
        Map<String, Object> info = dossierMedicalService.getDossierInfo(patientId);
        return ResponseEntity.ok(info);
    }

    /**
     * Récupère les statistiques du patient (nombre de RDV, etc.)
     */
    @GetMapping("/{patientId}/statistiques")
    @PreAuthorize("hasRole('PATIENT') and #patientId == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> getStatistiquesPatient(@PathVariable Long patientId) {
        List<RendezVous> rendezVous = rendezVousService.findByPatientId(patientId);
        
        Map<String, Object> stats = Map.of(
            "totalRendezVous", rendezVous.size(),
            "rendezVousEnAttente", rendezVous.stream()
                .filter(rdv -> rdv.getStatut().name().equals("EN_ATTENTE"))
                .count(),
            "rendezVousValides", rendezVous.stream()
                .filter(rdv -> rdv.getStatut().name().equals("VALIDE"))
                .count(),
            "rendezVousRejetes", rendezVous.stream()
                .filter(rdv -> rdv.getStatut().name().equals("REJETE"))
                .count()
        );
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/patients")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }
}