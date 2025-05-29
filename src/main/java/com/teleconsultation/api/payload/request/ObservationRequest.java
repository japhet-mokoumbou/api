package com.teleconsultation.api.payload.request;

import lombok.Data;


@Data
public class ObservationRequest {
    @jakarta.validation.constraints.NotBlank
    private String contenu;
    
    @jakarta.validation.constraints.NotNull
    private Long medecinId;
}