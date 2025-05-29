package com.teleconsultation.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "admins")
public class Admin extends User {

    // Constructeur
    public Admin() {
        super();
    }

    // equals et hashCode (basés sur id)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    // toString
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", nom=" + getNom() +
                ", prenom=" + getPrenom() +
                ", email=" + getEmail() +
                '}';
    }
}
