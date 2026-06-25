package common.utils;

/**
 * GmailService - Stub utility for retrieve OTP from email (temporarily created to resolve compilation).
 */
public class GmailService {
    public static String getOTPFromEmail(String subject) {
        LoggerUtil.info("Stubbed GmailService: returning dummy OTP '123456' for subject: {}", subject);
        return "123456";
    }
}
