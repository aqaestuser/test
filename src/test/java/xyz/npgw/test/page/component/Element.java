package xyz.npgw.test.page.component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public abstract class Element {

    private final Page page;
    protected final String selector;

    public Element(final Page page, final String selector) {
        this.page = page;
        this.selector = selector;
    }

    protected Locator link(String text) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator button(String text) {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(text));
    }

    protected boolean isVisible() {
        return getLocator().isVisible();
    }

    protected Locator getLocator() {
        return page.locator(selector).first();
    }

    protected Page getPage() {
        return page;
    }
}
