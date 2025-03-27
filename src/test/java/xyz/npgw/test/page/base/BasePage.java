package xyz.npgw.test.page.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public abstract class BasePage {

    private final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    protected Locator placeholder(String text) {
        return page.getByPlaceholder(text);
    }

    protected Locator button(String text) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator checkbox(String text) {
        return page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator link(String text) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text));
    }
}
