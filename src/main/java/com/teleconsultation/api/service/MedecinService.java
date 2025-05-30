package com.teleconsultation.api.service;

import com.teleconsultation.api.models.*;
import com.teleconsultation.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    public Medecin findById(Long id) {
        return medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + id));
    }
    
    public List<RendezVous> findAllRendezVous(Long medecinId) {
        Medecin medecin = findById(medecinId);
        return rendezVousRepository.findByMedecinOrderByDateHeureDesc(medecin);
    }

    // Méthodes simulées pour les rendez-vous (comme dans votre script)
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

    // ===== NOUVELLES MÉTHODES POUR LES DOSSIERS MÉDICAUX =====
    
    /**
     * Trouve tous les dossiers médicaux d'un médecin
     */
    public List<DossierMedical> findDossiersMedicaux(Long medecinId) {
        Medecin medecin = findById(medecinId);
        return dossierMedicalRepository.findByMedecinOrderByDateCreationDesc(medecin);
    }
    
    /**
     * Trouve les patients qui n'ont pas encore de dossier médical
     */
    public List<Patient> findPatientsSansDossier() {
        return patientRepository.findPatientsWithoutDossier();
    }
    
    /**
     * Crée un dossier médical pour un patient
     */
    @Transactional
    public DossierMedical creerDossierPourPatient(Long medecinId, Long patientId) {
        Medecin medecin = findById(medecinId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + patientId));
        
        // Vérifier si un dossier existe déjà pour ce patient
        Optional<DossierMedical> existingDossier = dossierMedicalRepository.findByPatient(patient);
        
        if (existingDossier.isPresent()) {
            throw new RuntimeException("Un dossier médical existe déjà pour ce patient");
        }
        
        DossierMedical dossier = new DossierMedical();
        dossier.setPatient(patient);
        dossier.setMedecin(medecin);
        
        return dossierMedicalRepository.save(dossier);
    }
    
    /**
     * Trouve un dossier médical par ID
     */
    public DossierMedical findDossierById(Long dossierId) {
        return dossierMedicalRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier médical non trouvé avec l'ID: " + dossierId));
    }
    
    /**
     * Recherche des patients
     */
    public List<Patient> searchPatients(String query) {
        if (query == null || query.trim().isEmpty()) {
            return patientRepository.findByCompteValideAndActif(true, true);
        }
        return patientRepository.findBySearchQuery("%" + query.toLowerCase() + "%");
    }
    
    /**
     * Obtient tous les patients validés et actifs
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findByCompteValideAndActif(true, true);
    }
}