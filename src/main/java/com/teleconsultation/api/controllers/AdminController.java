package com.teleconsultation.api.controllers;

import com.teleconsultation.api.models.User;
import com.teleconsultation.api.payload.response.MessageResponse;
import com.teleconsultation.api.service.AdminService;
import com.teleconsultation.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/utilisateurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUtilisateurs() {
        List<User> utilisateurs = userService.findAllUsers();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/utilisateurs/en-attente")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUtilisateursEnAttente() {
        List<User> utilisateurs = userService.findAllByCompteValide(false);
        return ResponseEntity.ok(utilisateurs);
    }

    @PutMapping("/utilisateurs/{id}/valider")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> validerUtilisateur(@PathVariable Long id) {
        try {
            userService.validerUtilisateur(id);
            return ResponseEntity.ok(new MessageResponse("Utilisateur validé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/utilisateurs/{id}/rejeter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejeterUtilisateur(@PathVariable Long id) {
        try {
            userService.rejeterUtilisateur(id);
            return ResponseEntity.ok(new MessageResponse("Utilisateur rejeté avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/utilisateurs/{id}/activer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activerUtilisateur(@PathVariable Long id) {
        try {
            userService.activerUtilisateur(id);
            return ResponseEntity.ok(new MessageResponse("Utilisateur activé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/utilisateurs/{id}/desactiver")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desactiverUtilisateur(@PathVariable Long id) {
        try {
            userService.desactiverUtilisateur(id);
            return ResponseEntity.ok(new MessageResponse("Utilisateur désactivé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
}