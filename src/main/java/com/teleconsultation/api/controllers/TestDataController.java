package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.Patient;
import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.models.StatutRendezVous;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestDataController {
    
    // Données de test pour les patients
    private List<Patient> createTestPatients() {
        List<Patient> patients = new ArrayList<>();
        
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setNom("Dupont");
        patient1.setPrenom("Jean");
        patient1.setEmail("jean.dupont@email.com");
        patient1.setDateNaissance(LocalDate.of(1980, 5, 15));
        patient1.setNumeroSecuriteSociale("1800515123456");
        patients.add(patient1);
        
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setNom("Martin");
        patient2.setPrenom("Marie");
        patient2.setEmail("marie.martin@email.com");
        patient2.setDateNaissance(LocalDate.of(1975, 8, 22));
        patient2.setNumeroSecuriteSociale("2750822654321");
        patients.add(patient2);
        
        Patient patient3 = new Patient();
        patient3.setId(3L);
        patient3.setNom("Durand");
        patient3.setPrenom("Pierre");
        patient3.setEmail("pierre.durand@email.com");
        patient3.setDateNaissance(LocalDate.of(1990, 12, 3));
        patient3.setNumeroSecuriteSociale("1901203789012");
        patients.add(patient3);
        
        return patients;
    }
    
    // Données de test pour les médecins
    private List<Medecin> createTestMedecins() {
        List<Medecin> medecins = new ArrayList<>();
        
        Medecin medecin1 = new Medecin();
        medecin1.setId(1L);
        medecin1.setNom("Docteur");
        medecin1.setPrenom("Martin");
        medecin1.setEmail("dr.martin@clinic.com");
        medecin1.setSpecialite("Généraliste");
        medecin1.setNumeroOrdre("12345");
        medecins.add(medecin1);
        
        Medecin medecin2 = new Medecin();
        medecin2.setId(2L);
        medecin2.setNom("Bernard");
        medecin2.setPrenom("Sophie");
        medecin2.setEmail("dr.bernard@clinic.com");
        medecin2.setSpecialite("Cardiologie");
        medecin2.setNumeroOrdre("67890");
        medecins.add(medecin2);
        
        Medecin medecin3 = new Medecin();
        medecin3.setId(3L);
        medecin3.setNom("Rousseau");
        medecin3.setPrenom("Paul");
        medecin3.setEmail("dr.rousseau@clinic.com");
        medecin3.setSpecialite("Dermatologie");
        medecin3.setNumeroOrdre("11111");
        medecins.add(medecin3);
        
        return medecins;
    }
    
    @GetMapping("/patients")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<Patient>> getAllPatients(@RequestParam(required = false) String q) {
        List<Patient> patients = createTestPatients();
        
        if (q != null && !q.isEmpty()) {
            patients = patients.stream()
                .filter(patient -> 
                    patient.getNom().toLowerCase().contains(q.toLowerCase()) ||
                    patient.getPrenom().toLowerCase().contains(q.toLowerCase()) ||
                    patient.getEmail().toLowerCase().contains(q.toLowerCase())
                )
                .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/medecins")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<Medecin>> getAllMedecins(@RequestParam(required = false) String q) {
        List<Medecin> medecins = createTestMedecins();
        
        if (q != null && !q.isEmpty()) {
            medecins = medecins.stream()
                .filter(medecin -> 
                    medecin.getNom().toLowerCase().contains(q.toLowerCase()) ||
                    medecin.getPrenom().toLowerCase().contains(q.toLowerCase()) ||
                    (medecin.getSpecialite() != null && medecin.getSpecialite().toLowerCase().contains(q.toLowerCase()))
                )
                .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(medecins);
    }
    
    @GetMapping("/rendez-vous")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        List<RendezVous> rendezVous = new ArrayList<>();
        List<Patient> patients = createTestPatients();
        List<Medecin> medecins = createTestMedecins();
        
        // Créer quelques rendez-vous de test
        RendezVous rdv1 = new RendezVous();
        rdv1.setId(1L);
        rdv1.setPatient(patients.get(0));
        rdv1.setMedecin(medecins.get(0));
        rdv1.setDateHeure(LocalDateTime.now().plusDays(1));
        rdv1.setDuree(30);
        rdv1.setMotif("Consultation générale");
        rdv1.setStatut(StatutRendezVous.PLANIFIE);
        rendezVous.add(rdv1);
        
        RendezVous rdv2 = new RendezVous();
        rdv2.setId(2L);
        rdv2.setPatient(patients.get(1));
        rdv2.setMedecin(medecins.get(1));
        rdv2.setDateHeure(LocalDateTime.now().plusDays(2));
        rdv2.setDuree(45);
        rdv2.setMotif("Contrôle cardiaque");
        rdv2.setStatut(StatutRendezVous.VALIDE);
        rendezVous.add(rdv2);
        
        RendezVous rdv3 = new RendezVous();
        rdv3.setId(3L);
        rdv3.setPatient(patients.get(2));
        rdv3.setMedecin(medecins.get(2));
        rdv3.setDateHeure(LocalDateTime.now().plusDays(3));
        rdv3.setDuree(30);
        rdv3.setMotif("Consultation dermatologique");
        rdv3.setStatut(StatutRendezVous.REJETE);
        rendezVous.add(rdv3);
        
        return ResponseEntity.ok(rendezVous);
    }
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = Map.of(
            "totalRendezVous", 15,
            "rendezVousEnAttente", 5,
            "rendezVousValides", 8,
            "rendezVousRejetes", 2
        );
        
        return ResponseEntity.ok(stats);
    }
}