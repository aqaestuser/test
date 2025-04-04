package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.HeaderComponent;

public abstract class BasePageWithHeader extends BasePage {

    public BasePageWithHeader(Page page) {
        super(page);
    }

    public HeaderComponent getHeader() {
        return new HeaderComponent(getPage());
    }
}
