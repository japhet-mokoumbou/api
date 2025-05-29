// DicomController.java - Contrôleur pour la gestion des images DICOM

package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.ImageDICOM;
import com.teleconsultation.api.payload.response.MessageResponse;
import com.teleconsultation.api.service.DossierMedicalService;
import com.teleconsultation.api.service.OrthancService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/dicom")
public class DicomController {
    
    @Autowired
    private OrthancService orthancService;
    
    @Autowired
    private DossierMedicalService dossierMedicalService;
    
    /**
     * Récupère une image DICOM depuis Orthanc
     */
    @GetMapping("/{orthancId}")
    @PreAuthorize("hasAnyRole('MEDECIN', 'PATIENT')")
    public ResponseEntity<byte[]> getImage(@PathVariable String orthancId) {
        try {
            byte[] imageData = orthancService.getImage(orthancId);
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(imageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupère un aperçu de l'image DICOM
     */
    @GetMapping("/{orthancId}/preview")
    @PreAuthorize("hasAnyRole('MEDECIN', 'PATIENT')")
    public ResponseEntity<byte[]> getImagePreview(@PathVariable String orthancId) {
        try {
            byte[] previewData = orthancService.getImagePreview(orthancId);
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(previewData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupère les métadonnées d'une image DICOM
     */
    @GetMapping("/{orthancId}/metadata")
    @PreAuthorize("hasAnyRole('MEDECIN', 'PATIENT')")
    public ResponseEntity<Map<String, Object>> getImageMetadata(@PathVariable String orthancId) {
        try {
            Map<String, Object> metadata = orthancService.getImageMetadata(orthancId);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Téléverse une image DICOM
     */
    @PostMapping("/upload/{dossierId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> uploadDicomImage(
            @PathVariable Long dossierId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {
        
        try {
            // Vérifications de base
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Aucun fichier sélectionné"));
            }
            
            // Vérifier la taille du fichier (max 50MB)
            long maxSize = 50 * 1024 * 1024; // 50MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Le fichier est trop volumineux (maximum 50MB)"));
            }
            
            // Vérifier le type de fichier
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".dcm")) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Seuls les fichiers DICOM (.dcm) sont acceptés"));
            }
            
            // Vérifier que le serveur Orthanc est disponible
            if (!orthancService.isOrthancAvailable()) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new MessageResponse("Le serveur d'images médicales n'est pas disponible"));
            }
            
            // Téléverser le fichier sur Orthanc
            String orthancId = orthancService.uploadDicomFile(file);
            
            // Enregistrer l'image dans la base de données
            ImageDICOM imageDICOM = dossierMedicalService.ajouterImageDICOM(
                dossierId, 
                orthancId, 
                description
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(imageDICOM);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Erreur lors du téléversement de l'image: " + e.getMessage()));
        }
    }
    
    /**
     * Vérifie le statut du serveur Orthanc
     */
    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('MEDECIN', 'PATIENT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getOrthancStatus() {
        boolean available = orthancService.isOrthancAvailable();
        
        Map<String, Object> status = Map.of(
            "available", available,
            "message", available ? "Serveur Orthanc opérationnel" : "Serveur Orthanc non disponible"
        );
        
        return ResponseEntity.ok(status);
    }
}