package xyz.npgw.test.page.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public abstract class BaseModel {

    private final Page page;

    public BaseModel(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    protected Locator placeholder(String placeholder) {
        return getPage().getByPlaceholder(placeholder);
    }

    protected Locator button(String name) {
        return getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator checkbox(String name) {
        return getPage().getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator optionByLabelExactText(String text) {
        return getPage().getByLabel(text, new Page.GetByLabelOptions().setExact(true)).getByText(text);
    }

    protected Locator locator(String selector) {
        return getPage().locator(selector);
    }

    protected Locator dialog() {
        return getPage().getByRole(AriaRole.DIALOG);
    }

    protected Locator tab(String name) {
        return getPage().getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator labelExact(String text) {
        return getPage().getByLabel(text, new Page.GetByLabelOptions().setExact(true));
    }

    protected Locator textExact(String text) {
        return getPage().getByText(text, new Page.GetByTextOptions().setExact(true));
    }

    protected Locator group(String name) {
        return getPage().getByRole(AriaRole.GROUP, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator link(String name) {
        return getPage().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator option(Locator locator) {
        return locator.getByRole(AriaRole.OPTION);
    }
}
