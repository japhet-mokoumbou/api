package com.teleconsultation.api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ImageDICOMDTO {
    private Long id;
    private String orthancId;
    private String description;
    private LocalDateTime dateUpload;
}
