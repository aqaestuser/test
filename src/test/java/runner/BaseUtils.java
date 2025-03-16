package runner;

import TestData.Constants;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseUtils {

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
        if (page.title().equals(Constants.BASE_URL_TITLE)) {
            System.out.println("Base URL is opened");
        } else {
            System.out.println("Failed to open Base URL");
        }
    }
}
