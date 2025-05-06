package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.common.SelectCompanyTrait;
import xyz.npgw.test.page.common.TableTrait;
import xyz.npgw.test.page.dialog.user.AddUserDialog;
import xyz.npgw.test.page.dialog.user.ChangeUserActivityDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;

@Log4j2
public class TeamPage extends BaseSystemPage<TeamPage> implements TableTrait<TeamPage>, SelectCompanyTrait<TeamPage> {

    private final Locator applyFilterButton = getByTestId("ApplyFilterButtonTeamPage");
    private final Locator addUserButton = getByTestId("AddUserButtonTeamPage");
    private final Locator selectCompanyInput = placeholder("Search...");

    public TeamPage(Page page) {
        super(page);
    }

    public Locator userRow(String username) {
        return getPage().getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(username));
    }

    @Step("Click 'Add user' button")
    public AddUserDialog clickAddUserButton() {
        addUserButton.click();

        return new AddUserDialog(getPage());
    }

    @Step("Click 'Edit user'")
    public EditUserDialog clickEditUserButton(String username) {
        Locator editButton = userRow(username).getByTestId("EditUserButton");
        editButton.waitFor();
        editButton.click();

        return new EditUserDialog(getPage());
    }

    public Locator getUserEmailByUsername(String username) {
        return userRow(username).locator("td").first();
    }

    public Locator getUserRoleByUsername(String username) {
        return userRow(username).locator("td").nth(1);
    }

    public Locator getUserStatusByUsername(String username) {
        return userRow(username).locator("td").nth(2);
    }

    @Step("Click 'Apply filter")
    public TeamPage clickApplyFilter() {
        selectCompanyInput.waitFor();

        applyFilterButton.click();

        return this;
    }

    public Locator getChangeUserActivityButton(String username) {
        return userRow(username)
                .getByTestId("ChangeUserActivityButton")
                .locator("svg");
    }

    @Step("Click user activation button")
    public ChangeUserActivityDialog clickChangeUserActivityButton(String username) {
        Locator button = getChangeUserActivityButton(username);
        button.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        button.click();

        return new ChangeUserActivityDialog(getPage());
    }

    public TeamPage waitUntilAlertIsGone() {
        alert("SUCCESS").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        alert("SUCCESS").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }

    @Step("Click 'Refresh data'")
    public TeamPage clickRefreshData() {
        getPage().getByTestId("ApplyFilterButtonTeamPage").click();

        return this;
    }
}
