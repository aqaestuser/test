package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.BusinessUnitsTableTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.dialog.company.AddCompanyDialog;
import xyz.npgw.test.page.dialog.company.DeleteCompanyDialog;
import xyz.npgw.test.page.dialog.company.EditCompanyDialog;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;

import java.time.LocalTime;

@Getter
public class CompaniesAndBusinessUnitsPage extends BaseSystemPage<CompaniesAndBusinessUnitsPage> implements
        SelectCompanyTrait<CompaniesAndBusinessUnitsPage>,
        AlertTrait<CompaniesAndBusinessUnitsPage>,
        BusinessUnitsTableTrait {

    private final Locator addCompanyButton = locator("button[data-testid='AddCompanyButton']");
    private final Locator addBusinessUnitButton = getByTestId("ButtonAddMerchant");
    private final Locator editCompanyButton = getByTestId("EditCompanyButton");
    private final Locator addCompanyDialog = getByRole(AriaRole.DIALOG);
    private final Locator name = getByLabelExact("Name");
    private final Locator type = getByLabelExact("Type");
    private final Locator description = getByLabelExact("Description");
    private final Locator website = getByLabelExact("Website");
    private final Locator primaryContact = getByLabelExact("Primary contact");
    private final Locator email = getByLabelExact("Email");
    private final Locator phone = getByLabelExact("Phone");
    private final Locator mobile = getByLabelExact("Mobile");
    private final Locator fax = getByLabelExact("Fax");
    private final Locator country = getByLabelExact("Country");
    private final Locator state = getByLabelExact("State");
    private final Locator zip = getByLabelExact("ZIP");
    private final Locator city = getByLabelExact("City");
    private final Locator apiActive = getByLabelExact("API active");
    private final Locator portalActive = getByLabelExact("Portal active");
    private final Locator editBusinessUnitDialog = getByRole(AriaRole.DIALOG).getByTitle("Edit business unit");
    private final Locator merchantsTable = getByLabelExact("merchants table");
    private final Locator resetFilterButton = getByTestId("ResetButtonTeamPage");
    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator pageContent = locator("[class='contentBlock']");
    private final Locator settings = getByTestId("SettingsButtonMerchantsPage");
    private final Locator showRadiobutton = locator("[value='show']");
    private final Locator hideRadiobutton = locator("[value='hide']");
    private final Locator companyInfoBlock = locator("//div[text()='Company info']/..");
    private final Locator deleteSelectedCompany = getByTestId("DeleteCompanyButton");

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
        getPage().getByText("Loading").waitFor();
        getPage().getByText("Loading").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        editCompanyButton.click();

        return new EditCompanyDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public CompaniesAndBusinessUnitsPage clickOnResetFilterButton() {
        resetFilterButton.click();

        return this;
    }

    @Step("Click 'Refresh data' button")
    public CompaniesAndBusinessUnitsPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }

    @Step("Click 'Settings'")
    public CompaniesAndBusinessUnitsPage clickSettings() {
        getPage().waitForCondition(() -> LocalTime.now().isAfter(THREAD_LAST_ACTIVITY.get()));
        settings.click();

        return this;
    }

    @Step("Check 'Show' Company info option")
    public CompaniesAndBusinessUnitsPage checkShowCompanyInfo() {
        showRadiobutton.check();

        return this;
    }

    @Step("Check 'Hide' Company info option")
    public CompaniesAndBusinessUnitsPage checkHideCompanyInfo() {
        hideRadiobutton.check();

        return this;
    }

    @Step("Click 'Delete selected company' button")
    public DeleteCompanyDialog clickDeleteSelectedCompany() {
        deleteSelectedCompany.click();

        return new DeleteCompanyDialog(getPage());
    }
}
