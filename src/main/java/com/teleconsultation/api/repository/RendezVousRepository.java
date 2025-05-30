package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.Patient;
import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.models.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    
    // Requêtes existantes conservées
    List<RendezVous> findAllByOrderByDateHeureDesc();
    
    List<RendezVous> findByMedecinOrderByDateHeureDesc(Medecin medecin);
    
    List<RendezVous> findByPatientOrderByDateHeureDesc(Patient patient);
    
    List<RendezVous> findByDateHeureBetweenOrderByDateHeureAsc(LocalDateTime startDate, LocalDateTime endDate);
    
    // Requêtes optimisées avec JOIN FETCH - VERSION CORRIGÉE POUR POSTGRESQL
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "ORDER BY r.dateHeure DESC")
    List<RendezVous> findAllWithPatientAndMedecinOrderByDateHeureDesc();
    
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "WHERE r.medecin.id = :medecinId " +
           "ORDER BY r.dateHeure DESC")
    List<RendezVous> findByMedecinIdWithPatientAndMedecinOrderByDateHeureDesc(@Param("medecinId") Long medecinId);
    
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "WHERE r.patient.id = :patientId " +
           "ORDER BY r.dateHeure DESC")
    List<RendezVous> findByPatientIdWithPatientAndMedecinOrderByDateHeureDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "WHERE r.dateHeure BETWEEN :startDate AND :endDate " +
           "ORDER BY r.dateHeure ASC")
    List<RendezVous> findByDateHeureBetweenWithPatientAndMedecinOrderByDateHeureAsc(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);
    
    // Requêtes par statut
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "WHERE r.statut = :statut " +
           "ORDER BY r.dateHeure DESC")
    List<RendezVous> findByStatutWithPatientAndMedecinOrderByDateHeureDesc(@Param("statut") StatutRendezVous statut);
    
    // Recherche par ID avec JOIN FETCH
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "WHERE r.id = :id")
    Optional<RendezVous> findByIdWithPatientAndMedecin(@Param("id") Long id);
    
    // Requêtes statistiques simples
    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.statut = :statut")
    Long countByStatut(@Param("statut") StatutRendezVous statut);
    
    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.statut = :statut")
    Long countByMedecinIdAndStatut(@Param("medecinId") Long medecinId, @Param("statut") StatutRendezVous statut);
    
    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.patient.id = :patientId AND r.statut = :statut")
    Long countByPatientIdAndStatut(@Param("patientId") Long patientId, @Param("statut") StatutRendezVous statut);
    
    // Requêtes pour les rendez-vous à venir - SIMPLIFIÉES
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "WHERE r.dateHeure > :currentTime " +
           "ORDER BY r.dateHeure ASC")
    List<RendezVous> findUpcomingRendezVousWithPatientAndMedecin(@Param("currentTime") LocalDateTime currentTime);
    
    // Requêtes pour les rendez-vous passés - SIMPLIFIÉES
    @Query("SELECT r FROM RendezVous r " +
           "JOIN FETCH r.patient p " +
           "JOIN FETCH r.medecin m " +
           "WHERE r.dateHeure < :currentTime " +
           "ORDER BY r.dateHeure DESC")
    List<RendezVous> findPastRendezVousWithPatientAndMedecin(@Param("currentTime") LocalDateTime currentTime);
}