package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.HeaderComponent;
import xyz.npgw.test.page.component.TableComponent;

public abstract class BasePageWithHeaderAndTable extends BasePage {

    public BasePageWithHeaderAndTable(Page page) {
        super(page);
    }

    public HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }

    public TableComponent getTable() {
        return new TableComponent(getPage());
    }
}
