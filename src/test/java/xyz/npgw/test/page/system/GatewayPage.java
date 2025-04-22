package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import org.testng.Assert;

public class GatewayPage extends BaseSystemPage<GatewayPage> {

    @Getter
    private final Locator businessUnitsListHeader = textExact("Business units list");
    @Getter
    private final Locator businessUnitsList = locator("div[data-slot='base'] li");
    @Getter
    private final Locator selectCompanyPlaceholder = locator("input[aria-label='Select company']");
    private final Locator selectCompanyContainer =
            locator("div[data-slot='input-wrapper']")
                    .filter(new Locator.FilterOptions().setHas(selectCompanyPlaceholder));
    private final Locator selectCompanyDropdownChevron =
            selectCompanyContainer.locator("button[aria-label='Show suggestions']:last-child");

    private final Locator selectCompanyClearIcon =
            selectCompanyContainer.locator("button[aria-label='Show suggestions']:first-child");
    @Getter
    private final Locator companyDropdown = locator("div[data-slot='content']");
    private final Locator companyDropdownOptions = companyDropdown.locator("li");
    @Getter
    private final Locator currencyValue = locator("div[data-slot='innerWrapper'] span");
    private final Locator currencyDropdown = locator("div[data-slot='listbox']");
    @Getter
    private final Locator currencyOptions = option(currencyDropdown);

    public GatewayPage(Page page) {
        super(page);
        businessUnitsListHeader
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
    }

    @Step("Click Currency value")
    public GatewayPage clickCurrencyValue() {
        currencyValue.click();

        return this;
    }

    @Step("Select Currency '{currency}'")
    public GatewayPage selectCurrency(String currency) {
        currencyOptions.filter(new Locator.FilterOptions().setHasText(currency)).click();
        currencyDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }

    @Step("Click 'Select company' placeholder")
    public GatewayPage clickSelectCompanyPlaceholder() {
        selectCompanyPlaceholder.click();

        return this;
    }

    @Step("Click company '{company}' in dropdown")
    public GatewayPage clickCompanyInDropdown(String company) {

        boolean found = false;
        String lastOptionBeforeScroll = "";

        while (!found) {
            String currentLastOptionText = companyDropdownOptions.last().textContent();

            if (lastOptionBeforeScroll.equals(currentLastOptionText) && !lastOptionBeforeScroll.isEmpty()) {
                Assert.fail("Company '" + company + "' not found in dropdown.");
                break;
            }

            lastOptionBeforeScroll = currentLastOptionText;

            Locator matchingOption = companyDropdownOptions.filter(new Locator.FilterOptions().setHasText(company));

            if (matchingOption.count() > 0 && matchingOption.first().isVisible()) {
                found = true;
                matchingOption.click();
            } else {
                companyDropdownOptions.last().scrollIntoViewIfNeeded();
                getPage().waitForTimeout(200);
            }
        }

        return this;
    }

    @Step("Click select Company clear icon")
    public GatewayPage clickSelectCompanyClearIcon() {
        selectCompanyClearIcon.dispatchEvent("click");

        return this;
    }

    @Step("Click company dropdown toggle arrow '˅˄'")
    public GatewayPage clickSelectCompanyDropdownChevron() {
        selectCompanyDropdownChevron.click();

        return this;
    }
}
