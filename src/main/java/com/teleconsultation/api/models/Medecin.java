package com.teleconsultation.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "medecins")
public class Medecin extends User {

    private String specialite;

    private String numeroOrdre;

    @OneToMany(mappedBy = "medecin")
    private List<RendezVous> rendezVous = new ArrayList<>();

    @OneToMany(mappedBy = "medecin")
    private List<DossierMedical> dossiersMedicaux = new ArrayList<>();

    // Getters and Setters
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(List<RendezVous> rendezVous) {
        this.rendezVous = rendezVous;
    }

    public List<DossierMedical> getDossiersMedicaux() {
        return dossiersMedicaux;
    }

    public void setDossiersMedicaux(List<DossierMedical> dossiersMedicaux) {
        this.dossiersMedicaux = dossiersMedicaux;
    }

    // equals and hashCode (basés sur l'ID hérité + numéro d'ordre)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medecin)) return false;
        if (!super.equals(o)) return false;
        Medecin medecin = (Medecin) o;
        return Objects.equals(numeroOrdre, medecin.numeroOrdre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numeroOrdre);
    }

    // toString
    @Override
    public String toString() {
        return "Medecin{" +
                "id=" + getId() +
                ", nom=" + getNom() +
                ", prenom=" + getPrenom() +
                ", email=" + getEmail() +
                ", specialite='" + specialite + '\'' +
                ", numeroOrdre='" + numeroOrdre + '\'' +
                '}';
    }
}
