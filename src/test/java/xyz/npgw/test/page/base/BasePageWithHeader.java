package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.Header;

public abstract class BasePageWithHeader extends BasePage {

    public BasePageWithHeader(Page page) {
        super(page);
    }

    public Header getHeader() {
        return new Header(getPage());
    }
}
