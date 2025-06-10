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

import java.util.List;
import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GatewayPageTest extends BaseTest {

    private static final String COMPANY_NAME = "%s company 112172".formatted(RUN_ID);
    private final String[] expectedBusinessUnitsList = new String[]{"Merchant 1 for C112172", "Merchant 2 for C112172"};
    private final String[] expectedOptions = new String[]{"ALL", "EUR", "USD", "GBP"};
    Company company = new Company("%s company for 602".formatted(RUN_ID), "first");
    String merchantTitle = "second";

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
        assertThat(gatewayPage.getSelectCompany().getCompanyDropdown()).isHidden();

        Allure.step("Verify: Company is selected");
        assertThat(selectCompanyField).hasValue(COMPANY_NAME);

        gatewayPage
                .getSelectCompany().clickSelectCompanyClearIcon()
                .getSelectCompany().clickSelectCompanyDropdownChevron();

        Allure.step("Verify: Placeholder has value 'Search...'");
        assertThat(selectCompanyField).hasAttribute("placeholder", "Search...");

        Allure.step("Verify: Field is empty");
        assertThat(selectCompanyField).isEmpty();
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
                .getSelectCompany().selectCompany(company.companyName())
                .getSelectBusinessUnit().clickSelectBusinessUnitPlaceholder();

        Allure.step("Verify that all the Business units are presented in the list");
        assertThat(gatewayPage.getSelectBusinessUnit().getDropdownOptionList())
                .hasText(new String[]{"first", "second"});
    }

    @Test
    @TmsLink("693")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("Verify Reset filter cleans all the filters applied")
    public void testResetAllTheFilters() {
        List<String> expectedCurrency = List.of("All", "EUR", "USD", "GBP");
        String selectedCurrency = expectedCurrency.get(new Random().nextInt(expectedCurrency.size() - 1) + 1);
        Company company = new Company(new Faker().company().name(), new Faker().company().industry());

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
                .getSystemMenu().clickGatewayTab()
                .getSelectCompany().clickSelectCompanyField()
                .getSelectCompany().selectCompany(company.companyName())
                .getSelectBusinessUnit().selectBusinessUnit(company.companyType())
                .clickCurrencyValue()
                .selectCurrency(selectedCurrency);

        Allure.step("Verify that all the values are presented in filter's filter");
        assertThat(gatewayPage.getCurrencyValue()).containsText(selectedCurrency);
        assertThat(gatewayPage.getBusinessUnitDropdownTrigger()).hasAttribute("value", company.companyType());
        assertThat(gatewayPage.getCompanyDropdownTrigger()).hasAttribute("value", company.companyName());

        gatewayPage
                .clickResetFilterButton();

        Allure.step("Verify that all the filter are cleaned");
        assertThat(gatewayPage.getCurrencyValue()).containsText("ALL");
        assertThat(gatewayPage.getBusinessUnitDropdownTrigger()).hasAttribute("placeholder", "Select business unit");
        assertThat(gatewayPage.getCompanyDropdownTrigger()).hasAttribute("placeholder", "Search...");
        assertThat(gatewayPage.getBusinessUnitDropdownTrigger()).isEmpty();
        assertThat(gatewayPage.getCompanyDropdownTrigger()).isEmpty();

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
        super.afterClass();
    }
}
