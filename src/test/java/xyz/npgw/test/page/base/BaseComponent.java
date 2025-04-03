package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;

public abstract class BaseComponent<T extends BasePage<T>> extends BaseModel {

    public BaseComponent(Page page, T owner) {
        super(page);
    }
}
