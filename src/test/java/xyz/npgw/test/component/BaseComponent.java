package xyz.npgw.test.component;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BaseModel;

public abstract class BaseComponent extends BaseModel {

    public BaseComponent(Page page) {
        super(page);
    }
}
