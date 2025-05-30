package com.teleconsultation.api.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateDossierRequest {
    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;
}