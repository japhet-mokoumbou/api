package com.teleconsultation.api.service;

import com.teleconsultation.api.models.*;
import com.teleconsultation.api.payload.notification.NotificationMessage;
import com.teleconsultation.api.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    @Lazy
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private EmailService emailService;

    /**
     * Créer et envoyer une notification
     */
    @Transactional
    public Notification sendNotification(User destinataire, String titre, String message, TypeNotification type) {
        // Créer la notification en base de données
        Notification notification = new Notification();
        notification.setDestinataire(destinataire);
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType(type);
        notification.setLue(false);
        notification.setDateCreation(LocalDateTime.now());
        
        Notification savedNotification = notificationRepository.save(notification);
        
        // Envoyer la notification en temps réel via WebSocket
        NotificationMessage notificationMessage = new NotificationMessage(
            savedNotification.getId(),
            titre,
            message,
            type.toString(),
            LocalDateTime.now()
        );
        
        messagingTemplate.convertAndSendToUser(
            destinataire.getEmail(),
            "/queue/notifications",
            notificationMessage
        );
        
        // Envoyer également par email selon le type
        if (shouldSendEmail(type)) {
            emailService.envoyerNotificationEmail(destinataire, titre, message);
        }
        
        return savedNotification;
    }

    /**
     * Récupérer les notifications d'un utilisateur
     */
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByDestinataire_IdOrderByDateCreationDesc(userId);
    }

    /**
     * Récupérer les notifications non lues d'un utilisateur
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByDestinataire_IdAndLueFalseOrderByDateCreationDesc(userId);
    }

    /**
     * Marquer une notification comme lue
     */
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setLue(true);
            notification.setDateLecture(LocalDateTime.now());
            notificationRepository.save(notification);
        });
    }

    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        LocalDateTime now = LocalDateTime.now();
        
        unreadNotifications.forEach(notification -> {
            notification.setLue(true);
            notification.setDateLecture(now);
        });
        
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Supprimer une notification
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    /**
     * Notifications pour les rendez-vous
     */
    public void notifierNouveauRendezVous(RendezVous rendezVous) {
        String titre = "Nouveau rendez-vous à valider";
        String message = String.format(
            "Un nouveau rendez-vous a été planifié avec %s %s pour le %s",
            rendezVous.getPatient().getNom(),
            rendezVous.getPatient().getPrenom(),
            rendezVous.getDateHeure().toString()
        );
        
        sendNotification(
            rendezVous.getMedecin(),
            titre,
            message,
            TypeNotification.NOUVEAU_RENDEZ_VOUS
        );
    }

    public void notifierRendezVousValide(RendezVous rendezVous) {
        String titre = "Rendez-vous confirmé";
        String message = String.format(
            "Votre rendez-vous du %s avec Dr. %s %s a été confirmé",
            rendezVous.getDateHeure().toString(),
            rendezVous.getMedecin().getNom(),
            rendezVous.getMedecin().getPrenom()
        );
        
        sendNotification(
            rendezVous.getPatient(),
            titre,
            message,
            TypeNotification.RENDEZ_VOUS_VALIDE
        );
    }

    public void notifierRendezVousRejete(RendezVous rendezVous, String motif) {
        String titre = "Rendez-vous annulé";
        String message = String.format(
            "Votre rendez-vous du %s avec Dr. %s %s a été annulé",
            rendezVous.getDateHeure().toString(),
            rendezVous.getMedecin().getNom(),
            rendezVous.getMedecin().getPrenom()
        );
        
        if (motif != null && !motif.isEmpty()) {
            message += ". Motif : " + motif;
        }
        
        sendNotification(
            rendezVous.getPatient(),
            titre,
            message,
            TypeNotification.RENDEZ_VOUS_REJETE
        );
    }

    /**
     * Notifications pour les dossiers médicaux
     */
    public void notifierNouvelleObservation(DossierMedical dossier, Observation observation) {
        String titre = "Nouvelle observation médicale";
        String message = String.format(
            "Dr. %s %s a ajouté une nouvelle observation à votre dossier médical",
            observation.getMedecin().getNom(),
            observation.getMedecin().getPrenom()
        );
        
        sendNotification(
            dossier.getPatient(),
            titre,
            message,
            TypeNotification.NOUVELLE_OBSERVATION
        );
    }

    public void notifierNouvelleImageDICOM(DossierMedical dossier) {
        String titre = "Nouvelle image médicale";
        String message = "Une nouvelle image DICOM a été ajoutée à votre dossier";
        
        sendNotification(
            dossier.getMedecin(),
            titre,
            message,
            TypeNotification.NOUVELLE_IMAGE
        );
    }

    /**
     * Détermine si une notification doit être envoyée par email
     */
    private boolean shouldSendEmail(TypeNotification type) {
        // Envoyer par email pour les notifications importantes
        return type == TypeNotification.NOUVEAU_RENDEZ_VOUS ||
               type == TypeNotification.RENDEZ_VOUS_VALIDE ||
               type == TypeNotification.RENDEZ_VOUS_REJETE ||
               type == TypeNotification.COMPTE_VALIDE;
    }
}