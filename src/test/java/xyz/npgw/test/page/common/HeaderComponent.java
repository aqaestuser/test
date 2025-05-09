package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
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

    private final Locator img = getPage().getByAltText("logo");
    private final Locator logo = getByRole(AriaRole.LINK).filter(new Locator.FilterOptions().setHas(img));
    private final Locator transactionsButton = getByRole(AriaRole.LINK, "Transactions");
    private final Locator reportsButton = getByRole(AriaRole.LINK, "Reports");
    private final Locator systemAdministrationButton = getByRole(AriaRole.LINK, "System administration");
    private final Locator logOutButton = getByTextExact("Log out");
    private final Locator userMenuButton = getByTestId("userMenuToggle");
    private final Locator profileSettingsButton = getByTextExact("Profile Settings");
    private final Locator passwordField = getByPlaceholder("Enter new password");
    private final Locator repeatPasswordField = getByPlaceholder("Repeat new password");
    private final Locator saveButton = locator("button:has-text('Save')");
    private final Locator logOutButtonUserMenu = getByRole(AriaRole.MENUITEM, "Log Out");
    private final Locator lightRadioButtonInUserMenu = getByRoleExact(AriaRole.RADIO, "Light");
    private final Locator darkRadioButtonInUserMenu = getByRoleExact(AriaRole.RADIO, "Dark");

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
        ResponseUtils.clickAndWaitForResponse(getPage(), systemAdministrationButton, Constants.ASSETS_TEAM);

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
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
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

    @Step("Click the 'Light' radio button in the user menu")
    public DashboardPage clickLightRadioButton() {
        lightRadioButtonInUserMenu.click();

        return new DashboardPage(getPage());
    }

    @Step ("Click the 'Dark' radio button in the user menu")
    public DashboardPage clickDarkRadioButton() {
        darkRadioButtonInUserMenu.click();

        return new DashboardPage(getPage());
    }

    public boolean isLogoImageLoaded() {
        return (boolean) getImg().evaluate(
                "img => img.complete && img.naturalWidth > 0 && img.naturalHeight > 0"
                        + " && !img.src.includes('base64') && !img.src.endsWith('.svg') && !img.src.endsWith('.ico')");
    }
}
