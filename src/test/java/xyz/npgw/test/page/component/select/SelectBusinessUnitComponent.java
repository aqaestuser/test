package xyz.npgw.test.page.component.select;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SelectBusinessUnitComponent<CurrentPageT> extends SelectComponent<CurrentPageT> {

    @Getter
    private final Locator selectBusinessUnitField = getByLabelExact("Business unit");
    @Getter
    private final Locator selectBusinessUnitPlaceholder = locator("input[aria-label='Business unit']");
    @Getter
    private final Locator businessUnitDropdown = locator("div[aria-label='Suggestions']");
    @Getter
    private final Locator dropdownOptionList = getPage().getByRole(AriaRole.OPTION);
    private final Locator selectBusinessUnitContainer = locator("div[data-slot='input-wrapper']")
            .filter(new Locator.FilterOptions().setHas(selectBusinessUnitPlaceholder));
    private final Locator selectBusinessUnitDropdownChevron = selectBusinessUnitContainer
            .locator("button[aria-label='Show suggestions']:last-child");
    private final Locator selectBusinessUnitClearIcon = selectBusinessUnitContainer
            .locator("button[aria-label='Show suggestions']:first-child");

    public SelectBusinessUnitComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    @Step("Select '{businessUnitName}' business unit using filter")
    public CurrentPageT selectBusinessUnit(String businessUnitName) {
        select(selectBusinessUnitField, businessUnitName);

        return currentPage;
    }

    @Step("Click select Business unit clear icon")
    public CurrentPageT clickSelectBusinessUnitClearIcon() {
        selectBusinessUnitClearIcon.dispatchEvent("click");

        return currentPage;
    }

    @Step("Click Business unit dropdown toggle arrow '˅˄'")
    public CurrentPageT clickSelectBusinessUnitDropdownChevron() {
        selectBusinessUnitDropdownChevron.click();

        return currentPage;
    }

    @Step("Click 'Select Business unit' placeholder")
    public CurrentPageT clickSelectBusinessUnitPlaceholder() {
        selectBusinessUnitPlaceholder.click();

        return currentPage;
    }
}
