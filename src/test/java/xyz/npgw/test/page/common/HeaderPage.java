package xyz.npgw.test.page.common;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BasePage;

public abstract class HeaderPage extends BasePage implements HeaderTrait {

    public HeaderPage(Page page) {
        super(page);
    }
}
