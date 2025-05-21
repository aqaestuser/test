package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;

public class AlertComponent<CurrentPageT> extends BaseComponent {

    private final Locator alertMessage = locator("[role='alert']");
    private final Locator successMessage = getByRole(AriaRole.ALERT, "SUCCESS");

    private final CurrentPageT currentPage;

    public AlertComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.currentPage = currentPage;
    }

    public CurrentPageT waitUntilSuccessAlertIsPresent() {
        successMessage.waitFor();

        return currentPage;
    }

    public CurrentPageT waitUntilSuccessAlertIsHidden() {
        successMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return currentPage;
    }

    public CurrentPageT waitUntilSuccessAlertIsGone() {
        waitUntilSuccessAlertIsPresent();
        waitUntilSuccessAlertIsHidden();

        return currentPage;
    }

    public Locator getMessage() {
        alertMessage.waitFor();

        return alertMessage;
    }

    @Step("Close 'SUCCESS' alert message")
    public CurrentPageT clickCloseButton() {
        successMessage.getByLabel("Close");

        return currentPage;
    }
}
