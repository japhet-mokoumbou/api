package com.teleconsultation.api.service;

import com.teleconsultation.api.models.*;
import com.teleconsultation.api.payload.request.MedecinRequest;
import com.teleconsultation.api.payload.request.PatientRequest;
import com.teleconsultation.api.payload.request.SecretaireRequest;
import com.teleconsultation.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private SecretaireMedicalRepository secretaireRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private EmailService emailService;
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> findAllByCompteValide(boolean compteValide) {
        return userRepository.findAllByCompteValide(compteValide);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));
    }
    
    @Transactional
    public Patient registerPatient(PatientRequest patientRequest) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(patientRequest.getEmail())) {
            throw new RuntimeException("Erreur: Cet email est déjà utilisé!");
        }
        
        // Créer le nouvel utilisateur patient
        Patient patient = new Patient();
        patient.setNom(patientRequest.getNom());
        patient.setPrenom(patientRequest.getPrenom());
        patient.setEmail(patientRequest.getEmail());
        patient.setMotDePasse(encoder.encode(patientRequest.getMotDePasse()));
        patient.setTelephone(patientRequest.getTelephone());
        patient.setCompteValide(false);
        patient.setActif(true);
        
        // Informations spécifiques au patient
        patient.setNumeroSecuriteSociale(patientRequest.getNumeroSecuriteSociale());
        patient.setDateNaissance(patientRequest.getDateNaissance());
        patient.setAdresse(patientRequest.getAdresse());
        
        // Définir le rôle
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle non trouvé."));
        roles.add(userRole);
        patient.setRoles(roles);
        
        // Enregistrer le patient
        Patient savedPatient = patientRepository.save(patient);
        
        // Envoyer un email de confirmation à l'administrateur
        emailService.envoyerEmailNouvelleInscription(patient);
        
        return savedPatient;
    }
    
    @Transactional
    public Medecin registerMedecin(MedecinRequest medecinRequest) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(medecinRequest.getEmail())) {
            throw new RuntimeException("Erreur: Cet email est déjà utilisé!");
        }
        
        // Créer le nouvel utilisateur médecin
        Medecin medecin = new Medecin();
        medecin.setNom(medecinRequest.getNom());
        medecin.setPrenom(medecinRequest.getPrenom());
        medecin.setEmail(medecinRequest.getEmail());
        medecin.setMotDePasse(encoder.encode(medecinRequest.getMotDePasse()));
        medecin.setTelephone(medecinRequest.getTelephone());
        medecin.setCompteValide(false);
        medecin.setActif(true);
        
        // Informations spécifiques au médecin
        medecin.setSpecialite(medecinRequest.getSpecialite());
        medecin.setNumeroOrdre(medecinRequest.getNumeroOrdre());
        
        // Définir le rôle
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_MEDECIN)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle non trouvé."));
        roles.add(userRole);
        medecin.setRoles(roles);
        
        // Enregistrer le médecin
        Medecin savedMedecin = medecinRepository.save(medecin);
        
        // Envoyer un email de confirmation à l'administrateur
        emailService.envoyerEmailNouvelleInscription(medecin);
        
        return savedMedecin;
    }
    
    @Transactional
    public SecretaireMedical registerSecretaire(SecretaireRequest secretaireRequest) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(secretaireRequest.getEmail())) {
            throw new RuntimeException("Erreur: Cet email est déjà utilisé!");
        }
        
        // Créer le nouvel utilisateur secrétaire
        SecretaireMedical secretaire = new SecretaireMedical();
        secretaire.setNom(secretaireRequest.getNom());
        secretaire.setPrenom(secretaireRequest.getPrenom());
        secretaire.setEmail(secretaireRequest.getEmail());
        secretaire.setMotDePasse(encoder.encode(secretaireRequest.getMotDePasse()));
        secretaire.setTelephone(secretaireRequest.getTelephone());
        secretaire.setCompteValide(false);
        secretaire.setActif(true);
        
        // Informations spécifiques au secrétaire
        secretaire.setService(secretaireRequest.getService());
        
        // Définir le rôle
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_SECRETAIRE)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle non trouvé."));
        roles.add(userRole);
        secretaire.setRoles(roles);
        
        // Enregistrer le secrétaire
        SecretaireMedical savedSecretaire = secretaireRepository.save(secretaire);
        
        // Envoyer un email de confirmation à l'administrateur
        emailService.envoyerEmailNouvelleInscription(secretaire);
        
        return savedSecretaire;
    }
    
    @Transactional
    public void validerUtilisateur(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        
        user.setCompteValide(true);
        userRepository.save(user);
        
        // Envoyer un email à l'utilisateur pour l'informer que son compte a été validé
        emailService.envoyerEmailCompteValide(user);
    }
    
    @Transactional
    public void rejeterUtilisateur(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        
        // Supprimer l'utilisateur
        userRepository.delete(user);
        
        // Envoyer un email à l'utilisateur pour l'informer que son compte a été rejeté
        emailService.envoyerEmailCompteRejete(user);
    }
    
    @Transactional
    public void activerUtilisateur(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        
        user.setActif(true);
        userRepository.save(user);
    }
    
    @Transactional
    public void desactiverUtilisateur(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        
        user.setActif(false);
        userRepository.save(user);
    }
}