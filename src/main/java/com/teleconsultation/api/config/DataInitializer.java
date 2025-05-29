package com.teleconsultation.api.config;

import com.teleconsultation.api.models.*;
import com.teleconsultation.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  MedecinRepository medecinRepository;

    @Autowired
    private  PatientRepository patientRepository;
    
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            System.out.println("Initialisation des données...");
            
            // Initialisation des rôles
            initRoles();
            
            // Initialisation de l'administrateur
            initAdmin();
            
            System.out.println("Initialisation terminée");

            initTestData();
        };
    }
    
    private void initRoles() {
        System.out.println("Initialisation des rôles...");
        
        createRoleIfNotExists(ERole.ROLE_ADMIN);
        createRoleIfNotExists(ERole.ROLE_MEDECIN);
        createRoleIfNotExists(ERole.ROLE_PATIENT);
        createRoleIfNotExists(ERole.ROLE_SECRETAIRE);
        
        System.out.println("Initialisation des rôles terminée");
    }
    
    private void createRoleIfNotExists(ERole roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            System.out.println("Rôle " + roleName + " créé");
        } else {
            System.out.println("Rôle " + roleName + " déjà existant");
        }
    }
    
    private void initAdmin() {
        System.out.println("Initialisation de l'administrateur...");
        
        // Vérifier si un administrateur existe déjà
        if (adminRepository.count() == 0) {
            // Créer un nouvel administrateur
            Admin admin = new Admin();
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setEmail("admin@teleconsultation.com");
            admin.setMotDePasse(passwordEncoder.encode("admin123")); // Mot de passe à changer en production
            admin.setCompteValide(true);
            admin.setActif(true);
            
            // Attribuer le rôle ADMIN
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Erreur: Rôle Admin non trouvé."));
            roles.add(adminRole);
            admin.setRoles(roles);
            
            // Sauvegarder l'administrateur
            adminRepository.save(admin);
            
            System.out.println("Administrateur créé avec succès. Email: admin@teleconsultation.com, Mot de passe: admin123");
        } else {
            System.out.println("Un administrateur existe déjà dans la base de données");
        }
    }

    // Ajouter ces méthodes dans votre DataInitializer.java

private void initTestData() {
    // Créer quelques médecins de test
    if (medecinRepository.count() < 3) {
        createTestMedecin("Docteur", "Martin", "docteur.martin@teleconsultation.com", "Généraliste", "12345");
        createTestMedecin("Docteur", "Durand", "docteur.durand@teleconsultation.com", "Cardiologue", "67890");
        createTestMedecin("Docteur", "Moreau", "docteur.moreau@teleconsultation.com", "Dermatologue", "11111");
    }
    
    // Créer quelques patients de test
    if (patientRepository.count() < 3) {
        createTestPatient("Dupont", "Jean", "jean.dupont@example.com", "1234567890123", LocalDate.of(1980, 1, 1));
        createTestPatient("Martin", "Marie", "marie.martin@example.com", "9876543210987", LocalDate.of(1990, 5, 15));
        createTestPatient("Bernard", "Paul", "paul.bernard@example.com", "5555555555555", LocalDate.of(1975, 12, 10));
    }
}

private void createTestMedecin(String nom, String prenom, String email, String specialite, String numeroOrdre) {
    if (!userRepository.existsByEmail(email)) {
        Medecin medecin = new Medecin();
        medecin.setNom(nom);
        medecin.setPrenom(prenom);
        medecin.setEmail(email);
        medecin.setMotDePasse(passwordEncoder.encode("medecin123"));
        medecin.setSpecialite(specialite);
        medecin.setNumeroOrdre(numeroOrdre);
        medecin.setCompteValide(true);
        medecin.setActif(true);
        
        Set<Role> roles = new HashSet<>();
        Role medecinRole = roleRepository.findByName(ERole.ROLE_MEDECIN)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle Médecin non trouvé."));
        roles.add(medecinRole);
        medecin.setRoles(roles);
        
        medecinRepository.save(medecin);
        System.out.println("Médecin de test créé: " + nom + " " + prenom);
    }
}

private void createTestPatient(String nom, String prenom, String email, String numeroSecu, LocalDate dateNaissance) {
    if (!userRepository.existsByEmail(email)) {
        Patient patient = new Patient();
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setEmail(email);
        patient.setMotDePasse(passwordEncoder.encode("patient123"));
        patient.setNumeroSecuriteSociale(numeroSecu);
        patient.setDateNaissance(dateNaissance);
        patient.setAdresse("Adresse de test");
        patient.setCompteValide(true);
        patient.setActif(true);
        
        Set<Role> roles = new HashSet<>();
        Role patientRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle Patient non trouvé."));
        roles.add(patientRole);
        patient.setRoles(roles);
        
        patientRepository.save(patient);
        System.out.println("Patient de test créé: " + nom + " " + prenom);
    }
}

// Appeler initTestData() à la fin de votre méthode initData()
}