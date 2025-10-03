package xyz.npgw.test.page.system;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import xyz.npgw.test.common.ProjectProperties;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectCompanyTrait;
import xyz.npgw.test.page.component.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.component.table.SuperBusinessUnitsTableTrait;
import xyz.npgw.test.page.dialog.company.AddCompanyDialog;
import xyz.npgw.test.page.dialog.company.DeleteCompanyDialog;
import xyz.npgw.test.page.dialog.company.EditCompanyDialog;
import xyz.npgw.test.page.dialog.merchant.AddBusinessUnitDialog;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Log4j2
@Getter
public class SuperCompaniesAndBusinessUnitsPage extends BaseBusinessUnitsPage<SuperCompaniesAndBusinessUnitsPage>
        implements SuperHeaderMenuTrait<SuperCompaniesAndBusinessUnitsPage>,
        SuperSystemMenuTrait,
        SelectCompanyTrait<SuperCompaniesAndBusinessUnitsPage>,
        SuperBusinessUnitsTableTrait {

    private final Locator addCompanyButton = getByTestId("AddCompanyButton");
    private final Locator editCompanyButton = getByTestId("EditCompanyButton");
    private final Locator deleteSelectedCompany = getByTestId("DeleteCompanyButton");
    private final Locator addBusinessUnitButton = getByTestId("ButtonAddMerchant");
    private final Locator resetFilterButton = getByTestId("ResetButtonTeamPage");
    private final Locator refreshDataButton = locator("[data-icon='arrows-rotate']");
    private final Locator initialCommonIcon = getByRole(AriaRole.TABPANEL).locator(">div>div:first-child [data-testid]:enabled");
    private final Locator commonIconButton = locator("[role='tabpanel']>div>div:first-child [data-testid]");

    public SuperCompaniesAndBusinessUnitsPage(Page page) {
        super(page);
    }

    @Step("Click 'Add company' button")
    public AddCompanyDialog clickAddCompanyButton() {
        addCompanyButton.click();

        return new AddCompanyDialog(getPage());
    }

    @Step("Click 'Add business unit' button (+)")
    public AddBusinessUnitDialog clickOnAddBusinessUnitButton() {
        addBusinessUnitButton.click();

        return new AddBusinessUnitDialog(getPage());
    }

    @Step("Click 'Edit company' button")
    public EditCompanyDialog clickEditCompanyButton() {
        editCompanyButton.click();

        return new EditCompanyDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public SuperCompaniesAndBusinessUnitsPage clickOnResetFilterButton() {
        resetFilterButton.click();

        return this;
    }

    @Step("Click 'Refresh data' button")
    public SuperCompaniesAndBusinessUnitsPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }

    @Step("Click 'Delete selected company' button")
    public DeleteCompanyDialog clickDeleteSelectedCompany() {
        deleteSelectedCompany.click();

        return new DeleteCompanyDialog(getPage());
    }

    @SneakyThrows
    public SuperCompaniesAndBusinessUnitsPage waitForCompanyAbsence(APIRequestContext request, String companyName) {
        double timeout = ProjectProperties.getDefaultTimeout();
        while (Arrays.stream(Company.getAll(request)).anyMatch(item -> item.companyName().equals(companyName))) {
            TimeUnit.MILLISECONDS.sleep(300);
            timeout -= 300;
            if (timeout <= 0) {
                throw new TimeoutError("Waiting for company '%s' absence".formatted(companyName));
            }
        }
        double waitTime = ProjectProperties.getDefaultTimeout() - timeout;
        if (waitTime > 0) {
            log.info("Company absence wait took {}ms", waitTime);
        }

        return this;
    }

//    private final Locator initialCommonIconButton = getByRole(AriaRole.TABPANEL).locator(">div>div:first-child [data-testid]:enabled");
//    public SuperCompaniesAndBusinessUnitsPage getInitialCommonIconName() {
//       initialCommonIconButton.locator("");
//
//        return this;
//    }

    public String  getIconName(Locator icon) {
        return icon.locator("svg").getAttribute("data-icon");
    }
}
