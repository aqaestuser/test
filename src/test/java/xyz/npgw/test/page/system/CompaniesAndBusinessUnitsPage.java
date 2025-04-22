package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import org.testng.Assert;
import xyz.npgw.test.page.dialog.company.AddCompanyDialog;
import xyz.npgw.test.page.dialog.company.EditCompanyDialog;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;

public class CompaniesAndBusinessUnitsPage extends BaseSystemPage<CompaniesAndBusinessUnitsPage> {

    private final Locator addCompanyButton = locator("button[data-testid='AddCompanyButton']");
    @Getter
    private final Locator addBusinessUnitButton = getByTestId("ButtonAddMerchant");
    @Getter
    private final Locator editCompanyButton = getByTestId("EditCompanyButton");
    private final Locator companyDropdown = labelExact("Select company");
    @Getter
    private final Locator businessUnitEmptyList = locator("[role='gridcell']");
    private final Locator successAlert = alert("SUCCESS");
    @Getter
    private final Locator addCompanyDialog = dialog();
    private final Locator alertMessage = locator("[role='alert']");
    private final Locator companyNameDropdownList = locator("[role='option']");
    private final Locator selectCompanyDropdown = locator("[aria-label='Show suggestions']:nth-child(2)");
    private final Locator lastDropdownOption = locator("[role='option']:last-child");
    @Getter
    private final Locator selectCompanyInput = placeholder("Search...");
    @Getter
    private final Locator descriptionFromCompanyInfoSection = labelExact("Description");
    @Getter
    private final Locator websiteFromCompanyInfoSection = labelExact("Website");
    @Getter
    private final Locator primaryContactFromCompanyInfoSection = labelExact("Primary contact");
    @Getter
    private final Locator emailFromCompanyInfoSection = labelExact("Email");
    @Getter
    private final Locator phoneFromCompanyInfoSection = labelExact("Phone");
    @Getter
    private final Locator mobileFromCompanyInfoSection = labelExact("Mobile");
    @Getter
    private final Locator faxFromCompanyInfoSection = labelExact("Fax");
    @Getter
    private final Locator countryFromCompanyInfoSection = labelExact("Country");
    @Getter
    private final Locator stateFromCompanyInfoSection = labelExact("State");
    @Getter
    private final Locator zipFromCompanyInfoSection = labelExact("ZIP");
    @Getter
    private final Locator cityFromCompanyInfoSection = labelExact("City");
    @Getter
    private final Locator apiActiveCheckboxFromCompanyInfoSection = labelExact("API active");
    @Getter
    private final Locator portalActiveCheckboxFromCompanyInfoSection = labelExact("Portal active");
    @Getter
    private final Locator businessUnitNameData = locator("[role='row'] span").first();
    @Getter
    private final Locator merchantIdData = locator("[role='row'] span").nth(1);

    public CompaniesAndBusinessUnitsPage(Page page) {
        super(page);
    }

    protected Locator alert(String text) {
        return getPage().getByRole(AriaRole.ALERT, new Page.GetByRoleOptions().setName(text));
    }

    @Step("Click 'Add company' button")
    public AddCompanyDialog clickAddCompanyButton() {
        addCompanyButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
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

    public Locator getAlertMessage() {
        alertMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return alertMessage;
    }

    @Step("Click 'Select company' dropdown")
    public CompaniesAndBusinessUnitsPage clickSelectCompanyDropdown() {
        getPage().waitForTimeout(1000);
        selectCompanyDropdown.click();

        return this;
    }

    @Step("Click 'Add business unit' button (+)")
    public AddBusinessUnitDialog clickOnAddBusinessUnitButton() {
        addBusinessUnitButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        getPage().waitForCondition(addBusinessUnitButton::isEnabled);

        addBusinessUnitButton.click();

        return new AddBusinessUnitDialog(getPage());
    }

    @Step("Click '{companyName}' company in dropdown")
    public CompaniesAndBusinessUnitsPage clickCompanyInDropdown(String companyName) {
        String lastSeenText = "";

        while (true) {
            Locator options = companyNameDropdownList;
            int count = options.count();

            for (int i = 0; i < count; i++) {
                String text = options.nth(i).innerText().trim();
                if (text.equals(companyName)) {
                    options.nth(i).click();
                    return this;
                }
            }

            String currentLastText = options.nth(count - 1).innerText().trim();
            if (currentLastText.equals(lastSeenText)) {
                break;
            }

            lastSeenText = currentLastText;

            lastDropdownOption.scrollIntoViewIfNeeded();
        }

        Assert.fail("Company '" + companyName + "' not found in dropdown.");

        return this;
    }

    @Step("Click 'Edit company' button")
    public EditCompanyDialog clickEditCompanyButton() {
        //getPage().waitForLoadState(LoadState.NETWORKIDLE);
        getPage().getByText("Loading").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        getPage().getByText("Loading").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        editCompanyButton.click();

        return new EditCompanyDialog(getPage());
    }
}
