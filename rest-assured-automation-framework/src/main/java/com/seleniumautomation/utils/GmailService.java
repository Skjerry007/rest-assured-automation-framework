package com.seleniumautomation.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GmailService {
    private static final String APPLICATION_NAME = "Automation Framework";
    private static final String CREDENTIALS_FILE_PATH = "src/test/resources/config/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static Gmail service;

    public static Gmail getGmailService() throws Exception {
        if (service == null) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(),
                new InputStreamReader(new FileInputStream(CREDENTIALS_FILE_PATH))
            );

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                SCOPES
            )
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
            .setAccessType("offline")
            .build();

            Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver.Builder().setPort(60310).build()
            ).authorize("user");

            service = new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            )
            .setApplicationName(APPLICATION_NAME)
            .build();
        }
        return service;
    }

    public static String getOTPFromEmail(String subject) throws Exception {
        Gmail gmail = getGmailService();
        ListMessagesResponse response = gmail.users().messages().list("me")
            .setQ("subject:" + subject)
            .setMaxResults(1L)
            .execute();

        if (response.getMessages() != null && !response.getMessages().isEmpty()) {
            Message message = gmail.users().messages().get("me", response.getMessages().get(0).getId())
                .setFormat("full")
                .execute();

            String emailContent = message.getSnippet();
            Pattern pattern = Pattern.compile("\\d{6}"); // Assuming 6-digit OTP
            Matcher matcher = pattern.matcher(emailContent);

            if (matcher.find()) {
                return matcher.group();
            }
        }
        throw new RuntimeException("OTP not found in email");
    }
}
