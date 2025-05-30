package com.teleconsultation.api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ObservationDTO {
    private Long id;
    private String contenu;
    private LocalDateTime dateCreation;
    private MedecinSimpleDTO medecin;
}
