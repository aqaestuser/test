package xyz.npgw.test.common;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProjectUtils {

    public static String getTestClassMethodName(Method method) {
        return method.getDeclaringClass().getSimpleName() + "/" + method.getName();
    }

    public static String getTestClassCompleteMethodName(Method method, ITestResult testResult) {
        String testMethodName = getTestClassMethodName(method);
        if (!method.getAnnotation(Test.class).dataProvider().isEmpty()) {
            testMethodName = "%s(%d)".formatted(testMethodName, testResult.getMethod().getCurrentInvocationCount());
        }
        return testMethodName + setNameFromDateAndTime();
    }

    public static String setNameFromDateAndTime() {
        return new SimpleDateFormat("_MMdd_HHmmss").format(new Date());
    }

    public static void loginAsRole(Page page, UserRole userRole) {
        String emailPrefix = (userRole == UserRole.SUPER) ? "" : userRole.name().toLowerCase();
        page.navigate("/");
        page.getByPlaceholder("Enter your email").fill(emailPrefix + ProjectProperties.getUserEmail());
        page.getByPlaceholder("Enter your password").fill(ProjectProperties.getUserPassword());
        page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Remember me")).setChecked(false);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        assertThat(page).hasURL("/dashboard");
    }
}
