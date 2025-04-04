package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.HeaderComponent;
import xyz.npgw.test.page.component.SystemAdministrationMenuComponent;
import xyz.npgw.test.page.component.TableComponent;

public abstract class SystemAdministrationWithTableBasePage extends BasePage{

    public SystemAdministrationWithTableBasePage(Page page) {
        super(page);
    }

    public HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }

    public SystemAdministrationMenuComponent getSystemAdministrationMenuComponent() {
        return new SystemAdministrationMenuComponent(getPage());
    }

    public TableComponent getTable() {
        return new TableComponent(getPage());
    }
}
