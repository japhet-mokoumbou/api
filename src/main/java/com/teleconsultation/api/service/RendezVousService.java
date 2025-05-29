package com.teleconsultation.api.service;

import com.teleconsultation.api.models.*;
import com.teleconsultation.api.payload.request.RendezVousRequest;
import com.teleconsultation.api.repository.MedecinRepository;
import com.teleconsultation.api.repository.PatientRepository;
import com.teleconsultation.api.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RendezVousService {
    
    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private EmailService emailService;
    
    public List<RendezVous> findAll() {
        return rendezVousRepository.findAllByOrderByDateHeureDesc();
    }
    
    public RendezVous findById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé avec l'ID: " + id));
    }
    
    public List<RendezVous> findByMedecinId(Long medecinId) {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + medecinId));
        return rendezVousRepository.findByMedecinOrderByDateHeureDesc(medecin);
    }
    
    public List<RendezVous> findByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + patientId));
        return rendezVousRepository.findByPatientOrderByDateHeureDesc(patient);
    }
    
    public List<RendezVous> findTodayRendezVous() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return rendezVousRepository.findByDateHeureBetweenOrderByDateHeureAsc(startOfDay, endOfDay);
    }
    
    @Transactional
    public RendezVous save(RendezVousRequest rendezVousRequest) {
        Patient patient = patientRepository.findById(rendezVousRequest.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + rendezVousRequest.getPatientId()));
        
        Medecin medecin = medecinRepository.findById(rendezVousRequest.getMedecinId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + rendezVousRequest.getMedecinId()));
        
        RendezVous rendezVous = new RendezVous();
        rendezVous.setPatient(patient);
        rendezVous.setMedecin(medecin);
        rendezVous.setDateHeure(rendezVousRequest.getDateHeure());
        rendezVous.setDuree(rendezVousRequest.getDuree());
        rendezVous.setMotif(rendezVousRequest.getMotif());
        rendezVous.setStatut(StatutRendezVous.PLANIFIE);
        
        RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
        
        // Envoyer une notification au médecin
        try {
            emailService.envoyerNotificationNouveauRendezVous(savedRendezVous);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer la création du rendez-vous
            System.err.println("Erreur lors de l'envoi de l'email de notification: " + e.getMessage());
        }
        
        return savedRendezVous;
    }
    
    @Transactional
    public RendezVous update(Long id, RendezVousRequest rendezVousRequest) {
        RendezVous rendezVous = findById(id);
        
        Patient patient = patientRepository.findById(rendezVousRequest.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + rendezVousRequest.getPatientId()));
        
        Medecin medecin = medecinRepository.findById(rendezVousRequest.getMedecinId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + rendezVousRequest.getMedecinId()));
        
        rendezVous.setPatient(patient);
        rendezVous.setMedecin(medecin);
        rendezVous.setDateHeure(rendezVousRequest.getDateHeure());
        rendezVous.setDuree(rendezVousRequest.getDuree());
        rendezVous.setMotif(rendezVousRequest.getMotif());
        
        return rendezVousRepository.save(rendezVous);
    }
    
    @Transactional
    public RendezVous valider(Long id) {
        RendezVous rendezVous = findById(id);
        rendezVous.setStatut(StatutRendezVous.VALIDE);
        
        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);
        
        // Envoyer une notification au patient
        try {
            emailService.envoyerNotificationRendezVousValide(updatedRendezVous);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de validation: " + e.getMessage());
        }
        
        return updatedRendezVous;
    }
    
    @Transactional
    public RendezVous rejeter(Long id, String motif) {
        RendezVous rendezVous = findById(id);
        rendezVous.setStatut(StatutRendezVous.REJETE);
        
        RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);
        
        // Envoyer une notification au patient avec le motif du rejet
        try {
            emailService.envoyerNotificationRendezVousRejete(updatedRendezVous, motif);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de rejet: " + e.getMessage());
        }
        
        return updatedRendezVous;
    }
    
    @Transactional
    public void delete(Long id) {
        RendezVous rendezVous = findById(id);
        rendezVousRepository.delete(rendezVous);
    }
}