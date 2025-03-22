package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public final class DashboardPage extends BasePage {
    private final Locator logOutButton = button("Log out");

    public DashboardPage(Page page) {
        super(page);
    }

    @Step("Нажать на кнопку 'Log out'")
    public LoginPage clickLogOutButton() {
        logOutButton.click();
        return new LoginPage(getPage());
    }
}
