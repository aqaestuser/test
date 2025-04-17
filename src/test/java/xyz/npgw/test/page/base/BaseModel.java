package xyz.npgw.test.page.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.Getter;

@Getter
public abstract class BaseModel {

    private final Page page;

    public BaseModel(Page page) {
        this.page = page;
    }

    protected Locator placeholder(String text) {
        return getPage().getByPlaceholder(text);
    }

    protected Locator buttonByName(String name) {
        return getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator checkbox(String text) {
        return getPage().getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator linkByName(String text) {
        return getPage().getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator locator(String selector) {
        return getPage().locator(selector);
    }

    protected Locator dialog() {
        return getPage().getByRole(AriaRole.DIALOG);
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

    protected Locator group(String text) {
        return getPage().getByRole(AriaRole.GROUP, new Page.GetByRoleOptions().setName(text));
    }

    protected Locator link() {
        return getPage().getByRole(AriaRole.LINK);
    }

    protected Locator altText(String text) {
        return getPage().getByAltText(text);
    }

    protected Locator option(Locator locator) {
        return locator.getByRole(AriaRole.OPTION);
    }

    protected Locator getByTestId(String testId) {
        return getPage().getByTestId(testId);
    }

    protected Locator optionByName(String name) {
        return getPage().getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(name));
    }

    protected Locator listboxByRole() {
        return getPage().getByRole(AriaRole.LISTBOX);
    }

    protected Locator optionByRole() {
        return getPage().getByRole(AriaRole.OPTION);
    }

    protected Locator menuItemByName(String text) {
        return getPage().getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName(text));
    }
}
