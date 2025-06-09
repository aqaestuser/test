package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;
import xyz.npgw.test.page.base.HeaderPage;

@Getter
public class SelectStatusComponent<CurrentPageT extends HeaderPage<?>> extends BaseComponent {
    @Getter(AccessLevel.NONE)
    private final CurrentPageT currentPage;
    private final Locator statusSelector = getByLabelExact("Status");
    private final Locator statusValue = statusSelector.locator("[data-slot='value']");
    private final Locator statusDropdown = locator("div[data-slot='listbox']");
    private final Locator statusOptions = statusDropdown.getByRole(AriaRole.OPTION);

    public SelectStatusComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.currentPage = currentPage;
    }

    @Step("Click status '{value}' in dropdown")
    public CurrentPageT clickValue(String value) {
        statusOptions
                .getByText(value, new Locator.GetByTextOptions().setExact(true))
                .click();

        return currentPage;
    }

    public CurrentPageT waitUntilDropdownGone() {
        statusDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return currentPage;
    }

    @Step("Click 'Status' Selector")
    public CurrentPageT clickSelector() {
        statusValue.click();

        return currentPage;
    }

    @Step("Select status '{value}'")
    public CurrentPageT select(String value) {
        clickSelector();
        clickValue(value);
        waitUntilDropdownGone();

        return currentPage;
    }

    @Step("Select status '{statuses}' from dropdown menu")
    public CurrentPageT selectTransactionStatuses(String... statuses) {
        clickSelector();
        for (String value : statuses) {
            clickValue(value);
        }
        getPage().keyboard().press("Tab");

        return currentPage;
    }
}
