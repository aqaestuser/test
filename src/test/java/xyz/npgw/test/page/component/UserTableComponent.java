package xyz.npgw.test.page.component;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.base.BaseTableComponent;

public class UserTableComponent<T extends BasePage<T>, R> extends BaseTableComponent<T> {

    public UserTableComponent(Page page, T owner, R goback) {
        super(page, owner);
    }
}
