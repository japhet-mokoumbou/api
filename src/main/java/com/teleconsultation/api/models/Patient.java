package com.teleconsultation.api.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patients")
public class Patient extends User {

    private String numeroSecuriteSociale;

    private LocalDate dateNaissance;

    private String adresse;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<DossierMedical> dossiersMedicaux = new ArrayList<>();

    @OneToMany(mappedBy = "patient")
    private List<RendezVous> rendezVous = new ArrayList<>();

    // Getters and Setters
    public String getNumeroSecuriteSociale() {
        return numeroSecuriteSociale;
    }

    public void setNumeroSecuriteSociale(String numeroSecuriteSociale) {
        this.numeroSecuriteSociale = numeroSecuriteSociale;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<DossierMedical> getDossiersMedicaux() {
        return dossiersMedicaux;
    }

    public void setDossiersMedicaux(List<DossierMedical> dossiersMedicaux) {
        this.dossiersMedicaux = dossiersMedicaux;
    }

    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(List<RendezVous> rendezVous) {
        this.rendezVous = rendezVous;
    }

    // equals and hashCode (basés sur l'ID hérité)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(numeroSecuriteSociale, patient.numeroSecuriteSociale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numeroSecuriteSociale);
    }

    // toString
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", nom=" + getNom() +
                ", prenom=" + getPrenom() +
                ", email=" + getEmail() +
                ", numeroSecuriteSociale='" + numeroSecuriteSociale + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
