import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CaptchaSolver {
    private static final String API_KEY = "YOUR_2CAPTCHA_API_KEY"; // Replace with your actual API key
    private static final String SOLVE_URL = "http://2captcha.com/in.php";
    private static final String RESULT_URL = "http://2captcha.com/res.php";

    public static String solveCaptcha(WebDriver driver) throws IOException, InterruptedException {
        // Take screenshot of the CAPTCHA
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        Path screenshotPath = Paths.get("target", "captcha.png");
        Files.copy(screenshot.toPath(), screenshotPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Convert screenshot to base64
        byte[] imageBytes = Files.readAllBytes(screenshotPath);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // Send CAPTCHA to 2Captcha
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(SOLVE_URL))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString("key=" + API_KEY + "&method=base64&body=" + base64Image))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String captchaId = response.body().split("\\|")[1];

        // Poll for CAPTCHA solution
        while (true) {
            Thread.sleep(5000); // Wait 5 seconds between polls
            request = HttpRequest.newBuilder()
                .uri(URI.create(RESULT_URL + "?key=" + API_KEY + "&action=get&id=" + captchaId))
                .GET()
                .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.body().contains("CAPCHA_NOT_READY")) {
                continue;
            }
            return response.body().split("\\|")[1];
        }
    }
} 