package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.DossierMedical;
import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.Patient;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.payload.request.CreateDossierRequest;
import com.teleconsultation.api.payload.response.MessageResponse;
import com.teleconsultation.api.repository.MedecinRepository;
import com.teleconsultation.api.repository.PatientRepository;
import com.teleconsultation.api.service.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medecin")
public class MedecinController {
    
    @Autowired
    private MedecinService medecinService;
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedecinRepository medecinRepository;
    
    // ===== ENDPOINTS EXISTANTS POUR LES RENDEZ-VOUS =====
    
    @GetMapping("/{medecinId}/rendez-vous/prochains")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<List<RendezVous>> getProchainRendezVous(@PathVariable Long medecinId) {
        List<RendezVous> rendezVous = medecinService.findProchainRendezVous(medecinId);
        return ResponseEntity.ok(rendezVous);
    }
    
    @GetMapping("/{medecinId}/statistiques")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> getStatistiques(@PathVariable Long medecinId) {
        Map<String, Object> stats = medecinService.getStatistiques(medecinId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/{medecinId}/rendez-vous")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<List<RendezVous>> getAllRendezVous(@PathVariable Long medecinId) {
        List<RendezVous> rendezVous = medecinService.findAllRendezVous(medecinId);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/medecins")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<Medecin>> getAllMedecins() {
        return ResponseEntity.ok(medecinRepository.findAll());
    }

    @GetMapping("/patients")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(medecinService.getAllPatients());
    }

    // ===== NOUVEAUX ENDPOINTS POUR LES DOSSIERS MÉDICAUX =====
    
    /**
     * Obtenir les dossiers médicaux d'un médecin
     */
    @GetMapping("/{medecinId}/dossiers")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<List<DossierMedical>> getDossiersMedicaux(@PathVariable Long medecinId) {
        List<DossierMedical> dossiers = medecinService.findDossiersMedicaux(medecinId);
        return ResponseEntity.ok(dossiers);
    }
    
    /**
     * Créer un dossier médical
     */
    @PostMapping("/{medecinId}/dossiers")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<?> creerDossierMedical(
            @PathVariable Long medecinId,
            @jakarta.validation.Valid @RequestBody CreateDossierRequest request) {
        try {
            DossierMedical dossier = medecinService.creerDossierPourPatient(medecinId, request.getPatientId());
            return ResponseEntity.status(HttpStatus.CREATED).body(dossier);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
    
    /**
     * Obtenir les patients sans dossier médical
     */
    @GetMapping("/{medecinId}/patients-sans-dossier")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<List<Patient>> getPatientsSansDossier(@PathVariable Long medecinId) {
        List<Patient> patients = medecinService.findPatientsSansDossier();
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Rechercher des patients
     */
    @GetMapping("/{medecinId}/patients/search")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<List<Patient>> searchPatients(
            @PathVariable Long medecinId,
            @RequestParam(required = false) String query) {
        List<Patient> patients = medecinService.searchPatients(query);
        return ResponseEntity.ok(patients);
    }
}