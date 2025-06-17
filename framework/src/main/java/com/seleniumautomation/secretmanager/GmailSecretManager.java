package com.seleniumautomation.secretmanager;

import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.google.protobuf.ByteString;
import java.io.IOException;

public class GmailSecretManager {
    private static final String PROJECT_ID = "898893564285";
    private static final String SECRET_ID = "gmail-credentials";
    private static final String VERSION_ID = "latest";

    public static String getGmailCredentialsJson() {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(PROJECT_ID, SECRET_ID, VERSION_ID);
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