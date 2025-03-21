package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public final class LoginPage extends BasePage {

    private final Locator emailField;
    private final Locator passwordField;
    private final Locator loginButton;

    public LoginPage(Page page) {
        super(page);
        emailField = placeholder("Enter your email");
        passwordField = placeholder("Enter your password");
        loginButton = button("Login");
    }

    public LoginPage fillEmailField(String userEmail) {
        emailField.fill(userEmail);
        return this;
    }

    public LoginPage fillPasswordField(String userPassword) {
        passwordField.fill(userPassword);
        return this;
    }

    public HomePage clickLoginButton() {
        loginButton.click();
        return new HomePage(getPage());
    }
}
