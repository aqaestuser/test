package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.dashboard.AdminDashboardPage;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dashboard.UserDashboardPage;

import static io.qameta.allure.model.Parameter.Mode.MASKED;

public final class LoginPage extends BasePage implements AlertTrait<LoginPage> {

    @Getter
    private final Locator emailField = getByPlaceholder("Enter your email");
    private final Locator passwordField = getByPlaceholder("Enter your password");
    private final Locator loginButton = getByRole(AriaRole.BUTTON, "Login");
    private final Locator rememberMeCheckbox = getByRole(AriaRole.CHECKBOX, "Remember me");
    @Getter
    private final Locator loginFormTitle = locator(".login-form-container h3");
    private final Locator newPasswordField = locator("input[aria-label='New password']");
    private final Locator repeatNewPasswordField = locator("input[aria-label='Repeat password']");
    private final Locator saveButton = getByRole(AriaRole.BUTTON, "Save");

    public LoginPage(Page page) {
        super(page);
    }

    @Step("Enter the user's email in the 'Email' field")
    public LoginPage fillEmailField(String userEmail) {
        emailField.fill(userEmail);

        return this;
    }

    @Step("Enter the user's password in the 'Password' field")
    public LoginPage fillPasswordField(@Param(name = "Password", mode = MASKED) String userPassword) {
        passwordField.fill(userPassword);

        return this;
    }

    @Step("Enter the user's password in the 'New password' field")
    public LoginPage fillNewPasswordField(@Param(name = "Password", mode = MASKED) String userPassword) {
        newPasswordField.fill(userPassword);

        return this;
    }

    @Step("Enter the user's password in the 'Repeat new password' field")
    public LoginPage fillRepeatNewPasswordField(@Param(name = "Password", mode = MASKED) String userPassword) {
        repeatNewPasswordField.fill(userPassword);

        return this;
    }

    @Step("Press 'Login' button")
    public SuperDashboardPage clickLoginButton() {
        loginButton.click();

        return new SuperDashboardPage(getPage());
    }

    @Step("Press 'Login' button to change password")
    public LoginPage clickLoginButtonToChangePassword() {
        loginButton.click();

        return this;
    }

    @Step("Press 'Login' button as disabled user")
    public LoginPage clickLoginButtonAsDisabledUser() {
        loginButton.click();

        return this;
    }

    @Step("Press 'Save' button to save changed password")
    public LoginPage clickSaveButton() {
        saveButton.click();

        return this;
    }

    @Step("Check 'Remember me' checkbox")
    public LoginPage checkRememberMeCheckbox() {
        rememberMeCheckbox.setChecked(true);

        return this;
    }

    @Step("Uncheck 'Remember me' checkbox")
    public LoginPage uncheckRememberMeCheckbox() {
        rememberMeCheckbox.setChecked(false);

        return this;
    }

    @Step("Login to the site as '{userRole}' with '{email}'")
    public void loginAs(String email, String password, String userRole) {
        fillEmailField(email);
        fillPasswordField(password);
        clickLoginButton();

        getPage().waitForURL("**/dashboard");
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));
    }

    public UserDashboardPage loginAsUser(String email, String password) {
        loginAs(email, password, UserRole.USER.getName());

        return new UserDashboardPage(getPage());
    }

    public AdminDashboardPage loginAsAdmin(String email, String password) {
        loginAs(email, password, UserRole.ADMIN.getName());

        return new AdminDashboardPage(getPage());
    }

    @Step("Login as disabled user with '{email}'")
    public LoginPage loginAsDisabledUser(String email, String password) {
        fillEmailField(email);
        fillPasswordField(password);
        clickLoginButtonAsDisabledUser();

        return new LoginPage(getPage());
    }
}
