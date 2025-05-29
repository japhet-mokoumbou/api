// RendezVousRequest.java
package com.teleconsultation.api.payload.request;

import lombok.Data;


import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class RendezVousRequest {
    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;
    
    @NotNull(message = "L'ID du médecin est obligatoire")
    private Long medecinId;
    
    @NotNull(message = "La date et l'heure sont obligatoires")
    private LocalDateTime dateHeure;
    
    @NotNull(message = "La durée est obligatoire")
    @Min(value = 15, message = "La durée minimum est de 15 minutes")
    private Integer duree;
    
    @NotBlank(message = "Le motif est obligatoire")
    private String motif;
}
