package com.seleniumautomation.secretmanager;

import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.google.protobuf.ByteString;
import java.io.IOException;

public class GmailSecretManager {
    private static final String SECRET_ID = "gmail-credentials";
    private static final String VERSION_ID = "latest";

    private static String getProjectId() {
        try {
            return System.getProperty("gcp.projectId", 
                    com.seleniumautomation.config.ConfigManager.getInstance().getProperty("gcp.projectId", "your-actual-project-id"));
        } catch (Exception e) {
            return System.getProperty("gcp.projectId", "your-actual-project-id");
        }
    }

    public static String getGmailCredentialsJson() {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(getProjectId(), SECRET_ID, VERSION_ID);
            AccessSecretVersionRequest request = AccessSecretVersionRequest.newBuilder()
                    .setName(secretVersionName.toString())
                    .build();
            ByteString payload = client.accessSecretVersion(request).getPayload().getData();
            return payload.toStringUtf8();
        } catch (IOException e) {
            throw new RuntimeException("Failed to access Gmail credentials from Google Secret Manager", e);
        }
    }
} 