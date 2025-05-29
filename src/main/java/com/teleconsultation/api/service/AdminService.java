package com.teleconsultation.api.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teleconsultation.api.models.ERole;
import com.teleconsultation.api.repository.MedecinRepository;
import com.teleconsultation.api.repository.PatientRepository;
import com.teleconsultation.api.repository.SecretaireMedicalRepository;
import com.teleconsultation.api.repository.UserRepository;

// AdminService.java (Backend)
@Service
public class AdminService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private SecretaireMedicalRepository secretaireRepository;
    
    //@Autowired
   // private rendezVousRepository rendezVousRepository;
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userRepository.count());
        stats.put("totalPatients", patientRepository.count());
        stats.put("totalMedecins", medecinRepository.count());
        stats.put("totalSecretaires", secretaireRepository.count());
        stats.put("totalAdmins", userRepository.countByRoles_Name(ERole.ROLE_ADMIN));
        
        // Rendez-vous de la semaine en cours
        LocalDateTime startOfWeek = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
       // stats.put("rendezVousSemaine", rendezVousRepository.countByDateHeureBetween(startOfWeek, endOfWeek));
        
        // Utilisateurs récemment inscrits (5 derniers jours)
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
      //  stats.put("nouveauxUtilisateurs", userRepository.countByDateCreationAfter(fiveDaysAgo));
        
        return stats;
    }
}
