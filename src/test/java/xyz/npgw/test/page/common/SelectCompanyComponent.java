package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BaseComponent;

import java.util.NoSuchElementException;

public class SelectCompanyComponent<CurrentPageT> extends BaseComponent {

    @Getter
    private final Locator selectCompanyField = getByLabelExact("Select company")
            .or(getByLabelExact("Company"));
    private final Locator dropdownOptionList = getByRole(AriaRole.OPTION);
    @Getter
    private final Locator selectCompanyPlaceholder = locator("input[aria-label='Select company']");
    @Getter
    private final Locator companyDropdown = locator("div[data-slot='content']");
    private final Locator selectCompanyContainer =
            locator("div[data-slot='input-wrapper']")
                    .filter(new Locator.FilterOptions().setHas(selectCompanyPlaceholder));
    private final Locator selectCompanyDropdownChevron =
            selectCompanyContainer.locator("button[aria-label='Show suggestions']:last-child");

    private final Locator selectCompanyClearIcon =
            selectCompanyContainer.locator("button[aria-label='Show suggestions']:first-child");

    private final CurrentPageT page;

    public SelectCompanyComponent(Page page, CurrentPageT currentPage) {
        super(page);
        this.page = currentPage;
    }

    public Locator getCompanyInDropdown(String companyName) {
        return dropdownOptionList.filter(new Locator.FilterOptions().setHas(getByTextExact(companyName)));
    }

    @Step("Select '{companyName}' company using filter")
    public CurrentPageT selectCompany(String companyName) {
        selectCompanyField.waitFor();
        getPage().waitForTimeout(1500);

        String lastName = "";

        selectCompanyField.pressSequentially(
                companyName,
                new Locator.PressSequentiallyOptions().setDelay(100));

        if (dropdownOptionList.all().isEmpty()) {
            throw new NoSuchElementException("Company '" + companyName + "' not found. Dropdown list is empty.");
        }

        while (getCompanyInDropdown(companyName).all().isEmpty()) {
            if (dropdownOptionList.last().innerText().equals(lastName)) {
                throw new NoSuchElementException("Company '" + companyName + "' not found in dropdown list.");
            }
            dropdownOptionList.last().scrollIntoViewIfNeeded();
            lastName = dropdownOptionList.last().innerText();
        }
//        .first() - из-за того, что компания "super" отображается в отфильтрованном списке два раза,
//        это баг(!!), правильно - один раз (или ноль).
//        На суть теста .first() не влияет и позволяет "не заметить" баг.
//
        getCompanyInDropdown(companyName).first().click();

        return page;
    }

    @Step("Click select Company clear icon")
    public CurrentPageT clickSelectCompanyClearIcon() {
        selectCompanyClearIcon.dispatchEvent("click");

        return page;
    }

    @Step("Click company dropdown toggle arrow '˅˄'")
    public CurrentPageT clickSelectCompanyDropdownChevron() {
        selectCompanyDropdownChevron.click();

        return page;
    }

    @Step("Click 'Select company' placeholder")
    public CurrentPageT clickSelectCompanyPlaceholder() {
        selectCompanyPlaceholder.click();

        return page;
    }
}
