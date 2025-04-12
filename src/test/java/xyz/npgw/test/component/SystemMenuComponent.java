package xyz.npgw.test.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.AcquirersPage;

public class SystemMenuComponent extends BaseComponent {

    private final Locator acquirersTab = tab("Acquirers");

    public SystemMenuComponent(Page page) {
        super(page);
    }

    @Step("Click Acquirers Tab")
    public AcquirersPage clickAcquirersTab() {
        acquirersTab.click();

        return new AcquirersPage(getPage());
    }
}
