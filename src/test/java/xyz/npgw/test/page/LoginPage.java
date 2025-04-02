package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePage;

import static io.qameta.allure.model.Parameter.Mode.MASKED;

public final class LoginPage extends BasePage {

    private final Locator emailField = placeholder("Enter your email");
    private final Locator passwordField = placeholder("Enter your password");
    private final Locator loginButton = button("Login");
    private final Locator rememberMeCheckbox = checkbox("Remember me");
    private final Locator loginFormTitle = locator(".login-form-container h3");

    public LoginPage(Page page) {
        super(page);
    }

    public Locator getEmailField() {
        return emailField;
    }

    public Locator getLoginFormTitle() {
        return loginFormTitle;
    }

    @Step("Enter the user's email and password")
    public DashboardPage login(String user, String pwd) {
        return new LoginPage(getPage())
                .fillEmailField(user)
                .fillPasswordField(pwd)
                .checkRememberMeCheckbox()
                .clickLoginButton();
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

    @Step("Press 'Login' button")
    public DashboardPage clickLoginButton() {
        loginButton.click();

        return new DashboardPage(getPage());
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

    @Step("Navigate to '{url}' endpoint")
    public LoginPage navigate(String url) {
        getPage().navigate(url);

        return this;
    }
}
