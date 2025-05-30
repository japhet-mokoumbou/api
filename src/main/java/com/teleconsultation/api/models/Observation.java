package com.teleconsultation.api.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅ Import ajouté

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "observations")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ✅ Annotation ajoutée
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dossier_id")
    @JsonIgnoreProperties({"observations", "imagesDICOM", "hibernateLazyInitializer", "handler"}) // ✅ Évite les cycles
    private DossierMedical dossierMedical;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    @JsonIgnoreProperties({"dossiersMedicaux", "rendezVous", "hibernateLazyInitializer", "handler"}) // ✅ Évite les cycles
    private Medecin medecin;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @CreationTimestamp
    private LocalDateTime dateCreation;

    // Constructeurs
    public Observation() {}

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public void setDossierMedical(DossierMedical dossierMedical) {
        this.dossierMedical = dossierMedical;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
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
        if (!(o instanceof Observation)) return false;
        Observation that = (Observation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "Observation{" +
                "id=" + id +
                ", dossierMedical=" + (dossierMedical != null ? dossierMedical.getId() : null) +
                ", medecin=" + (medecin != null ? medecin.getId() : null) +
                ", contenu='" + contenu + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}