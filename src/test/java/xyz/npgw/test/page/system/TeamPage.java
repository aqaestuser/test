package xyz.npgw.test.page.system;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.User;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.UserTableTrait;
import xyz.npgw.test.page.dialog.user.AddUserDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Log4j2
public class TeamPage extends BaseSystemPage<TeamPage> implements
        UserTableTrait,
        SelectCompanyTrait<TeamPage>,
        AlertTrait<TeamPage>,
        SelectStatusTrait<TeamPage> {

    public TeamPage(Page page) {
        super(page);
    }

    @Step("Click 'Add user' button")
    public AddUserDialog clickAddUserButton() {
        getByTestId("AddUserButtonTeamPage").click();

        return new AddUserDialog(getPage());
    }

    @Step("Click 'Edit user' button")
    public EditUserDialog clickEditUserButton(String username) {
//        Locator editButton = getTable().getRow(username).getByTestId("EditUserButton");
//        editButton.waitFor();
        getTable().getRow(username).getByTestId("EditUserButton").click();

        return new EditUserDialog(getPage());
    }

    @Step("Click 'Refresh data' button")
    public TeamPage clickRefreshDataButton() {
        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));
        getByTestId("ApplyFilterButtonTeamPage").click();
        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        return this;
    }

    @Step("Click 'Reset filter' button")
    public TeamPage clickResetFilterButton() {
        getByTestId("ResetFilterButtonTeamPage").click();

        return this;
    }

    @SneakyThrows
    public TeamPage waitForUser(APIRequestContext request, String email, String companyName) {
        int timeout = (int) ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(User.getAll(request, companyName)).noneMatch(user -> user.email().equals(email))) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Timeout %dms exceeded waiting for user %s presence".formatted(timeout, email));
            }
        }

        clickRefreshDataButton();

        return new TeamPage(getPage());
    }
}
