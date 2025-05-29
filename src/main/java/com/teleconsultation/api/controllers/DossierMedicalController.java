package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.DossierMedical;
import com.teleconsultation.api.models.ImageDICOM;
import com.teleconsultation.api.models.Observation;
import com.teleconsultation.api.payload.request.ObservationRequest;
import com.teleconsultation.api.payload.response.MessageResponse;
import com.teleconsultation.api.service.DossierMedicalService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dossiers")
public class DossierMedicalController {

    @Autowired
    private DossierMedicalService dossierMedicalService;

    @GetMapping("/medecin/{medecinId}")
    @PreAuthorize("hasRole('MEDECIN') and #medecinId == authentication.principal.id")
    public ResponseEntity<List<DossierMedical>> getDossiersByMedecin(@PathVariable Long medecinId) {
        List<DossierMedical> dossiers = dossierMedicalService.findByMedecinId(medecinId);
        return ResponseEntity.ok(dossiers);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT') and #patientId == authentication.principal.id")
    public ResponseEntity<DossierMedical> getDossierByPatient(@PathVariable Long patientId) {
        Optional<DossierMedical> dossier = dossierMedicalService.findByPatientId(patientId);
        return dossier.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}/info")
    @PreAuthorize("hasRole('PATIENT') and #patientId == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> getDossierInfo(@PathVariable Long patientId) {
        Map<String, Object> info = dossierMedicalService.getDossierInfo(patientId);
        return ResponseEntity.ok(info);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDECIN', 'PATIENT')")
    public ResponseEntity<DossierMedical> getDossierById(@PathVariable Long id) {
        DossierMedical dossier = dossierMedicalService.findById(id);
        return ResponseEntity.ok(dossier);
    }

    @PostMapping("/{id}/observations")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<Observation> ajouterObservation(
            @PathVariable Long id,
            @Valid @RequestBody ObservationRequest observationRequest) {
        
        Observation observation = dossierMedicalService.ajouterObservation(
                id, 
                observationRequest.getContenu(), 
                observationRequest.getMedecinId()
        );
        
        return ResponseEntity.ok(observation);
    }

    @GetMapping("/{id}/observations")
    @PreAuthorize("hasAnyRole('MEDECIN', 'PATIENT')")
    public ResponseEntity<List<Observation>> getObservations(@PathVariable Long id) {
        List<Observation> observations = dossierMedicalService.getObservationsByDossier(id);
        return ResponseEntity.ok(observations);
    }

    @GetMapping("/{id}/images")
    @PreAuthorize("hasAnyRole('MEDECIN', 'PATIENT')")
    public ResponseEntity<List<ImageDICOM>> getImages(@PathVariable Long id) {
        List<ImageDICOM> images = dossierMedicalService.getImagesByDossier(id);
        return ResponseEntity.ok(images);
    }
}