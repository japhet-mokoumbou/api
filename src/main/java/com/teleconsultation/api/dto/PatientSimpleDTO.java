package com.teleconsultation.api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PatientSimpleDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String numeroSecuriteSociale;
    private LocalDate dateNaissance;
    private String adresse;
}
