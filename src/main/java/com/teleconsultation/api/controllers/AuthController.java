package com.teleconsultation.api.controllers;

import com.teleconsultation.api.payload.request.LoginRequest;
import com.teleconsultation.api.payload.request.MedecinRequest;
import com.teleconsultation.api.payload.request.PatientRequest;
import com.teleconsultation.api.payload.request.SecretaireRequest;
import com.teleconsultation.api.payload.response.JwtResponse;
import com.teleconsultation.api.payload.response.MessageResponse;
import com.teleconsultation.api.security.jwt.JwtUtils;
import com.teleconsultation.api.security.services.UserDetailsImpl;
import com.teleconsultation.api.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                                                userDetails.getId(),
                                                userDetails.getUsername(),
                                                userDetails.getNom(),
                                                userDetails.getPrenom(),
                                                roles));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody PatientRequest patientRequest) {
        try {
            userService.registerPatient(patientRequest);
            return ResponseEntity.ok(new MessageResponse("Patient enregistré avec succès! Veuillez attendre la validation par un administrateur."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/register/medecin")
    public ResponseEntity<?> registerMedecin(@Valid @RequestBody MedecinRequest medecinRequest) {
        try {
            userService.registerMedecin(medecinRequest);
            return ResponseEntity.ok(new MessageResponse("Médecin enregistré avec succès! Veuillez attendre la validation par un administrateur."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/register/secretaire")
    public ResponseEntity<?> registerSecretaire(@Valid @RequestBody SecretaireRequest secretaireRequest) {
        try {
            userService.registerSecretaire(secretaireRequest);
            return ResponseEntity.ok(new MessageResponse("Secrétaire enregistré avec succès! Veuillez attendre la validation par un administrateur."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}