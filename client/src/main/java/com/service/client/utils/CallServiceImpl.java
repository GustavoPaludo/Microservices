package com.service.client.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.service.client.ApplicationConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.net.URI;
import java.sql.Date;
import java.security.Key;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Service
public class CallServiceImpl implements CallService {

    private static final Logger logger = LogManager.getLogger(CallServiceImpl.class);
    private final RestClient restClient;
    private final Key signingKey;

    @Autowired
    public CallServiceImpl(RestClient restClient) {
        this.restClient = restClient;
        this.signingKey = generateKey(ApplicationConstants.JWT_KEY);
    }

    @Override
    public <T> T getForObject(URI serviceUri, String endpoint, Class<T> responseType) {
        ResponseSpec response = null;

        try {
            response = restClient.get()
                .uri(serviceUri.toString() + endpoint)
                .header("Authorization", "Bearer " + this.generateJwt())
                .retrieve();
        } catch (Exception e) {
            logger.error(CallServiceImpl.class.getName() + " getForObject : Erro ao encontrar o serviço: " + e.getMessage(), e);
            return null;
        }

        return response != null ? response.body(responseType) : null;
    }

    @Override
    public <T> T getForObject(URI serviceUri, String endpoint, ParameterizedTypeReference<T> responseType) {
        ResponseSpec response = null;

        try {
            response = restClient.get()
                .uri(serviceUri.toString() + endpoint)
                .header("Authorization", "Bearer " + this.generateJwt())
                .retrieve();
        } catch (Exception e) {
            logger.error(CallServiceImpl.class.getName() + " getForObject : Erro ao encontrar o serviço: " + e.getMessage(), e);
            return null;
        }

        return response != null ? response.body(responseType) : null;
    }

    private String generateJwt() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(signingKey)
                .compact();
        } else {
            logger.warn("Principal is not an instance of UserDetails");
            return null;
        }
    }

    private Key generateKey(String baseValue) {
        try {
            byte[] salt = "fixed-salt".getBytes();
            PBEKeySpec spec = new PBEKeySpec(baseValue.toCharArray(), salt, 65536, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] key = factory.generateSecret(spec).getEncoded();
            return Keys.hmacShaKeyFor(key);
        } catch (Exception e) {
            logger.error("Error generating JWT key: " + e.getMessage(), e);
            return null;
        }
    }
}
