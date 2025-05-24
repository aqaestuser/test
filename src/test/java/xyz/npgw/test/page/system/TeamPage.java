package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.common.trait.UserTableTrait;
import xyz.npgw.test.page.dialog.user.AddUserDialog;
import xyz.npgw.test.page.dialog.user.EditUserDialog;

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
        Locator editButton = getTable().getRow(username).getByTestId("EditUserButton");
        editButton.waitFor();
        editButton.click();

        return new EditUserDialog(getPage());
    }

    @Step("Click 'Refresh data' button")
    public TeamPage clickRefreshDataButton() {
//        TODO remove after bugfix
        getPage().waitForTimeout(500);
        getByTestId("ApplyFilterButtonTeamPage").click();

        return this;
    }
}
