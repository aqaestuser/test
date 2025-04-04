package xyz.npgw.test.page.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.systemadministration.AcquirersPage;

public class SystemAdministrationMenuComponent extends BaseComponent {

    private final Locator acquirersTab = tab("Acquirers");

    public SystemAdministrationMenuComponent(Page page) {
        super(page);
    }

    @Step("Click Acquirers Tab")
    public AcquirersPage clickAcquirersTab() {
        acquirersTab.click();

        return new AcquirersPage(getPage());
    }
}
