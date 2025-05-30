package com.teleconsultation.api.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅ Import ajouté

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "images_dicom")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ✅ Annotation ajoutée
public class ImageDICOM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dossier_id")
    @JsonIgnoreProperties({"observations", "imagesDICOM", "hibernateLazyInitializer", "handler"}) // ✅ Évite les cycles
    private DossierMedical dossierMedical;

    private String orthancId; // Identifiant de l'image sur le serveur Orthanc

    private String description;

    @CreationTimestamp
    private LocalDateTime dateUpload;

    // Constructeur par défaut
    public ImageDICOM() {}

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

    public String getOrthancId() {
        return orthancId;
    }

    public void setOrthancId(String orthancId) {
        this.orthancId = orthancId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }

    // equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageDICOM)) return false;
        ImageDICOM that = (ImageDICOM) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "ImageDICOM{" +
                "id=" + id +
                ", dossierMedical=" + (dossierMedical != null ? dossierMedical.getId() : null) +
                ", orthancId='" + orthancId + '\'' +
                ", description='" + description + '\'' +
                ", dateUpload=" + dateUpload +
                '}';
    }
}