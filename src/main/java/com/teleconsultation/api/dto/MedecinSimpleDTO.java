package com.teleconsultation.api.dto;

import lombok.Data;

@Data
public class MedecinSimpleDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String specialite;
}
