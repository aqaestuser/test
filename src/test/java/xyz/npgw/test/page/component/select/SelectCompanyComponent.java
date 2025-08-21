package xyz.npgw.test.page.component.select;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;

public class SelectCompanyComponent<CurrentPageT> extends SelectComponent<CurrentPageT> {

    @Getter
    private final Locator selectCompanyField = getByLabelExact("Select company")
            .or(getByLabelExact("Company"));
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

    public boolean isCompanyPresent(String companyName) {
        return getAllOptions(selectCompanyField, companyName).contains(companyName);
    }
}
