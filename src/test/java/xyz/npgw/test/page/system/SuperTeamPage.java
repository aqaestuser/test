package xyz.npgw.test.page.system;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.UsersTableTrait;
import xyz.npgw.test.page.dialog.user.AddUserDialog;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Log4j2
public class SuperTeamPage extends SuperSystemPage<SuperTeamPage>
        implements UsersTableTrait,
                   SelectCompanyTrait<SuperTeamPage>,
                   SelectStatusTrait<SuperTeamPage> {

    public SuperTeamPage(Page page) {
        super(page);
    }

    @Step("Click 'Add user' button")
    public AddUserDialog clickAddUserButton() {
        getByTestId("AddUserButtonTeamPage").click();

        return new AddUserDialog(getPage());
    }

    @Step("Click 'Refresh data' button")
    public SuperTeamPage clickRefreshDataButton() {
        getPage().waitForResponse("**/portal-v1/user/list/*", getByTestId("ApplyFilterButtonTeamPage")::click);

        return this;
    }

    @Step("Click 'Reset filter' button")
    public SuperTeamPage clickResetFilterButton() {
        getByTestId("ResetFilterButtonTeamPage").click();

        return this;
    }

    @SneakyThrows
    public SuperTeamPage waitForUserPresence(APIRequestContext request, String email, String companyName) {
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

        return this;
    }

    @SneakyThrows
    public SuperTeamPage waitForUserAbsence(APIRequestContext request, String email, String companyName) {
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

        return this;
    }

    @SneakyThrows
    public SuperTeamPage waitForUserActivation(APIRequestContext request, String email, String companyName) {
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

        return this;
    }

    @SneakyThrows
    public SuperTeamPage waitForUserDeactivation(APIRequestContext request, String email, String companyName) {
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

        return this;
    }
}
