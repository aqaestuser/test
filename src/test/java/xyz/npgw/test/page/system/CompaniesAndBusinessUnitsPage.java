package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.AlertTrait;
import xyz.npgw.test.page.common.SelectCompanyTrait;
import xyz.npgw.test.page.dialog.company.AddCompanyDialog;
import xyz.npgw.test.page.dialog.company.EditCompanyDialog;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;

@Getter
public class CompaniesAndBusinessUnitsPage extends BaseSystemPage<CompaniesAndBusinessUnitsPage>
        implements SelectCompanyTrait<CompaniesAndBusinessUnitsPage>, AlertTrait<CompaniesAndBusinessUnitsPage> {

    private final Locator addCompanyButton = locator("button[data-testid='AddCompanyButton']");
    private final Locator addBusinessUnitButton = getByTestId("ButtonAddMerchant");
    private final Locator editCompanyButton = getByTestId("EditCompanyButton");
    private final Locator businessUnitEmptyList = locator("[role='gridcell']");
    private final Locator addCompanyDialog = getByRole(AriaRole.DIALOG);
    private final Locator companyNameDropdownList = locator("[role='option']");
    private final Locator selectCompanyInput = getByPlaceholder("Search...");
    private final Locator descriptionFromCompanyInfoSection = getByLabelExact("Description");
    private final Locator websiteFromCompanyInfoSection = getByLabelExact("Website");
    private final Locator primaryContactFromCompanyInfoSection = getByLabelExact("Primary contact");
    private final Locator emailFromCompanyInfoSection = getByLabelExact("Email");
    private final Locator phoneFromCompanyInfoSection = getByLabelExact("Phone");
    private final Locator mobileFromCompanyInfoSection = getByLabelExact("Mobile");
    private final Locator faxFromCompanyInfoSection = getByLabelExact("Fax");
    private final Locator countryFromCompanyInfoSection = getByLabelExact("Country");
    private final Locator stateFromCompanyInfoSection = getByLabelExact("State");
    private final Locator zipFromCompanyInfoSection = getByLabelExact("ZIP");
    private final Locator cityFromCompanyInfoSection = getByLabelExact("City");
    private final Locator apiActiveCheckboxFromCompanyInfoSection = getByLabelExact("API active");
    private final Locator portalActiveCheckboxFromCompanyInfoSection = getByLabelExact("Portal active");
    private final Locator editBusinessUnitDialog = getByRole(AriaRole.DIALOG).getByTitle("Edit business unit");
    private final Locator businessUnitNameData = locator("[role='row'] span").first();
    private final Locator merchantIdData = locator("[role='row'] span").nth(1);
    private final Locator editBusinessUnitButton = getByTestId("EditBusinessUnitButton");
    private final Locator merchantsTable = getByLabelExact("merchants table");
    private final Locator resetFilterButton = getByTestId("ResetButtonTeamPage");
    private final Locator pageContent = locator("[class='contentBlock']");

    public CompaniesAndBusinessUnitsPage(Page page) {
        super(page);
    }

    @Step("Click 'Add company' button")
    public AddCompanyDialog clickAddCompanyButton() {
        addCompanyButton.waitFor();
        addCompanyButton.click();

        return new AddCompanyDialog(getPage());
    }

    @Step("Click 'Add business unit' button (+)")
    public AddBusinessUnitDialog clickOnAddBusinessUnitButton() {
        addBusinessUnitButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        getPage().waitForCondition(addBusinessUnitButton::isEnabled);

        addBusinessUnitButton.click();

        return new AddBusinessUnitDialog(getPage());
    }

    @Step("Click 'Edit company' button")
    public EditCompanyDialog clickEditCompanyButton() {
        //getPage().waitForLoadState(LoadState.NETWORKIDLE);
        getPage().getByText("Loading").waitFor();
        getPage().getByText("Loading").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        editCompanyButton.click();

        return new EditCompanyDialog(getPage());
    }

    @Step("Click 'Edit Business Unit' button")
    public EditBusinessUnitDialog clickEditBusinessUnitButton() {
        editBusinessUnitButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        editBusinessUnitButton.click();

        return new EditBusinessUnitDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public CompaniesAndBusinessUnitsPage clickOnResetFilterButton() {
        resetFilterButton.click();

        return this;
    }
}
