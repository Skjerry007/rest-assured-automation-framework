package com.restautomation.secretmanager;

import com.restautomation.utils.LoggerUtil;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * SecretManager - AWS Secret Manager Integration
 * Singleton class for retrieving secrets from AWS Secrets Manager
 */
public class SecretManager {
    private static SecretManager instance;
    private final SecretsManagerClient secretsManagerClient;
    private final ObjectMapper objectMapper;

    private SecretManager() {
        // Initialize the AWS Secrets Manager client
        this.secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.US_EAST_1) // Default region, can be configured
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get singleton instance
     * @return SecretManager instance
     */
    public static synchronized SecretManager getInstance() {
        if (instance == null) {
            instance = new SecretManager();
        }
        return instance;
    }

    /**
     * Get secret value by name
     * @param secretName name of the secret
     * @return secret value
     */
    public String getSecret(String secretName) {
        try {
            LoggerUtil.info("Retrieving secret: {}", secretName);
            
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();
            
            GetSecretValueResponse response = secretsManagerClient.getSecretValue(request);
            return response.secretString();
        } catch (SecretsManagerException e) {
            LoggerUtil.error("Error retrieving secret: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve secret from AWS Secrets Manager", e);
        }
    }

    /**
     * Get specific key from a JSON secret
     * @param secretName name of the secret
     * @param key key to retrieve from JSON
     * @return value for the specified key
     */
    public String getSecretKey(String secretName, String key) {
        try {
            String secretJson = getSecret(secretName);
            JsonNode jsonNode = objectMapper.readTree(secretJson);
            if (jsonNode.has(key)) {
                return jsonNode.get(key).asText();
            } else {
                LoggerUtil.error("Key {} not found in secret {}", key, secretName);
                throw new RuntimeException("Key not found in secret");
            }
        } catch (IOException e) {
            LoggerUtil.error("Error parsing secret JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to parse secret JSON", e);
        }
    }

    /**
     * Close the AWS Secrets Manager client
     */
    public void close() {
        if (secretsManagerClient != null) {
            secretsManagerClient.close();
        }
    }
}