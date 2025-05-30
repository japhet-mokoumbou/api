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
import java.util.Optional;

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
    
    @Transactional(readOnly = true)
    public List<RendezVous> findAll() {
        try {
            System.out.println("Service: Récupération de tous les rendez-vous avec JOIN FETCH");
            
            // Utiliser la requête optimisée avec JOIN FETCH
            List<RendezVous> rendezVous = rendezVousRepository.findAllWithPatientAndMedecinOrderByDateHeureDesc();
            
            System.out.println("Service: " + rendezVous.size() + " rendez-vous trouvés");
            
            // Vérifier que les relations sont bien chargées
            for (RendezVous rdv : rendezVous) {
                System.out.println("Service: RDV " + rdv.getId() + 
                    " - Patient: " + (rdv.getPatient() != null ? rdv.getPatient().getNom() + " " + rdv.getPatient().getPrenom() : "null") +
                    " - Médecin: " + (rdv.getMedecin() != null ? rdv.getMedecin().getNom() + " " + rdv.getMedecin().getPrenom() : "null") +
                    " - Date: " + rdv.getDateHeure() + 
                    " - Durée: " + rdv.getDuree() + 
                    " - Motif: " + rdv.getMotif() + 
                    " - Statut: " + rdv.getStatut());
            }
            
            return rendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.findAll():");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des rendez-vous", e);
        }
    }
    
    @Transactional(readOnly = true)
    public RendezVous findById(Long id) {
        try {
            System.out.println("Service: Recherche du rendez-vous ID: " + id);
            
            // Utiliser la requête optimisée avec JOIN FETCH
            Optional<RendezVous> rendezVousOpt = rendezVousRepository.findByIdWithPatientAndMedecin(id);
            
            if (!rendezVousOpt.isPresent()) {
                throw new RuntimeException("Rendez-vous non trouvé avec l'ID: " + id);
            }
            
            RendezVous rendezVous = rendezVousOpt.get();
            
            System.out.println("Service: Rendez-vous trouvé - ID: " + rendezVous.getId() +
                " - Patient: " + (rendezVous.getPatient() != null ? rendezVous.getPatient().getNom() : "null") +
                " - Médecin: " + (rendezVous.getMedecin() != null ? rendezVous.getMedecin().getNom() : "null"));
            
            return rendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.findById(" + id + "):");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche du rendez-vous", e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<RendezVous> findByMedecinId(Long medecinId) {
        try {
            System.out.println("Service: Recherche des rendez-vous pour le médecin ID: " + medecinId);
            
            // Vérifier que le médecin existe
            Medecin medecin = medecinRepository.findById(medecinId)
                    .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + medecinId));
            
            // Utiliser la requête optimisée
            List<RendezVous> rendezVous = rendezVousRepository.findByMedecinIdWithPatientAndMedecinOrderByDateHeureDesc(medecinId);
            
            System.out.println("Service: " + rendezVous.size() + " rendez-vous trouvés pour le médecin");
            return rendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.findByMedecinId(" + medecinId + "):");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous du médecin", e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<RendezVous> findByPatientId(Long patientId) {
        try {
            System.out.println("Service: Recherche des rendez-vous pour le patient ID: " + patientId);
            
            // Vérifier que le patient existe
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + patientId));
            
            // Utiliser la requête optimisée
            List<RendezVous> rendezVous = rendezVousRepository.findByPatientIdWithPatientAndMedecinOrderByDateHeureDesc(patientId);
            
            System.out.println("Service: " + rendezVous.size() + " rendez-vous trouvés pour le patient");
            return rendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.findByPatientId(" + patientId + "):");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous du patient", e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<RendezVous> findTodayRendezVous() {
        try {
            System.out.println("Service: Recherche des rendez-vous d'aujourd'hui");
            
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1);
            
            // Utiliser la requête optimisée avec paramètres
            List<RendezVous> rendezVous = rendezVousRepository.findByDateHeureBetweenWithPatientAndMedecinOrderByDateHeureAsc(startOfDay, endOfDay);
            
            System.out.println("Service: " + rendezVous.size() + " rendez-vous d'aujourd'hui trouvés");
            return rendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.findTodayRendezVous():");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la recherche des rendez-vous d'aujourd'hui", e);
        }
    }
    
    @Transactional
    public RendezVous save(RendezVousRequest rendezVousRequest) {
        try {
            System.out.println("Service: Création d'un nouveau rendez-vous");
            System.out.println("Service: Patient ID: " + rendezVousRequest.getPatientId());
            System.out.println("Service: Médecin ID: " + rendezVousRequest.getMedecinId());
            System.out.println("Service: Date/Heure: " + rendezVousRequest.getDateHeure());
            System.out.println("Service: Durée: " + rendezVousRequest.getDuree());
            System.out.println("Service: Motif: " + rendezVousRequest.getMotif());
            
            // Vérifier et récupérer le patient
            Patient patient = patientRepository.findById(rendezVousRequest.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + rendezVousRequest.getPatientId()));
            System.out.println("Service: Patient trouvé - " + patient.getNom() + " " + patient.getPrenom());
            
            // Vérifier et récupérer le médecin
            Medecin medecin = medecinRepository.findById(rendezVousRequest.getMedecinId())
                    .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + rendezVousRequest.getMedecinId()));
            System.out.println("Service: Médecin trouvé - " + medecin.getNom() + " " + medecin.getPrenom());
            
            // Créer le rendez-vous
            RendezVous rendezVous = new RendezVous();
            rendezVous.setPatient(patient);
            rendezVous.setMedecin(medecin);
            rendezVous.setDateHeure(rendezVousRequest.getDateHeure());
            rendezVous.setDuree(rendezVousRequest.getDuree());
            rendezVous.setMotif(rendezVousRequest.getMotif());
            rendezVous.setStatut(StatutRendezVous.PLANIFIE);
            
            // Sauvegarder
            RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
            System.out.println("Service: Rendez-vous sauvegardé avec l'ID: " + savedRendezVous.getId());
            
            // Envoyer une notification au médecin
            try {
                emailService.envoyerNotificationNouveauRendezVous(savedRendezVous);
                System.out.println("Service: Email de notification envoyé au médecin");
            } catch (Exception e) {
                // Log l'erreur mais ne pas faire échouer la création du rendez-vous
                System.err.println("Erreur lors de l'envoi de l'email de notification: " + e.getMessage());
            }
            
            return savedRendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.save():");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création du rendez-vous: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public RendezVous update(Long id, RendezVousRequest rendezVousRequest) {
        try {
            System.out.println("Service: Modification du rendez-vous ID: " + id);
            System.out.println("Service: Nouvelles données:");
            System.out.println("Service: Patient ID: " + rendezVousRequest.getPatientId());
            System.out.println("Service: Médecin ID: " + rendezVousRequest.getMedecinId());
            System.out.println("Service: Date/Heure: " + rendezVousRequest.getDateHeure());
            System.out.println("Service: Durée: " + rendezVousRequest.getDuree());
            System.out.println("Service: Motif: " + rendezVousRequest.getMotif());
            
            // Récupérer le rendez-vous existant
            RendezVous rendezVous = findById(id);
            
            // Vérifier et récupérer le patient
            Patient patient = patientRepository.findById(rendezVousRequest.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID: " + rendezVousRequest.getPatientId()));
            System.out.println("Service: Patient trouvé - " + patient.getNom() + " " + patient.getPrenom());
            
            // Vérifier et récupérer le médecin
            Medecin medecin = medecinRepository.findById(rendezVousRequest.getMedecinId())
                    .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec l'ID: " + rendezVousRequest.getMedecinId()));
            System.out.println("Service: Médecin trouvé - " + medecin.getNom() + " " + medecin.getPrenom());
            
            // Mettre à jour les champs
            rendezVous.setPatient(patient);
            rendezVous.setMedecin(medecin);
            rendezVous.setDateHeure(rendezVousRequest.getDateHeure());
            rendezVous.setDuree(rendezVousRequest.getDuree());
            rendezVous.setMotif(rendezVousRequest.getMotif());
            
            // Sauvegarder
            RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);
            System.out.println("Service: Rendez-vous modifié avec succès");
            
            return updatedRendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.update(" + id + "):");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification du rendez-vous: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public RendezVous valider(Long id) {
        try {
            System.out.println("Service: Validation du rendez-vous ID: " + id);
            
            RendezVous rendezVous = findById(id);
            rendezVous.setStatut(StatutRendezVous.VALIDE);
            
            RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);
            System.out.println("Service: Rendez-vous validé avec succès");
            
            // Envoyer une notification au patient
            try {
                emailService.envoyerNotificationRendezVousValide(updatedRendezVous);
                System.out.println("Service: Email de validation envoyé au patient");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de l'email de validation: " + e.getMessage());
            }
            
            return updatedRendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.valider(" + id + "):");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la validation du rendez-vous: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public RendezVous rejeter(Long id, String motif) {
        try {
            System.out.println("Service: Rejet du rendez-vous ID: " + id);
            if (motif != null) {
                System.out.println("Service: Motif du rejet: " + motif);
            }
            
            RendezVous rendezVous = findById(id);
            rendezVous.setStatut(StatutRendezVous.REJETE);
            
            RendezVous updatedRendezVous = rendezVousRepository.save(rendezVous);
            System.out.println("Service: Rendez-vous rejeté avec succès");
            
            // Envoyer une notification au patient avec le motif du rejet
            try {
                emailService.envoyerNotificationRendezVousRejete(updatedRendezVous, motif);
                System.out.println("Service: Email de rejet envoyé au patient");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de l'email de rejet: " + e.getMessage());
            }
            
            return updatedRendezVous;
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.rejeter(" + id + "):");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du rejet du rendez-vous: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        try {
            System.out.println("Service: Suppression du rendez-vous ID: " + id);
            
            RendezVous rendezVous = findById(id);
            rendezVousRepository.delete(rendezVous);
            
            System.out.println("Service: Rendez-vous supprimé avec succès");
        } catch (Exception e) {
            System.err.println("Erreur dans RendezVousService.delete(" + id + "):");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du rendez-vous: " + e.getMessage(), e);
        }
    }
}