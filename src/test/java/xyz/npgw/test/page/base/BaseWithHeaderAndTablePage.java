package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.component.HeaderComponent;
import xyz.npgw.test.component.TableComponent;

public abstract class BaseWithHeaderAndTablePage extends BasePage {

    public BaseWithHeaderAndTablePage(Page page) {
        super(page);
    }

    public HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }

    public TableComponent getTable() {
        return new TableComponent(getPage());
    }
}
