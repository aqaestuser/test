package xyz.npgw.test.page.systemadministration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.AddBusinessUnitDialog;
import xyz.npgw.test.page.AddCompanyDialog;
import xyz.npgw.test.page.base.SystemAdministrationWithTableBasePage;

public class CompaniesAndBusinessUnitsPage extends SystemAdministrationWithTableBasePage {

    private final Locator addCompanyButton = locator("svg[data-icon='circle-plus']").first();
    @Getter
    private final Locator addBusinessUnitButton = testId("ButtonAddMerchant");
    @Getter
    private final Locator editCompanyButton = testId("EditCompanyButton");
    private final Locator companyDropdown = labelExact("Select company");
    @Getter
    private final Locator businessUnitEmptyList = locator("[role='gridcell']");
    private final Locator successAlert = alert("SUCCESS");
    @Getter
    private final Locator addCompanyDialog = dialog();

    public CompaniesAndBusinessUnitsPage(Page page) {
        super(page);
    }

    protected Locator testId(String text) {
        return getPage().getByTestId(text);
    }

    protected Locator alert(String text) {
        return getPage().getByRole(AriaRole.ALERT, new Page.GetByRoleOptions().setName(text));
    }

    @Step("Click 'Add company' button")
    public AddCompanyDialog clickAddCompanyButton() {
        addCompanyButton.click();

        return new AddCompanyDialog(getPage());
    }

    public CompaniesAndBusinessUnitsPage waitUntilAlertIsGone() {
        successAlert.waitFor();
        successAlert.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return this;
    }

    @Step("Select a company into 'Select company' filter field")
    public CompaniesAndBusinessUnitsPage selectCompanyInTheFilter(String name) {
        companyDropdown.click();
        companyDropdown.fill(name);
        getPage().locator("li[role='option']:has-text('%s')".formatted(name)).first().click();

        return this;
    }

    public AddBusinessUnitDialog clickOnAddBusinessUnitButton() {
        addBusinessUnitButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        getPage().waitForCondition(addBusinessUnitButton::isEnabled);

        addBusinessUnitButton.click();

        return new AddBusinessUnitDialog(getPage());
    }
}
