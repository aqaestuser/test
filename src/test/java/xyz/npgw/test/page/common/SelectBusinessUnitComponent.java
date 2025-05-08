package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;

import java.util.NoSuchElementException;

public class SelectBusinessUnitComponent<CurrentPageT> extends BaseComponent {

    @Getter
    private final Locator selectBusinessUnitField = labelExact("Business unit");
    @Getter
    private final Locator selectBusinessUnitPlaceholder = locator("input[aria-label='Business unit']");
    @Getter
    private final Locator businessUnitDropdown = locator("div[aria-label='Suggestions']");
    private final Locator dropdownOptionList = getPage().getByRole(AriaRole.OPTION);
    private final Locator selectBusinessUnitContainer =
            locator("div[data-slot='input-wrapper']")
                    .filter(new Locator.FilterOptions().setHas(selectBusinessUnitPlaceholder));
    private final Locator selectBusinessUnitDropdownChevron =
            selectBusinessUnitContainer.locator("button[aria-label='Show suggestions']:last-child");
    private final Locator selectBusinessUnitClearIcon =
            selectBusinessUnitContainer.locator("button[aria-label='Show suggestions']:first-child");

    private final CurrentPageT page;

    public SelectBusinessUnitComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.page = currentPage;
    }

    public Locator getBusinessUnitInDropdownOption(String businessUnitName) {
        return dropdownOptionList.filter(new Locator.FilterOptions().setHas(textExact(businessUnitName)));
    }

    @Step("Select '{businessUnitName}' business unit using filter")
    public CurrentPageT selectBusinessUnit(String businessUnitName) {
        selectBusinessUnitField.waitFor();
        getPage().waitForTimeout(1500);

        String lastName = "";

        selectBusinessUnitField.pressSequentially(businessUnitName,
                new Locator.PressSequentiallyOptions().setDelay(100));

        if (dropdownOptionList.all().isEmpty()) {
            throw new NoSuchElementException("Business unit '" + businessUnitName + "' not found in dropdown.");
        }

        while (getBusinessUnitInDropdownOption(businessUnitName).all().isEmpty()) {
            if (dropdownOptionList.last().innerText().equals(lastName)) {
                throw new NoSuchElementException("Business unit '" + businessUnitName + "' not found in dropdown.");
            }
            dropdownOptionList.last().scrollIntoViewIfNeeded();

            lastName = dropdownOptionList.last().innerText();
        }

        getBusinessUnitInDropdownOption(businessUnitName).first().click();

        return page;
    }

    @Step("Click select Business unit clear icon")
    public CurrentPageT clickSelectBusinessUnitClearIcon() {
        selectBusinessUnitClearIcon.dispatchEvent("click");

        return page;
    }

    @Step("Click Business unit dropdown toggle arrow '˅˄'")
    public CurrentPageT clickSelectBusinessUnitDropdownChevron() {
        selectBusinessUnitDropdownChevron.click();

        return page;
    }

    @Step("Click 'Select Business unit' placeholder")
    public CurrentPageT clickSelectBusinessUnitPlaceholder() {
        selectBusinessUnitPlaceholder.click();

        return page;
    }
}
