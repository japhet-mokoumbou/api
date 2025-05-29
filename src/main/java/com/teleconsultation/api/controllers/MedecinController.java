package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.Patient;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.repository.MedecinRepository;
import com.teleconsultation.api.repository.PatientRepository;
import com.teleconsultation.api.service.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Supprimer ou corriger l'endpoint en double :
    // @GetMapping("/medecin/patients")  // À supprimer
@PreAuthorize("hasRole('MEDECIN')")
public List<Patient> getAllPatients() {
    return patientRepository.findAll();
}
}