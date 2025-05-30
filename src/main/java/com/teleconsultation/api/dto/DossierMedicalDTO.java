package com.teleconsultation.api.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DossierMedicalDTO {
    private Long id;
    private PatientSimpleDTO patient;
    private MedecinSimpleDTO medecin;
    private List<ObservationDTO> observations;
    private List<ImageDICOMDTO> imagesDICOM;
    private LocalDateTime dateCreation;
}