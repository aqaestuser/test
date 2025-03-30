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

    protected Locator linkByName(String text) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator optionByExactName(String text) {
        return page.getByLabel(text, new Page.GetByLabelOptions().setExact(true)).getByText(text);
    }

    protected Locator locator(String selector) {
        return page.locator(selector);
    }

    protected Locator columnHeader(String name) {
        return page.getByRole(AriaRole.COLUMNHEADER, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator dialog() {
        return page.getByRole(AriaRole.DIALOG);
    }

    protected Locator tab(String text) {
        return getPage().getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator labelExact(String text) {
        return getPage().getByLabel(text, new Page.GetByLabelOptions().setExact(true));
    }

    protected Locator textExact(String text) {
        return getPage().getByText(text, new Page.GetByTextOptions().setExact(true));
    }
}
