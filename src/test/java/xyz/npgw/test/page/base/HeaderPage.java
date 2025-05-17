package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.trait.HeaderTrait;

public abstract class HeaderPage extends BasePage implements HeaderTrait {

    public HeaderPage(Page page) {
        super(page);
    }
}
