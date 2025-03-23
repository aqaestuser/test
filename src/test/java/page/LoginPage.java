package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public final class LoginPage extends BasePage {

    private final Locator emailField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator rememberMeCheckbox;

    public LoginPage(Page page) {
        super(page);
        emailField = placeholder("Enter your email");
        passwordField = placeholder("Enter your password");
        loginButton = button("Login");
        rememberMeCheckbox = checkbox("Remember me");
    }

    @Step("Ввести в поле Email - email пользователя")
    public LoginPage fillEmailField(String userEmail) {
        emailField.fill(userEmail);
        return this;
    }

    public Locator getEmailField() {
        return emailField;
    }

    @Step("Ввести в поле Password - пароль пользователя")
    public LoginPage fillPasswordField(String userPassword) {
        passwordField.fill(userPassword);
        return this;
    }

    @Step("Нажать на кнопку 'Login'")
    public DashboardPage clickLoginButton() {
        loginButton.click();
        return new DashboardPage(getPage());
    }

    @Step("Отметить чекбокс 'Remember me'")
    public LoginPage checkRememberMeCheckbox() {
        rememberMeCheckbox.setChecked(true);
        return this;
    }

    @Step("Снять отметку c чекбокса 'Remember me'")
    public LoginPage uncheckRememberMeCheckbox() {
        rememberMeCheckbox.setChecked(false);
        return this;
    }
}
