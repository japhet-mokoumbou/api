package com.teleconsultation.api.payload.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private Long id;
    private String titre;
    private String message;
    private String type;
    private LocalDateTime dateCreation;
    private boolean lue = false;
    
    // Constructeur pour les nouvelles notifications
    public NotificationMessage(Long id, String titre, String message, String type, LocalDateTime dateCreation) {
        this.id = id;
        this.titre = titre;
        this.message = message;
        this.type = type;
        this.dateCreation = dateCreation;
        this.lue = false;
    }
}