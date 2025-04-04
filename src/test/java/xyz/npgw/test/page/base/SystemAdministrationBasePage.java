package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.HeaderComponent;
import xyz.npgw.test.page.component.SystemAdministrationMenuComponent;

public abstract class SystemAdministrationBasePage extends BasePage {

    public SystemAdministrationBasePage(Page page) {
        super(page);
    }

    public HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }

    public SystemAdministrationMenuComponent getSystemAdministrationMenuComponent() {
        return new SystemAdministrationMenuComponent(getPage());
    }
}
