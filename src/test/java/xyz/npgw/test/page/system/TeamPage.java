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
public class TeamPage extends BaseSystemPage<TeamPage> implements TableTrait, SelectCompanyTrait<TeamPage> {

    private final Locator applyFilterButton = getByTestId("ApplyFilterButtonTeamPage");
    private final Locator addUserButton = getByTestId("AddUserButtonTeamPage");

    public TeamPage(Page page) {
        super(page);
    }

    @Step("Click 'Add user' button")
    public AddUserDialog clickAddUserButton() {
        addUserButton.click();

        return new AddUserDialog(getPage());
    }

    @Step("Click 'Edit user'")
    public EditUserDialog clickEditUser(String email) {
        Locator editButton = getPage().getByRole(
                AriaRole.ROW, new Page.GetByRoleOptions().setName(email)).getByTestId("EditUserButton");
        editButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        editButton.click();

        return new EditUserDialog(getPage());
    }

    public Locator getUsernameByEmail(String email) {
        Locator row = getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email));

        return row.locator("td").first();
    }

    public Locator getUserRoleByEmail(String email) {
        Locator row = getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email));

        return row.locator("td").nth(1);
    }

    public Locator getUserStatusByEmail(String email) {
        Locator row = getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email));

        return row.locator("td").nth(2);
    }

    @Step("Click 'Apply filter")
    public TeamPage clickApplyFilter() {
        applyFilterButton.click();

        return this;
    }

    public Locator getChangeUserActivityButton(String email) {
        return getPage()
                .getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(email))
                .getByTestId("ChangeUserActivityButton")
                .locator("svg");
    }

    @Step("Click user activation button")
    public ChangeUserActivityDialog clickChangeUserActivityButton(String email) {
        Locator button = getChangeUserActivityButton(email);
        button.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        button.click();

        return new ChangeUserActivityDialog(getPage());
    }
}
