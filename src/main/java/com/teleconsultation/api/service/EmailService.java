package com.teleconsultation.api.service;

import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender emailSender;
    
    public void envoyerEmailNouvelleInscription(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("admin@teleconsultation.com"); // Adresse de l'administrateur
        message.setSubject("Nouvelle inscription à valider");
        message.setText("Bonjour,\n\n" +
                "Un nouvel utilisateur s'est inscrit sur la plateforme de téléconsultation.\n\n" +
                "Nom: " + user.getNom() + " " + user.getPrenom() + "\n" +
                "Email: " + user.getEmail() + "\n\n" +
                "Veuillez vous connecter pour valider ou rejeter cette inscription.\n\n" +
                "Cordialement,\n" +
                "L'équipe de téléconsultation");
        
        emailSender.send(message);
    }
    
    public void envoyerEmailCompteValide(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Votre compte a été validé");
        message.setText("Bonjour " + user.getPrenom() + ",\n\n" +
                "Nous avons le plaisir de vous informer que votre compte sur la plateforme de téléconsultation a été validé.\n\n" +
                "Vous pouvez désormais vous connecter avec votre email et votre mot de passe.\n\n" +
                "Cordialement,\n" +
                "L'équipe de téléconsultation");
        
        emailSender.send(message);
    }
    
    public void envoyerEmailCompteRejete(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Votre demande d'inscription");
        message.setText("Bonjour " + user.getPrenom() + ",\n\n" +
                "Nous sommes désolés de vous informer que votre demande d'inscription sur la plateforme de téléconsultation n'a pas été acceptée.\n\n" +
                "Pour plus d'informations, veuillez nous contacter.\n\n" +
                "Cordialement,\n" +
                "L'équipe de téléconsultation");
        
        emailSender.send(message);
    }
    
    public void envoyerNotificationNouveauRendezVous(RendezVous rendezVous) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(rendezVous.getMedecin().getEmail());
        message.setSubject("Nouveau rendez-vous à valider");
        message.setText("Bonjour Dr. " + rendezVous.getMedecin().getPrenom() + " " + rendezVous.getMedecin().getNom() + ",\n\n" +
                "Un nouveau rendez-vous a été planifié pour vous:\n\n" +
                "Patient: " + rendezVous.getPatient().getNom() + " " + rendezVous.getPatient().getPrenom() + "\n" +
                "Date: " + rendezVous.getDateHeure() + "\n" +
                "Motif: " + rendezVous.getMotif() + "\n\n" +
                "Veuillez vous connecter pour valider ou rejeter ce rendez-vous.\n\n" +
                "Cordialement,\n" +
                "L'équipe de téléconsultation");
        
        emailSender.send(message);
    }
    
    public void envoyerNotificationRendezVousValide(RendezVous rendezVous) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(rendezVous.getPatient().getEmail());
        message.setSubject("Votre rendez-vous a été validé");
        message.setText("Bonjour " + rendezVous.getPatient().getPrenom() + ",\n\n" +
                "Nous avons le plaisir de vous informer que votre rendez-vous a été validé:\n\n" +
                "Médecin: Dr. " + rendezVous.getMedecin().getNom() + " " + rendezVous.getMedecin().getPrenom() + "\n" +
                "Date: " + rendezVous.getDateHeure() + "\n" +
                "Motif: " + rendezVous.getMotif() + "\n\n" +
                "Cordialement,\n" +
                "L'équipe de téléconsultation");
        
        emailSender.send(message);
    }
    
    public void envoyerNotificationRendezVousRejete(RendezVous rendezVous, String motif) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(rendezVous.getPatient().getEmail());
        message.setSubject("Votre rendez-vous a été rejeté");
        message.setText("Bonjour " + rendezVous.getPatient().getPrenom() + ",\n\n" +
                "Nous sommes désolés de vous informer que votre demande de rendez-vous n'a pas pu être acceptée:\n\n" +
                "Médecin: Dr. " + rendezVous.getMedecin().getNom() + " " + rendezVous.getMedecin().getPrenom() + "\n" +
                "Date: " + rendezVous.getDateHeure() + "\n" +
                "Motif initial: " + rendezVous.getMotif() + "\n\n" +
                "Raison du rejet: " + (motif != null && !motif.isEmpty() ? motif : "Non spécifiée") + "\n\n" +
                "Veuillez nous contacter ou planifier un nouveau rendez-vous.\n\n" +
                "Cordialement,\n" +
                "L'équipe de téléconsultation");
        
        emailSender.send(message);
    }

    // Ajouter cette méthode dans EmailService.java

public void envoyerNotificationEmail(User destinataire, String titre, String contenu) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(destinataire.getEmail());
    message.setSubject(titre);
    message.setText("Bonjour " + destinataire.getPrenom() + ",\n\n" +
            contenu + "\n\n" +
            "Connectez-vous à votre espace pour plus de détails.\n\n" +
            "Cordialement,\n" +
            "L'équipe de téléconsultation");
    
    try {
        emailSender.send(message);
    } catch (Exception e) {
        // Logger l'erreur mais ne pas bloquer le processus de notification
        //logger.error("Erreur lors de l'envoi de l'email de notification", e);
    }
}
}