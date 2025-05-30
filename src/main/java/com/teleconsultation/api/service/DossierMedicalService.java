package com.teleconsultation.api.service;

import com.teleconsultation.api.models.*;
import com.teleconsultation.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DossierMedicalService {

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private ObservationRepository observationRepository;
    
    @Autowired
    private ImageDICOMRepository imageDICOMRepository;

    public List<DossierMedical> findByMedecinId(Long medecinId) {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + medecinId));
        return dossierMedicalRepository.findByMedecinOrderByDateCreationDesc(medecin);
    }

    public DossierMedical findById(Long id) {
        return dossierMedicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier médical non trouvé avec l'ID: " + id));
    }

    public Optional<DossierMedical> findByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + patientId));
        return dossierMedicalRepository.findByPatient(patient);
    }

    public Map<String, Object> getDossierInfo(Long patientId) {
        Map<String, Object> info = new HashMap<>();
        
        Optional<DossierMedical> dossierOpt = findByPatientId(patientId);
        
        if (dossierOpt.isPresent()) {
            DossierMedical dossier = dossierOpt.get();
            info.put("exists", true);
            info.put("nombreObservations", dossier.getObservations().size());
            info.put("nombreImages", dossier.getImagesDICOM().size());
            info.put("derniereMiseAJour", dossier.getDateCreation());
            
            // Dernière observation
            if (!dossier.getObservations().isEmpty()) {
                Observation derniere = dossier.getObservations().get(dossier.getObservations().size() - 1);
                info.put("derniereMiseAJour", derniere.getDateCreation());
            }
        } else {
            info.put("exists", false);
            info.put("nombreObservations", 0);
            info.put("nombreImages", 0);
            info.put("derniereMiseAJour", null);
        }
        
        return info;
    }

    @Transactional
    public DossierMedical createOrGetDossier(Long patientId, Long medecinId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + patientId));
        
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + medecinId));

        Optional<DossierMedical> existingDossier = dossierMedicalRepository.findByPatient(patient);
        
        if (existingDossier.isPresent()) {
            return existingDossier.get();
        } else {
            DossierMedical dossier = new DossierMedical();
            dossier.setPatient(patient);
            dossier.setMedecin(medecin);
            return dossierMedicalRepository.save(dossier);
        }
    }

    @Transactional
    public Observation ajouterObservation(Long dossierId, String contenu, Long medecinId) {
        DossierMedical dossier = findById(dossierId);
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + medecinId));

        Observation observation = new Observation();
        observation.setDossierMedical(dossier);
        observation.setMedecin(medecin);
        observation.setContenu(contenu);

        return observationRepository.save(observation);
    }

    @Transactional
    public ImageDICOM ajouterImageDICOM(Long dossierId, String orthancId, String description) {
        DossierMedical dossier = findById(dossierId);

        ImageDICOM image = new ImageDICOM();
        image.setDossierMedical(dossier);
        image.setOrthancId(orthancId);
        image.setDescription(description);

        return imageDICOMRepository.save(image);
    }

    public List<Observation> getObservationsByDossier(Long dossierId) {
        DossierMedical dossier = findById(dossierId);
        return observationRepository.findByDossierMedicalOrderByDateCreationDesc(dossier);
    }

    public List<ImageDICOM> getImagesByDossier(Long dossierId) {
        DossierMedical dossier = findById(dossierId);
        return imageDICOMRepository.findByDossierMedicalOrderByDateUploadDesc(dossier);
    }
    
}