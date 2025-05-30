package com.teleconsultation.api.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅ Import ajouté

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dossiers_medicaux")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ✅ Annotation ajoutée
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"dossiersMedicaux", "rendezVous", "hibernateLazyInitializer", "handler"}) // ✅ Évite les cycles
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medecin_id")
    @JsonIgnoreProperties({"dossiersMedicaux", "rendezVous", "hibernateLazyInitializer", "handler"}) // ✅ Évite les cycles
    private Medecin medecin;

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"dossierMedical"}) // ✅ Évite les cycles avec Observation
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"dossierMedical"}) // ✅ Évite les cycles avec ImageDICOM
    private List<ImageDICOM> imagesDICOM = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime dateCreation;

    // Constructeurs
    public DossierMedical() {}

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public List<ImageDICOM> getImagesDICOM() {
        return imagesDICOM;
    }

    public void setImagesDICOM(List<ImageDICOM> imagesDICOM) {
        this.imagesDICOM = imagesDICOM;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    // equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DossierMedical)) return false;
        DossierMedical that = (DossierMedical) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "DossierMedical{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getId() : null) +
                ", medecin=" + (medecin != null ? medecin.getId() : null) +
                ", dateCreation=" + dateCreation +
                '}';
    }
}