// ImageDICOMRepository.java
package com.teleconsultation.api.repository;

import com.teleconsultation.api.models.DossierMedical;
import com.teleconsultation.api.models.ImageDICOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageDICOMRepository extends JpaRepository<ImageDICOM, Long> {
    List<ImageDICOM> findByDossierMedicalOrderByDateUploadDesc(DossierMedical dossierMedical);
}