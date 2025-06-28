package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import net.datafaker.Faker;
import org.testng.Assert;
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
import java.util.stream.IntStream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static xyz.npgw.test.run.AcquirersPageTest.ACQUIRER;

public class GatewayPageTest extends BaseTest {

    private static final String COMPANY_NAME = "%s company 112172".formatted(RUN_ID);
    private final String[] expectedBusinessUnitsList = new String[]{"Merchant 1 for C112172", "Merchant 2 for C112172",
            "MerchantAcquirer"};
    private final String[] expectedOptions = new String[]{"ALL", "EUR", "USD", "GBP"};
    private final Company company = new Company("%s company for 602".formatted(RUN_ID), "first");

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.createBusinessUnits(getApiRequestContext(), COMPANY_NAME, expectedBusinessUnitsList);
        TestUtils.createAcquirer(getApiRequestContext(), ACQUIRER);
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
        String merchantTitle = "second";

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
        assertThat(gatewayPage.getSelectBusinessUnit().getSelectBusinessUnitField()).hasValue(company.companyType());
        assertThat(gatewayPage.getSelectCompany().getSelectCompanyField()).hasValue(company.companyName());

        gatewayPage
                .clickResetFilterButton();

        Allure.step("Verify that all the filter are cleaned");
        assertThat(gatewayPage.getCurrencyValue()).containsText("ALL");
        assertThat(gatewayPage.getSelectBusinessUnit().getSelectBusinessUnitField())
                .hasAttribute("placeholder", "Select business unit");
        assertThat(gatewayPage.getSelectCompany().getSelectCompanyField())
                .hasAttribute("placeholder", "Search...");
        assertThat(gatewayPage.getSelectBusinessUnit().getSelectBusinessUnitField()).isEmpty();
        assertThat(gatewayPage.getSelectCompany().getSelectCompanyField()).isEmpty();

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
    }

    @Test
    @TmsLink("806")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("Check possibility to select an appropriate acquirer to merchant")
    public void testSelectAcquirer() {
        GatewayPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[2])
                .clickAddMerchantAcquirer()
                .getSelectAcquirer().selectAcquirer(ACQUIRER.acquirerName())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify the result of adding Acquirer within Gateway page table");
        assertThat(page.getMerchantValue()).hasText(expectedBusinessUnitsList[2]);
        assertThat(page.getAcquirerValue()).hasText(ACQUIRER.acquirerCode());
        assertThat(page.getAcquirerConfigValue()).hasText(ACQUIRER.acquirerConfig());
        assertThat(page.getAcquirerStatusValue()).hasText("Active");
        assertThat(page.getAcquirerCurrencyValue()).hasText("USD, EUR");
        assertThat(page.getAcquirerPriorityValue()).hasText("0");
    }

    @Test
    @TmsLink("763")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Verify the active and inactive merchant acquirers can be added")
    public void testAddMerchantAcquirer() {
        GatewayPage gatewayPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickGatewayTab()
                .getSelectCompany().clickSelectCompanyField()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[0])
                .clickAddMerchantAcquirerButton()
                .getSelectAcquirer().selectAcquirer(ACQUIRER.acquirerName())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickAddMerchantAcquirerButton()
                .selectInactiveStatus()
                .getSelectAcquirer().selectAcquirer(ACQUIRER.acquirerName())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        List<String> actualNames = gatewayPage.getTable().getColumnValues("Business unit");
        List<String> actualStatuses = gatewayPage.getTable().getColumnValues("Status");

        boolean foundActive = IntStream.range(0, actualNames.size())
                .anyMatch(i -> actualNames.get(i).equals("Merchant 1 for C112172")
                        && actualStatuses.get(i).equals("Active"));

        boolean foundInactive = IntStream.range(0, actualNames.size())
                .anyMatch(i -> actualNames.get(i).equals("Merchant 1 for C112172")
                        && actualStatuses.get(i).equals("Inactive"));

        Allure.step("Verify that new Merchant acquirer is displayed and has Active status");
        Assert.assertTrue(foundActive,
                "New Merchant acquirer 'Merchant 1 for C112172' with status 'Active' was not found.");

        Allure.step("Verify that new Merchant acquirer is displayed and has Inactive status");
        Assert.assertTrue(foundInactive,
                "New Merchant acquirer 'Merchant 1 for C112172' with status 'Inactive' was not found.");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER.acquirerName());
        super.afterClass();
    }
}
