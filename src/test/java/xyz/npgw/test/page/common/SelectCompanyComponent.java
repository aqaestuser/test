package xyz.npgw.test.page.common;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.NoSuchElementException;

@Log4j2
public class SelectCompanyComponent<CurrentPageT> extends SelectComponent<CurrentPageT> {

    @Getter
    private final Locator selectCompanyField = getByLabelExact("Select company")
            .or(getByLabelExact("Company"));
    private final Locator dropdownOptionList = getByRole(AriaRole.OPTION);
    @Getter
    private final Locator companyDropdown = locator("div[data-slot='content']");
    private final Locator selectCompanyContainer = locator("div[data-slot='input-wrapper']")
            .filter(new Locator.FilterOptions().setHas(selectCompanyField));
    private final Locator selectCompanyDropdownChevron = selectCompanyContainer
            .locator("button[aria-label='Show suggestions']:last-child");
    private final Locator selectCompanyClearIcon = selectCompanyContainer
            .locator("button[aria-label='Show suggestions']:first-child");

    public SelectCompanyComponent(Page page, CurrentPageT currentPage) {
        super(page, currentPage);
    }

    @Step("Select '{companyName}' company using filter")
    public CurrentPageT selectCompany(String companyName) {
        select(selectCompanyField, companyName);

        return currentPage;
    }

    @Step("Click select Company clear icon")
    public CurrentPageT clickSelectCompanyClearIcon() {
        selectCompanyClearIcon.dispatchEvent("click");

        return currentPage;
    }

    @Step("Click company dropdown toggle arrow '˅˄'")
    public CurrentPageT clickSelectCompanyDropdownChevron() {
        selectCompanyDropdownChevron.click();

        return currentPage;
    }

    @Step("Select first company in dropdown")
    public CurrentPageT selectFirstCompany() {
        if (dropdownOptionList.all().isEmpty()) {
            throw new NoSuchElementException("Dropdown list is empty.");
        } else {
            dropdownOptionList.first().click();
        }

        return currentPage;
    }

    public String firstCompanyName() {

        return dropdownOptionList.first().textContent();
    }

    public boolean isCompanyPresent(String companyName) {
        return getAllOptions(selectCompanyField, companyName).contains(companyName);
    }
}
