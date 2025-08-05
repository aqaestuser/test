package xyz.npgw.test.page.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.LoginPage;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.dialog.ProfileSettingsDialog;


@Getter
@SuppressWarnings("unchecked")
public abstract class HeaderPage<CurrentPageT extends HeaderPage<CurrentPageT>> extends BasePage
        implements AlertTrait<CurrentPageT> {

    private final Locator logOutButton = getByRole(AriaRole.BUTTON, "Log out");
    private final Locator userMenuButton = getByTestId("userMenuToggle");
    private final Locator profileSettingsButton = getByTextExact("Profile Settings");
    private final Locator logOutButtonInUserMenu = getByRole(AriaRole.MENUITEM, "Log Out");
    private final Locator lightRadioButtonInUserMenu = getByRoleExact(AriaRole.RADIO, "Light");
    private final Locator darkRadioButtonInUserMenu = getByRoleExact(AriaRole.RADIO, "Dark");

    public HeaderPage(Page page) {
        super(page);
    }

    protected CurrentPageT self() {
        return (CurrentPageT) this;
    }


    @Step("Click 'Log out' button")
    public LoginPage clickLogOutButton() {
        logOutButton.click();

        return new LoginPage(getPage());
    }

    @Step("Click 'User menu' button")
    public CurrentPageT clickUserMenuButton() {
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
        userMenuButton.click();

        return self();
    }

    @Step("Click 'Profile Settings' button")
    public ProfileSettingsDialog<CurrentPageT> clickProfileSettingsButton() {
        profileSettingsButton.click();

        return new ProfileSettingsDialog<>(getPage(), (CurrentPageT) this);
    }

    @Step("Click 'Log out' button in User menu")
    public LoginPage clickLogOutButtonUserMenu() {
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
        logOutButtonInUserMenu.click();
        getPage().waitForURL("**/");

        return new LoginPage(getPage());
    }

    @Step("Click the 'Light' radio button in the user menu")
    public CurrentPageT clickLightRadioButton() {
        lightRadioButtonInUserMenu.click();

        return self();
    }

    @Step("Click the 'Dark' radio button in the user menu")
    public CurrentPageT clickDarkRadioButton() {
        darkRadioButtonInUserMenu.click();

        return self();
    }
}
