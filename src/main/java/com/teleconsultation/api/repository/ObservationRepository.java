package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.DossierMedical;
import com.teleconsultation.api.models.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByDossierMedicalOrderByDateCreationDesc(DossierMedical dossierMedical);
}