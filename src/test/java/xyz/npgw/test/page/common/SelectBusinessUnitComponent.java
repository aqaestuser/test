package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.page.base.BaseComponent;

import java.time.LocalTime;
import java.util.NoSuchElementException;

@Log4j2
public class SelectBusinessUnitComponent<CurrentPageT> extends BaseComponent {

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

    private final CurrentPageT page;

    public SelectBusinessUnitComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.page = currentPage;
    }

    public Locator getBusinessUnitInDropdownOption(String businessUnitName) {
        return dropdownOptionList.filter(new Locator.FilterOptions().setHas(getByTextExact(businessUnitName)));
    }

    @Step("Select '{businessUnitName}' business unit using filter")
    public CurrentPageT selectBusinessUnit(String businessUnitName) {
//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        String lastName = "";
        selectBusinessUnitField.fill(businessUnitName);

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

//        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));
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
