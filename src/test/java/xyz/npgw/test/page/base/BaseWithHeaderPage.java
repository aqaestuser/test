package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.component.HeaderComponent;

public abstract class BaseWithHeaderPage extends BasePage {

    public BaseWithHeaderPage(Page page) {
        super(page);
    }

    public HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }
}
