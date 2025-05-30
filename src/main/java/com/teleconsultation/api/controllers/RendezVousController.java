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
@CrossOrigin(origins = "*", maxAge = 3600)
public class RendezVousController {
    
    @Autowired
    private RendezVousService rendezVousService;
    
    @GetMapping
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        try {
            List<RendezVous> rendezVous = rendezVousService.findAll();
            
            // Logs détaillés pour debug
            System.out.println("=== DEBUG BACKEND ===");
            System.out.println("Nombre de rendez-vous trouvés: " + rendezVous.size());
            
            if (!rendezVous.isEmpty()) {
                RendezVous premier = rendezVous.get(0);
                System.out.println("Premier rendez-vous ID: " + premier.getId());
                System.out.println("Premier rendez-vous - Patient: " + 
                    (premier.getPatient() != null ? 
                        premier.getPatient().getNom() + " " + premier.getPatient().getPrenom() : 
                        "null"));
                System.out.println("Premier rendez-vous - Médecin: " + 
                    (premier.getMedecin() != null ? 
                        premier.getMedecin().getNom() + " " + premier.getMedecin().getPrenom() : 
                        "null"));
                System.out.println("Premier rendez-vous - Date: " + premier.getDateHeure());
                System.out.println("Premier rendez-vous - Durée: " + premier.getDuree());
                System.out.println("Premier rendez-vous - Motif: " + premier.getMotif());
                System.out.println("Premier rendez-vous - Statut: " + premier.getStatut());
                
                // Vérifier la sérialisation des relations
                if (premier.getPatient() != null) {
                    System.out.println("Patient détails - ID: " + premier.getPatient().getId());
                    System.out.println("Patient détails - Email: " + premier.getPatient().getEmail());
                }
                
                if (premier.getMedecin() != null) {
                    System.out.println("Médecin détails - ID: " + premier.getMedecin().getId());
                    System.out.println("Médecin détails - Spécialité: " + premier.getMedecin().getSpecialite());
                }
            }
            System.out.println("=== FIN DEBUG BACKEND ===");
            
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des rendez-vous:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'MEDECIN', 'PATIENT')")
    public ResponseEntity<RendezVous> getRendezVousById(@PathVariable Long id) {
        try {
            RendezVous rendezVous = rendezVousService.findById(id);
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du rendez-vous ID " + id + ":");
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<?> createRendezVous(@Valid @RequestBody RendezVousRequest rendezVousRequest) {
        try {
            System.out.println("Création d'un nouveau rendez-vous:");
            System.out.println("Patient ID: " + rendezVousRequest.getPatientId());
            System.out.println("Médecin ID: " + rendezVousRequest.getMedecinId());
            System.out.println("Date/Heure: " + rendezVousRequest.getDateHeure());
            System.out.println("Durée: " + rendezVousRequest.getDuree());
            System.out.println("Motif: " + rendezVousRequest.getMotif());
            
            RendezVous rendezVous = rendezVousService.save(rendezVousRequest);
            
            System.out.println("Rendez-vous créé avec succès - ID: " + rendezVous.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(rendezVous);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du rendez-vous:");
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la création du rendez-vous: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<?> updateRendezVous(@PathVariable Long id, @Valid @RequestBody RendezVousRequest rendezVousRequest) {
        try {
            System.out.println("Modification du rendez-vous ID: " + id);
            System.out.println("Nouvelles données:");
            System.out.println("Patient ID: " + rendezVousRequest.getPatientId());
            System.out.println("Médecin ID: " + rendezVousRequest.getMedecinId());
            System.out.println("Date/Heure: " + rendezVousRequest.getDateHeure());
            System.out.println("Durée: " + rendezVousRequest.getDuree());
            System.out.println("Motif: " + rendezVousRequest.getMotif());
            
            RendezVous rendezVous = rendezVousService.update(id, rendezVousRequest);
            
            System.out.println("Rendez-vous modifié avec succès");
            
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification du rendez-vous ID " + id + ":");
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la modification du rendez-vous: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<?> deleteRendezVous(@PathVariable Long id) {
        try {
            System.out.println("Suppression du rendez-vous ID: " + id);
            
            rendezVousService.delete(id);
            
            System.out.println("Rendez-vous supprimé avec succès");
            
            return ResponseEntity.ok(new MessageResponse("Rendez-vous supprimé avec succès"));
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du rendez-vous ID " + id + ":");
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la suppression: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/valider")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<?> validerRendezVous(@PathVariable Long id) {
        try {
            System.out.println("Validation du rendez-vous ID: " + id);
            
            RendezVous rendezVous = rendezVousService.valider(id);
            
            System.out.println("Rendez-vous validé avec succès");
            
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            System.err.println("Erreur lors de la validation du rendez-vous ID " + id + ":");
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors de la validation: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/rejeter")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<?> rejeterRendezVous(@PathVariable Long id, @RequestBody(required = false) RejetRequest rejetRequest) {
        try {
            System.out.println("Rejet du rendez-vous ID: " + id);
            
            String motif = rejetRequest != null ? rejetRequest.getMotif() : null;
            if (motif != null) {
                System.out.println("Motif du rejet: " + motif);
            }
            
            RendezVous rendezVous = rendezVousService.rejeter(id, motif);
            
            System.out.println("Rendez-vous rejeté avec succès");
            
            return ResponseEntity.ok(rendezVous);
        } catch (Exception e) {
            System.err.println("Erreur lors du rejet du rendez-vous ID " + id + ":");
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Erreur lors du rejet: " + e.getMessage()));
        }
    }
    
    // Endpoint supplémentaire pour debug - à retirer en production
    @GetMapping("/debug")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<?> debugRendezVous() {
        try {
            List<RendezVous> rendezVous = rendezVousService.findAll();
            
            System.out.println("=== DEBUG ENDPOINT ===");
            for (RendezVous rdv : rendezVous) {
                System.out.println("RDV ID: " + rdv.getId());
                System.out.println("  Patient: " + (rdv.getPatient() != null ? rdv.getPatient().toString() : "null"));
                System.out.println("  Médecin: " + (rdv.getMedecin() != null ? rdv.getMedecin().toString() : "null"));
                System.out.println("  Date: " + rdv.getDateHeure());
                System.out.println("  Statut: " + rdv.getStatut());
                System.out.println("  ---");
            }
            System.out.println("=== FIN DEBUG ENDPOINT ===");
            
            return ResponseEntity.ok("Debug terminé, consultez les logs");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur debug: " + e.getMessage());
        }
    }
}