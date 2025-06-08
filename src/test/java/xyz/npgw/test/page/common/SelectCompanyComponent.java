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
public class SelectCompanyComponent<CurrentPageT> extends BaseComponent {

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
        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));

        String lastName = "";
        selectCompanyField.fill(companyName);

        if (locator("div[data-slot='empty-content']").isVisible()) {
            throw new NoSuchElementException("Company '" + companyName + "' not found. Dropdown list is empty.");
        }

        while (getCompanyInDropdown(companyName).all().isEmpty()) {
            if (dropdownOptionList.last().innerText().equals(lastName)) {
                throw new NoSuchElementException("Company '" + companyName + "' not found in dropdown list.");
            }
            dropdownOptionList.last().scrollIntoViewIfNeeded();
            lastName = dropdownOptionList.last().innerText();
        }
        getCompanyInDropdown(companyName).click();

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

    @Step("Click 'Select company' field")
    public CurrentPageT clickSelectCompanyField() {
        selectCompanyField.click();

        return page;
    }

    @Step("Select first company in dropdown")
    public CurrentPageT selectFirstCompany() {
        if (dropdownOptionList.all().isEmpty()) {
            throw new NoSuchElementException("Dropdown list is empty.");
        } else {
            dropdownOptionList.first().click();
        }

        return page;
    }

    public String firstCompanyName() {

        return  dropdownOptionList.first().textContent();
    }
}
