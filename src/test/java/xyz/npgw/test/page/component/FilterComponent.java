package xyz.npgw.test.page.component;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.base.BasePage;

public class FilterComponent<T extends BasePage<T>> extends BaseComponent<T> {

    public FilterComponent(Page page, T owner) {
        super(page, owner);
    }
}
