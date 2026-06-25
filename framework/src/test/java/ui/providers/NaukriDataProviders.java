package ui.providers;

import common.config.ConfigManager;
import org.testng.annotations.DataProvider;

/**
 * NaukriDataProviders - TestNG Data Providers for Naukri UI tests
 */
public class NaukriDataProviders {

    @DataProvider(name = "naukriUploadData")
    public static Object[][] getUploadData() {
        ConfigManager config = ConfigManager.getInstance();
        return new Object[][] {
            { 
                config.getWebUrl(), 
                config.getNaukriEmail(), 
                config.getNaukriPassword(), 
                config.getResumePath() 
            }
        };
    }
}
