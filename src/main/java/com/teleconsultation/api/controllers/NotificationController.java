package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.Notification;
import com.teleconsultation.api.payload.response.MessageResponse;
import com.teleconsultation.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    @Lazy
    private NotificationService notificationService;
    
    /**
     * Récupérer toutes les notifications d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Récupérer les notifications non lues d'un utilisateur
     */
    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Marquer une notification comme lue
     */
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PATIENT', 'SECRETAIRE')")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(new MessageResponse("Notification marquée comme lue"));
    }
    
    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     */
    @PutMapping("/user/{userId}/read-all")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<?> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(new MessageResponse("Toutes les notifications ont été marquées comme lues"));
    }
    
    /**
     * Supprimer une notification
     */
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDECIN', 'PATIENT', 'SECRETAIRE')")
    public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(new MessageResponse("Notification supprimée"));
    }
}