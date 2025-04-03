package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;

public abstract class BasePage<T extends BasePage<T>> extends BaseModel {

    public BasePage(Page page) {
        super(page);
    }
}
