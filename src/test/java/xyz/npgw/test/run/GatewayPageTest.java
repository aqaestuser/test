package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import net.datafaker.Faker;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.GatewayPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GatewayPageTest extends BaseTest {

    private static final String COMPANY_NAME = "%s company 112172".formatted(RUN_ID);
    private final String[] expectedBusinessUnitsList = new String[]{"Merchant 1 for C112172", "Merchant 2 for C112172"};
    private final String[] expectedOptions = new String[]{"ALL", "EUR", "USD", "GBP"};
    Company company = new Company("%s company for 602".formatted(RUN_ID));
    String merchantTitle = new Faker().company().industry();

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.createBusinessUnits(getApiRequestContext(), COMPANY_NAME, expectedBusinessUnitsList);
    }

    @Test
    @TmsLink("283")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("The 'Currency' dropdown toggles and contains options")
    public void testOpenCurrencyDropdown() {
        GatewayPage gatewayPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickGatewayTab()
                .clickCurrencyValue();

        Allure.step("Verify: The 'Currency' dropdown toggles and contains options");
        assertThat(gatewayPage.getCurrencyOptions()).hasText(expectedOptions);
    }

    @Test
    @TmsLink("285")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("Verify that re-selecting an already selected currency keeps the selection unchanged.")
    public void testRetainCurrencyWhenReSelectingSameOption() {
        GatewayPage gatewayPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickGatewayTab();

        Locator actualCurrency = gatewayPage
                .getCurrencyValue();

        for (String currency : expectedOptions) {
            gatewayPage
                    .clickCurrencyValue()
                    .selectCurrency(currency);

            Allure.step("Verify currency has value: " + currency);
            assertThat(actualCurrency).hasText(currency);

            gatewayPage
                    .clickCurrencyValue()
                    .selectCurrency(currency);

            Allure.step("Verify currency has the same value: " + currency);
            assertThat(actualCurrency).hasText(currency);
        }
    }

    @Test
    @TmsLink("307")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("Check that selecting a company populates the 'Business units list',"
            + " and when no company is selected, the list is empty with 'No items.'")
    public void testBusinessUnitsListUpdatesOnCompanySelection() {
        GatewayPage gatewayPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickGatewayTab()
                .getSelectCompany().clickSelectCompanyField()
                .getSelectCompany().selectCompany(COMPANY_NAME);

        Locator selectCompanyField = gatewayPage
                .getSelectCompany().getSelectCompanyField();

        Allure.step("Verify: The dropdown is closed.");
        assertThat(gatewayPage.getSelectCompany().getCompanyDropdown()).not().isVisible();

        Allure.step("Verify: Placeholder value");
        assertThat(selectCompanyField).hasValue(COMPANY_NAME);

        Allure.step("Verify: 'Business units list' title is visible");
        assertThat(gatewayPage.getBusinessUnitsListHeader()).isVisible();

        Allure.step("Verify: Business units list length");
        assertThat(gatewayPage.getBusinessUnitsList()).hasCount(expectedBusinessUnitsList.length);

        Allure.step("Verify: Expected list");
        assertThat(gatewayPage.getBusinessUnitsList()).hasText(expectedBusinessUnitsList);

        gatewayPage
                .getSelectCompany().clickSelectCompanyClearIcon()
                .getSelectCompany().clickSelectCompanyDropdownChevron();

        Allure.step("Verify: Placeholder has value 'Search...'");
        assertThat(selectCompanyField).hasAttribute("placeholder", "Search...");

        Allure.step("Verify: Field is empty");
        assertThat(selectCompanyField).isEmpty();

        Allure.step("Verify: 'Business units list' title is still visible");
        assertThat(gatewayPage.getBusinessUnitsListHeader()).isVisible();

        Allure.step("Verify: 'Business units list' has 'No items.'");
        assertThat(gatewayPage.getBusinessUnitsList()).hasText(new String[]{"No items."});
    }

    @Test
    @TmsLink("602")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("Verify that if company is selected all it's business units are presented in the list")
    public void testCompaniesBusinessUnitsPresence() {
        GatewayPage gatewayPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton()
                .fillBusinessUnitNameField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickOnAddBusinessUnitButton()
                .fillBusinessUnitNameField(merchantTitle)
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getSystemMenu().clickGatewayTab()
                .getSelectCompany().clickSelectCompanyField()
                .getSelectCompany().selectCompany(company.companyName());

        Allure.step("Verify that all the Business units are presented in the list");
        assertThat(gatewayPage.getBusinessUnitsBlock()).containsText(company.companyType());
        assertThat(gatewayPage.getBusinessUnitsBlock()).containsText(merchantTitle);
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
        super.afterClass();
    }
}
