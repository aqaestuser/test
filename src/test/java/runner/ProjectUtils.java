package runner;

import com.microsoft.playwright.Page;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void navigateToBaseURL(Page page) {
        page.navigate("/");
    }
}
