package xyz.npgw.test.common.base;

import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
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
import java.util.Set;
import java.util.stream.Stream;

@Log4j2
public abstract class BaseTestForSingleLogin extends BaseTest {

    @Override
    @BeforeClass
    protected void beforeClass() {
        super.beforeClass();
        browserContext = BrowserUtils.createContext(browser);
        createBlankPage();
    }

    @BeforeMethod
    protected void beforeMethod(Method method, ITestResult testResult, Object[] args) {
        initApiRequestContext();

        testId = "%s/%s/%s(%d)%s".formatted(
                ProjectProperties.getArtefactDir(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                testResult.getMethod().getCurrentInvocationCount(),
                new SimpleDateFormat("_MMdd_HHmmss").format(new Date()));

        if (page.isClosed()) {
            createBlankPage();
            new AboutBlankPage(page).navigate("/dashboard");
        }

        if (ProjectProperties.isTracingMode()) {
            BrowserUtils.startTracing(browserContext);
        }
    }

    @AfterMethod
    protected void afterMethod(Method method, ITestResult testResult) throws IOException {
        int resultStatus = testResult.getStatus();
        boolean isNeedArtefacts = Set.of(ITestResult.FAILURE, ITestResult.SKIP).contains(resultStatus);
        if (resultStatus == ITestResult.FAILURE) {
            long testDuration = (testResult.getEndMillis() - testResult.getStartMillis()) / 1000;
            log.info("{} <<< {} in {} s", status(resultStatus), testId, testDuration);
        }
        if (!page.isClosed()) {
            page.close();
        }
        if (ProjectProperties.isVideoMode()) {
            attachVideoIfNeeded(page, testId, isNeedArtefacts);
        }

        if (ProjectProperties.isTracingMode()) {
            if (isNeedArtefacts) {
                Path traceFilePath = Paths.get(testId + ".zip");
                browserContext.tracing().stop(new Tracing.StopOptions().setPath(traceFilePath));
                Allure.getLifecycle().addAttachment(
                        "tracing", "archive/zip", "zip", Files.readAllBytes(traceFilePath));
            } else {
                browserContext.tracing().stop();
            }
        }
    }

    @Override
    @AfterClass(alwaysRun = true)
    protected void afterClass() {
        if (browserContext != null) {
            try {
                browserContext.close();
            } catch (Exception ignored) {
                log.info("Attempt to close the browserContext that is already closed.");
            }
        }
        super.afterClass();
    }

    private void openSite(String role) {
        UserRole userRole = UserRole.valueOf(role);

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

    protected void openSiteAccordingRole() {
        String testClassName = this.getClass().getSimpleName();
        String prefix = Stream.of("Unauthenticated", "TestUser", "TestAdmin", "User", "Admin")
                .filter(testClassName::startsWith)
                .findFirst()
                .orElse("");

        switch (prefix) {
            case "Unauthenticated":
                return;
            case "TestUser":
                new AboutBlankPage(page)
                        .navigate("/")
                        .loginAsUser("testUser@email.com", ProjectProperties.getPassword());
                return;
            case "TestAdmin":
                new AboutBlankPage(page)
                        .navigate("/")
                        .loginAsAdmin("testAdmin@email.com", ProjectProperties.getPassword());
                return;
            case "User":
                openSite("USER");
                return;
            case "Admin":
                openSite("ADMIN");
                return;
            default:
                openSite("SUPER");
        }
    }
}
