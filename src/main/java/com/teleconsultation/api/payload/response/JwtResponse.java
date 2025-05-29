package com.teleconsultation.api.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String email, String nom, String prenom, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.roles = roles;
    }
}