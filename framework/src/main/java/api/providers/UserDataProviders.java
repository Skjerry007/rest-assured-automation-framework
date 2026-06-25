package api.providers;

import common.utils.TestDataUtil;
import api.models.User;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;

/**
 * UserDataProviders - TestNG Data Providers for User API tests
 */
public class UserDataProviders {

    @DataProvider(name = "userDataProvider")
    public static Object[][] userDataProvider(Method method) {
        String testName = method.getName();
        String jsonKey = "";
        
        if (testName.equals("testCreateUser")) {
            jsonKey = "validUser";
        } else if (testName.equals("testUpdateUser")) {
            jsonKey = "updateUser";
        } else {
            throw new IllegalArgumentException("No data key mapping found for test method: " + testName);
        }
        
        User user = TestDataUtil.getTestData("users.json", jsonKey, User.class);
        return new Object[][] { { user } };
    }
}
