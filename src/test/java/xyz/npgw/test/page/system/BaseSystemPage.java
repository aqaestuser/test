package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.HeaderPage;

@SuppressWarnings("unchecked")
public abstract class BaseSystemPage<SystemPageT extends BaseSystemPage<SystemPageT>> extends HeaderPage
        implements MenuTrait {

    private final Locator alertMessage = locator("div[role='alert']");

    public BaseSystemPage(Page page) {
        super(page);
    }

    @Step("Click on the 'System administration' button in the Header")
    public SystemPageT clickSystemAdministrationLink() {
        getHeader().getSystemAdministrationButton().click();
        getPage().waitForLoadState(LoadState.NETWORKIDLE);

        return (SystemPageT) this;
    }

    public Locator getAlertMessage() {
        alertMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return alertMessage;
    }
}
