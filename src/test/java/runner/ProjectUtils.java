package runner;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectUtils {

    public static String getTestClassMethodName(Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }

    public static String getTestClassMethodNameWithInvocationCount(Method method, ITestResult testResult) {
        String testMethodName = getTestClassMethodName(method);
        if (!method.getAnnotation(Test.class).dataProvider().isEmpty()) {
            testMethodName += "(" + testResult.getMethod().getCurrentInvocationCount() + ")";
        }
        return testMethodName;
    }

    public static String setNameFromDateAndTime() {
        return new SimpleDateFormat("ddMMMyyyy_HH-mm-ss").format(new Date());
    }

    public static void navigateToBaseURL(Page page) {
        page.navigate("/");
        page.waitForLoadState(LoadState.LOAD);
    }
}
