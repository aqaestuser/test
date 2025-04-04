package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.TableComponent;

public abstract class SystemAdministrationWithTableBasePage extends SystemAdministrationBasePage{

    public SystemAdministrationWithTableBasePage(Page page) {
        super(page);
    }

    public TableComponent getTable() {
        return new TableComponent(getPage());
    }
}
