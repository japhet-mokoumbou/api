package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Trouver toutes les notifications d'un utilisateur, triées par date décroissante
    List<Notification> findByDestinataire_IdOrderByDateCreationDesc(Long destinataireId);
    
    // Trouver les notifications non lues d'un utilisateur
    List<Notification> findByDestinataire_IdAndLueFalseOrderByDateCreationDesc(Long destinataireId);
    
    // Compter les notifications non lues d'un utilisateur
    long countByDestinataire_IdAndLueFalse(Long destinataireId);
    
    // Supprimer les anciennes notifications (plus de 30 jours)
    void deleteByDateCreationBefore(java.time.LocalDateTime date);
}