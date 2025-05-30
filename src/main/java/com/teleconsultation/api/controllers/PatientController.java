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

    // ===== VOS MÉTHODES EXISTANTES =====

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
   /*  @GetMapping("/{patientId}/dossier-medical")
    @PreAuthorize("hasRole('PATIENT') and #patientId == authentication.principal.id")
    public ResponseEntity<DossierMedical> getDossierMedical(@PathVariable Long patientId) {
        Optional<DossierMedical> dossier = dossierMedicalService.findByPatientId(patientId);
        return dossier.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } */

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

    // ===== NOUVELLES MÉTHODES POUR LES MÉDECINS ET SECRÉTAIRES =====

    /**
     * Récupère tous les patients validés (pour médecins et secrétaires)
     */
    @GetMapping("/patients")
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE')")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientRepository.findByCompteValideAndActif(true, true);
        return ResponseEntity.ok(patients);
    }

    /**
     * Recherche de patients par nom, prénom ou email (pour médecins et secrétaires)
     */
    @GetMapping("/patients/search")
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE')")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam(required = false) String query) {
        List<Patient> patients;
        
        if (query == null || query.trim().isEmpty()) {
            // Si pas de requête, retourner tous les patients validés
            patients = patientRepository.findByCompteValideAndActif(true, true);
        } else {
            // Rechercher par nom, prénom ou email
            String searchQuery = "%" + query.toLowerCase().trim() + "%";
            patients = patientRepository.findBySearchQuery(searchQuery);
        }
        
        return ResponseEntity.ok(patients);
    }

    /**
     * Récupère un patient par son ID (pour médecins et secrétaires)
     */
    @GetMapping("/patients/{patientId}")
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE')")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}