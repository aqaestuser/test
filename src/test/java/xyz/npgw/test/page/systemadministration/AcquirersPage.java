package xyz.npgw.test.page.systemadministration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.SystemAdministrationBasePage;

public class AcquirersPage extends SystemAdministrationBasePage {

    @Getter
    private final Locator addAcquirerButton = locator("svg[data-icon='circle-plus']");
    @Getter
    private final Locator resetFilterButton = locator("svg[data-icon='xmark']");
    @Getter
    private final Locator applyFilterButton = locator("svg[data-icon='filter']");

    @Getter
    private final Locator acquirersListHeader = textExact("Acquirers list");
    private final Locator acquirersList = locator("div[data-slot='base'] li");

    @Getter
    private final Locator selectAcquirerLabel = labelExact("Select acquirer");
    private final Locator selectAcquirerPlaceholder = placeholder("Search");
    private final Locator dropdownAcquirerList = locator("div[data-slot='content'] li");

    @Getter
    private final Locator statusLabel = labelExact("Status");
    private final Locator acquirerStatusPlaceholder = locator("div[data-slot='innerWrapper'] span");
    @Getter
    private final Locator dropdownAcquirerStatusList = locator("div[data-slot='listbox']");
    @Getter
    private final Locator acquirerStatusOptions = option(dropdownAcquirerStatusList);

    public AcquirersPage(Page page) {
        super(page);
    }

    public Locator getAcquirersList() {
        getPage().waitForTimeout(1000);

        return acquirersList;
    }

    @Step("Click 'Select acquirer' placeholder")
    public AcquirersPage clickSelectAcquirerPlaceholder() {
        selectAcquirerLabel.waitFor();
        selectAcquirerPlaceholder.click();

        return this;
    }

    public Locator getSelectAcquirersDropdownItems() {
        dropdownAcquirerList.last().waitFor();

        return dropdownAcquirerList;
    }

    @Step("Click Status placeholder")
    public AcquirersPage clickAcquirerStatusPlaceholder() {
        selectAcquirerLabel.waitFor();
        acquirerStatusPlaceholder.click();

        return this;
    }

}
