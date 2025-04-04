package xyz.npgw.test.page.base;

import com.microsoft.playwright.Page;

public abstract class BaseComponent extends BaseModel {

    public BaseComponent(Page page) {
        super(page);
    }
}
