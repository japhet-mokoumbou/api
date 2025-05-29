package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.DossierMedical;
import com.teleconsultation.api.models.Medecin;
import com.teleconsultation.api.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {
    
    List<DossierMedical> findByMedecinOrderByDateCreationDesc(Medecin medecin);
    
    Optional<DossierMedical> findByPatient(Patient patient);
    
    @Query("SELECT COUNT(DISTINCT d.patient) FROM DossierMedical d WHERE d.medecin = ?1")
    long countDistinctPatientsByMedecin(Medecin medecin);
}