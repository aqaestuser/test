package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public abstract class BasePage extends BaseModel {

    public BasePage(Page page) {
        super(page);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
}
