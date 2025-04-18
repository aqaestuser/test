package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.util.ResponseUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.system.TeamPage;

@Getter
public class HeaderComponent extends BaseComponent {

    private final Locator img = altText("logo");
    private final Locator logo = link().filter(new Locator.FilterOptions().setHas(img));
    private final Locator transactionsButton = linkByName("Transactions");
    private final Locator reportsButton = linkByName("Reports");
    private final Locator systemAdministrationButton = linkByName("System administration");
    private final Locator logOutButton = textExact("Log out");
    private final Locator userMenuButton = getByTestId("userMenuToggle");
    private final Locator profileSettingsButton = textExact("Profile Settings");
    private final Locator passwordField = placeholder("Enter new password");
    private final Locator repeatPasswordField = placeholder("Repeat new password");
    private final Locator saveButton = locator("button:has-text('Save')");
    private final Locator logOutButtonUserMenu = menuItemByName("Log Out");

    public HeaderComponent(Page page) {
        super(page);
    }

    @Step("Click on 'Transactions' menu in Header")
    public TransactionsPage clickTransactionsLink() {
        ResponseUtils.clickAndWaitForResponse(getPage(), transactionsButton, Constants.TRANSACTION_HISTORY_ENDPOINT);

        return new TransactionsPage(getPage());
    }

    @Step("Click on 'Reports' menu in Header")
    public ReportsPage clickReportsLink() {
        reportsButton.click();

        return new ReportsPage(getPage());
    }

    @Step("Click on 'System administration' menu in Header")
    public TeamPage clickSystemAdministrationLink() {
        systemAdministrationButton.click();
        getPage().waitForLoadState(LoadState.NETWORKIDLE);

        return new TeamPage(getPage());
    }

    @Step("Press 'Log out' button")
    public LoginPage clickLogOutButton() {
        logOutButton.click();

        return new LoginPage(getPage());
    }

    @Step("Press 'Logo' button")
    public DashboardPage clickLogoButton() {
        logo.click();

        return new DashboardPage(getPage());
    }

    @Step("Press 'User menu' button")
    public DashboardPage clickUserMenuButton() {
        userMenuButton.click();

        return new DashboardPage(getPage());
    }

    @Step("Press 'Profile Settings' button")
    public DashboardPage clickProfileSettingsButton() {
        profileSettingsButton.click();

        return new DashboardPage(getPage());
    }

    @Step("Enter new password in the 'Password' field")
    public DashboardPage fillPasswordField(String newPassword) {
        passwordField.fill(newPassword);

        return new DashboardPage(getPage());
    }

    @Step("Enter new password in the 'Repeat Password' field")
    public DashboardPage fillRepeatPasswordField(String newPassword) {
        repeatPasswordField.fill(newPassword);

        return new DashboardPage(getPage());
    }

    @Step("Press 'Save' button")
    public DashboardPage clickSaveButton() {
        saveButton.click();

        return new DashboardPage(getPage());
    }

    @Step("Press 'Log out' button in User menu")
    public LoginPage clickLogOutButtonUserMenu() {

        getPage().waitForLoadState(LoadState.NETWORKIDLE);
        logOutButtonUserMenu.click();
        getPage().waitForURL("**/");

        return new LoginPage(getPage());
    }
}
