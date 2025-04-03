package xyz.npgw.test.page.component;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.base.BaseTableComponent;

public class UserProfileComponent<T extends BasePage<T>> extends BaseTableComponent<T> {
    public UserProfileComponent(Page page, T owner) {
        super(page, owner);
    }
}
