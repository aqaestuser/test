package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.component.HeaderComponent;
import xyz.npgw.test.component.SystemMenuComponent;
import xyz.npgw.test.page.base.BasePage;

public abstract class SystemBasePage extends BasePage {

    public SystemBasePage(Page page) {
        super(page);
    }

    public HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }

    public SystemMenuComponent getSystemAdministrationMenuComponent() {
        return new SystemMenuComponent(getPage());
    }
}
