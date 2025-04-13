package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.trait.HeaderTrait;

public abstract class BaseHeaderPage extends BasePage implements HeaderTrait {

    public BaseHeaderPage(Page page) {
        super(page);
    }
}
