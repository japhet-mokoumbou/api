package com.teleconsultation.api.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rendez_vous")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"rendezVous", "hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medecin_id", nullable = false)
    @JsonIgnoreProperties({"rendezVous", "hibernateLazyInitializer", "handler"})
    private Medecin medecin;

    @Column(name = "date_heure", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Paris")
    private LocalDateTime dateHeure;

    @Column(name = "duree", nullable = false)
    private int duree; // en minutes

    @Column(name = "motif", nullable = false, length = 1000)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutRendezVous statut = StatutRendezVous.PLANIFIE;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Paris")
    private LocalDateTime dateCreation;

    // Champ optionnel pour le motif de rejet
    @Column(name = "motif_rejet", length = 1000)
    private String motifRejet;

    // Constructeurs
    public RendezVous() {}

    public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure, int duree, String motif) {
        this.patient = patient;
        this.medecin = medecin;
        this.dateHeure = dateHeure;
        this.duree = duree;
        this.motif = motif;
        this.statut = StatutRendezVous.PLANIFIE;
    }

    // Getters et setters
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

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public StatutRendezVous getStatut() {
        return statut;
    }

    public void setStatut(StatutRendezVous statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    // Méthodes utilitaires
    public boolean isPasse() {
        return this.dateHeure != null && this.dateHeure.isBefore(LocalDateTime.now());
    }

    public boolean isAujourdhui() {
        if (this.dateHeure == null) return false;
        LocalDateTime now = LocalDateTime.now();
        return this.dateHeure.toLocalDate().equals(now.toLocalDate());
    }

    public boolean isValide() {
        return StatutRendezVous.VALIDE.equals(this.statut);
    }

    public boolean isPlanifie() {
        return StatutRendezVous.PLANIFIE.equals(this.statut);
    }

    public boolean isRejete() {
        return StatutRendezVous.REJETE.equals(this.statut);
    }

    // equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RendezVous)) return false;
        RendezVous that = (RendezVous) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString pour debug
    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getId() + "(" + patient.getNom() + ")" : "null") +
                ", medecin=" + (medecin != null ? medecin.getId() + "(" + medecin.getNom() + ")" : "null") +
                ", dateHeure=" + dateHeure +
                ", duree=" + duree +
                ", motif='" + motif + '\'' +
                ", statut=" + statut +
                ", dateCreation=" + dateCreation +
                ", motifRejet='" + motifRejet + '\'' +
                '}';
    }
}