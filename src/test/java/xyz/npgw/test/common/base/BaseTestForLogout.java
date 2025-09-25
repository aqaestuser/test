package xyz.npgw.test.common.base;

import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.common.util.BrowserUtils;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.AboutBlankPage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Log4j2
public abstract class BaseTestForLogout extends BaseTest {

    @BeforeMethod
    protected void beforeMethod(Method method, ITestResult testResult, Object[] args) {
        testId = "%s/%s/%s(%d)%s".formatted(
                ProjectProperties.getArtefactDir(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                testResult.getMethod().getCurrentInvocationCount(),
                new SimpleDateFormat("_MMdd_HHmmss").format(new Date()));

        browserContext = BrowserUtils.createContext(browser);

        if (ProjectProperties.isTracingMode()) {
            BrowserUtils.startTracing(browserContext);
        }

        createBlankPage();

        initApiRequestContext();

        String methodName = method.getName();
        String suffix = Stream.of("Unauthenticated", "AsTestUser", "AsTestAdmin", "AsUser", "AsAdmin")
                .filter(methodName::endsWith)
                .findFirst()
                .orElse("");

        switch (suffix) {
            case "Unauthenticated":
                return;

            case "AsTestUser":
                new AboutBlankPage(page)
                        .navigate("/")
                        .loginAsUser("testUser@email.com", ProjectProperties.getPassword());
                return;

            case "AsTestAdmin":
                new AboutBlankPage(page)
                        .navigate("/")
                        .loginAsAdmin("testAdmin@email.com", ProjectProperties.getPassword());
                return;

            case "AsUser":
                openSite(new Object[]{"USER"});
                return;

            case "AsAdmin":
                openSite(new Object[]{"ADMIN"});
                return;

            default:
                openSite(args);
        }
    }

    @AfterMethod
    protected void afterMethod(Method method, ITestResult testResult) throws IOException {
        if (!testResult.isSuccess() && !ProjectProperties.isCloseBrowserIfError()) {
            page.pause();
        }

        int resultStatus = testResult.getStatus();
        long testDuration = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;
        if (resultStatus == ITestResult.FAILURE) {
            log.info("{} <<< {} in {} s", status(resultStatus), testId, testDuration);
        }

        if (!page.isClosed()) {
            page.close();
            attachVideoIfNeeded(page, testId, resultStatus);
        }

        if (browserContext != null) {
            if (ProjectProperties.isTracingMode()) {
                if (resultStatus == ITestResult.FAILURE || resultStatus == ITestResult.SKIP) {
                    Path traceFilePath = Paths.get(testId + ".zip");
                    browserContext.tracing().stop(new Tracing.StopOptions().setPath(traceFilePath));
                    Allure.getLifecycle().addAttachment(
                            "tracing", "archive/zip", "zip", Files.readAllBytes(traceFilePath));
                } else {
                    browserContext.tracing().stop();
                }
            }
            browserContext.close();
        }
    }

    private void openSite(Object[] args) {
        UserRole userRole = UserRole.SUPER;
        if (args.length != 0 && (args[0] instanceof String)) {
            try {
                userRole = UserRole.valueOf((String) args[0]);
            } catch (IllegalArgumentException e) {
                if (args[0].equals("UNAUTHORISED")) {
//                    new AboutBlankPage(page).navigate("/");
                    return;
                }
            }
        }

        if (userRole == UserRole.USER && businessUnit == null) {
            businessUnit = TestUtils.createBusinessUnit(apiRequestContext, companyName, "default");
        }

        String email = "%s.%s@email.com".formatted(uid, userRole.toString().toLowerCase());
        if (!User.exists(apiRequestContext, email)) {
            User user = User.builder()
                    .companyName((userRole == UserRole.SUPER ? "super" : companyName))
                    .userRole(userRole)
                    .merchantIds(userRole == UserRole.USER ? new String[]{businessUnit.merchantId()} : new String[]{})
                    .email(email)
                    .build();
            User.create(apiRequestContext, user);
            User.passChallenge(apiRequestContext, user.getEmail(), user.getPassword());
        }

        new AboutBlankPage(page).navigate("/").loginAs(email, ProjectProperties.getPassword(), userRole.getName());
    }
}
