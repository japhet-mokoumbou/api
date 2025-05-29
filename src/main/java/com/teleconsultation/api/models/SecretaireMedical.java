package com.teleconsultation.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "secretaires")
public class SecretaireMedical extends User {

    // Attributs spécifiques aux secrétaires médicaux
    private String service;

    // Getter et Setter
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    // equals et hashCode (basés sur id et service)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecretaireMedical)) return false;
        if (!super.equals(o)) return false;
        SecretaireMedical that = (SecretaireMedical) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), service);
    }

    // toString
    @Override
    public String toString() {
        return "SecretaireMedical{" +
                "id=" + getId() +
                ", nom=" + getNom() +
                ", prenom=" + getPrenom() +
                ", email=" + getEmail() +
                ", service='" + service + '\'' +
                '}';
    }
}
