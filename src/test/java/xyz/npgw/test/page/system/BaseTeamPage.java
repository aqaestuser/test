package xyz.npgw.test.page.system;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.select.SelectStatusTrait;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Log4j2
@Getter
public abstract class BaseTeamPage<CurrentPageT extends HeaderPage<CurrentPageT>> extends HeaderPage<CurrentPageT>
        implements SelectStatusTrait<CurrentPageT> {

    public final Locator addUserButton = getByTestId("AddUserButtonTeamPage");
    private final Locator commonPanelIcon = locator("[role='tabpanel']>div>div:first-child button[data-testid] svg");
    private final Locator tooltip = getByRole(AriaRole.TOOLTIP);
    private final Locator transactionsTable = getByLabelExact("transactions table");
    private final Locator settings = getByRole(AriaRole.BUTTON).getByTestId("SettingsButtonTeamPage");
    private final Locator normalDensity = locator("[type='radio'][value='normal']");
    private final Locator condensedDensity = locator("[type='radio'][value='condensed']");

    public BaseTeamPage(Page page) {
        super(page);
    }

    @Step("Click 'Add user' button")
    public void clickAddUser() {
        addUserButton.click();
    }

    @Step("Click 'Refresh data' button")
    public CurrentPageT clickRefreshDataButton() {
        getPage().waitForResponse("**/portal-v1/user/list/*", getByTestId("ApplyFilterButtonTeamPage")::click);

        return self();
    }

    @Step("Click 'Reset filter' button")
    public CurrentPageT clickResetFilterButton() {
        getByTestId("ResetFilterButtonTeamPage").click();

        return self();
    }

    @SneakyThrows
    public CurrentPageT waitForUserPresence(APIRequestContext request, String email, String companyName) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(User.getAll(request, companyName)).noneMatch(user -> user.email().equals(email))) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for user '%s' presence".formatted(email));
            }
        }
        log.info("User presence wait took {}ms", ProjectProperties.getDefaultTimeout() - timeout);
        clickRefreshDataButton();

        return self();
    }

    @SneakyThrows
    public CurrentPageT waitForUserAbsence(APIRequestContext request, String email, String companyName) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(User.getAll(request, companyName)).anyMatch(user -> user.email().equals(email))) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for user '%s' absence".formatted(email));
            }
        }
        log.info("User absence wait took {}ms", ProjectProperties.getDefaultTimeout() - timeout);
        clickRefreshDataButton();

        return self();
    }

    @SneakyThrows
    public CurrentPageT waitForUserActivation(APIRequestContext request, String email, String companyName) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(User.getAll(request, companyName))
                .noneMatch(user -> user.email().equals(email) && user.enabled())) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for user '%s' activation".formatted(email));
            }
        }
        log.info("User activation wait took {}ms", ProjectProperties.getDefaultTimeout() - timeout);
        clickRefreshDataButton();

        return self();
    }

    @SneakyThrows
    public CurrentPageT waitForUserDeactivation(APIRequestContext request, String email, String companyName) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(User.getAll(request, companyName))
                .noneMatch(user -> user.email().equals(email) && !user.enabled())) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for user '%s' deactivation".formatted(email));
            }
        }
        log.info("User deactivation wait took {}ms", ProjectProperties.getDefaultTimeout() - timeout);
        clickRefreshDataButton();

        return self();
    }

    @Step("Click 'Settings'")
    public CurrentPageT clickSettings() {
        settings.click();

        return self();
    }

    @Step("Check 'condensed'")
    public CurrentPageT checkCondensed() {
        condensedDensity.check();

        return self();
    }

    @Step("Check 'normal'")
    public CurrentPageT checkNormal() {
        normalDensity.check();

        return self();
    }
}
