package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import xyz.npgw.test.page.base.BaseComponent;

public class AlertComponent<CurrentPageT> extends BaseComponent {

    private final Locator alertMessage = locator("[role='alert']");

    private final CurrentPageT currentPage;

    public AlertComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.currentPage = currentPage;
    }

    public CurrentPageT waitUntilSuccessAlertIsGone() {
        alert("SUCCESS").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        alert("SUCCESS").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return currentPage;
    }

    public Locator getAlertMessage() {
        alertMessage.waitFor();

        return alertMessage;
    }
}
