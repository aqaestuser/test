package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class LoginPage extends BasePage {

    private final Locator userNameOrEmailField = placeholder("Enter your email");
    private final Locator userPasswordField = placeholder("Enter your password");
    private final Locator signInButton = button("Login");

    public LoginPage(Page page) {
        super(page);
    }

    public LoginPage fillUserEmailField(String userEmail) {
        userNameOrEmailField.fill(userEmail);
        return this;
    }

    public LoginPage fillUserPasswordField(String userPassword) {
        userPasswordField.fill(userPassword);
        return this;
    }

    public HomePage clickLoginButton() {
        signInButton.click();
        getPage().waitForLoadState(LoadState.LOAD);
        return new HomePage(getPage());
    }
}
