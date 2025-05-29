// OrthancService.java - Service pour l'intégration avec Orthanc

package com.teleconsultation.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class OrthancService {
    
    @Value("${orthanc.server.url}")
    private String orthancUrl;
    
    @Value("${orthanc.server.username}")
    private String orthancUsername;
    
    @Value("${orthanc.server.password}")
    private String orthancPassword;
    
    private final RestTemplate restTemplate;
    
    public OrthancService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Récupère une image DICOM depuis Orthanc
     * @param orthancId L'identifiant de l'image sur Orthanc
     * @return L'image au format binaire
     */
    public byte[] getImage(String orthancId) {
        String url = orthancUrl + "/instances/" + orthancId + "/file";
        
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<byte[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            byte[].class
        );
        
        return response.getBody();
    }
    
    /**
     * Récupère les métadonnées d'une image DICOM
     * @param orthancId L'identifiant de l'image sur Orthanc
     * @return Les métadonnées de l'image
     */
    public Map<String, Object> getImageMetadata(String orthancId) {
        String url = orthancUrl + "/instances/" + orthancId + "/tags";
        
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Map.class
        );
        
        return response.getBody();
    }
    
    /**
     * Téléverse une image DICOM sur Orthanc
     * @param dicomFile Le fichier DICOM
     * @return L'identifiant de l'image sur Orthanc
     */
    public String uploadDicomFile(MultipartFile dicomFile) throws IOException {
        String url = orthancUrl + "/instances";
        
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        HttpEntity<byte[]> entity = new HttpEntity<>(dicomFile.getBytes(), headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            Map.class
        );
        
        Map<String, Object> responseBody = response.getBody();
        
        if (responseBody != null && responseBody.containsKey("ID")) {
            return (String) responseBody.get("ID");
        }
        
        throw new RuntimeException("Erreur lors du téléversement du fichier DICOM");
    }
    
    /**
     * Récupère un aperçu de l'image DICOM
     * @param orthancId L'identifiant de l'image sur Orthanc
     * @return L'image de prévisualisation en PNG
     */
    public byte[] getImagePreview(String orthancId) {
        String url = orthancUrl + "/instances/" + orthancId + "/preview";
        
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<byte[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            byte[].class
        );
        
        return response.getBody();
    }
    
    /**
     * Vérifie si Orthanc est accessible
     * @return true si Orthanc est accessible
     */
    public boolean isOrthancAvailable() {
        try {
            String url = orthancUrl + "/system";
            
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
            );
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Crée les en-têtes d'authentification pour Orthanc
     * @return HttpHeaders avec l'authentification Basic
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = orthancUsername + ":" + orthancPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        return headers;
    }
}