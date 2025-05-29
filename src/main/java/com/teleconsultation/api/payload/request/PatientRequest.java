package com.teleconsultation.api.payload.request;

import lombok.Data;


import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class PatientRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String nom;

    @NotBlank
    @Size(min = 2, max = 50)
    private String prenom;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String motDePasse;

    @Size(max = 20)
    private String telephone;

    @NotBlank
    private String numeroSecuriteSociale;

    @NotNull
    private LocalDate dateNaissance;

    @NotBlank
    private String adresse;
}