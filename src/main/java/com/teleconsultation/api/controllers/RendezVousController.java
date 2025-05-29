package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.RendezVous;
import com.teleconsultation.api.payload.request.RendezVousRequest;
import com.teleconsultation.api.payload.request.RejetRequest;
import com.teleconsultation.api.payload.response.MessageResponse;
import com.teleconsultation.api.service.RendezVousService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendez-vous")
public class RendezVousController {
    
    @Autowired
    private RendezVousService rendezVousService;
    
    @GetMapping
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        try {
            List<RendezVous> rendezVous = rendezVousService.findAll();
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'MEDECIN', 'PATIENT')")
    public ResponseEntity<RendezVous> getRendezVousById(@PathVariable Long id) {
        try {
            RendezVous rendezVous = rendezVousService.findById(id);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<?> createRendezVous(@jakarta.validation.Valid @RequestBody RendezVousRequest rendezVousRequest) {
        try {
            RendezVous rendezVous = rendezVousService.save(rendezVousRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la création du rendez-vous: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<?> updateRendezVous(@PathVariable Long id, @Valid @RequestBody RendezVousRequest rendezVousRequest) {
        try {
            RendezVous rendezVous = rendezVousService.update(id, rendezVousRequest);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la modification du rendez-vous: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<?> deleteRendezVous(@PathVariable Long id) {
        try {
            rendezVousService.delete(id);
            return ResponseEntity.ok(new MessageResponse("Rendez-vous supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la suppression: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/valider")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<?> validerRendezVous(@PathVariable Long id) {
        try {
            RendezVous rendezVous = rendezVousService.valider(id);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la validation: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/rejeter")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<?> rejeterRendezVous(@PathVariable Long id, @RequestBody(required = false) RejetRequest rejetRequest) {
        try {
            String motif = rejetRequest != null ? rejetRequest.getMotif() : null;
            RendezVous rendezVous = rendezVousService.rejeter(id, motif);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors du rejet: " + e.getMessage()));
        }
    }
}