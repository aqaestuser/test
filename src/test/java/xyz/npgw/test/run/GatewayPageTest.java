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
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.Company;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.system.GatewayPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class GatewayPageTest extends BaseTest {

    private static final Acquirer ACQUIRER = Acquirer.builder()
            .acquirerDisplayName("acquirer for gateway")
            .acquirerName("%s acquirer for gateway".formatted(RUN_ID))
            .acquirerMidMcc("4321")
            .build();
    private static final Acquirer ACQUIRER_MOVE = Acquirer.builder()
            .acquirerDisplayName("acquirer for gateway Movable")
            .acquirerName("%s acquirer for gateway Movable".formatted(RUN_ID))
            .acquirerMidMcc("4321")
            .build();
    private static final Acquirer ACQUIRER_EUR = Acquirer.builder()
            .acquirerDisplayName("acquirer for gateway EUR")
            .acquirerName("%s acquirer for gateway EUR".formatted(RUN_ID))
            .currencyList(new Currency[]{Currency.EUR})
            .acquirerMidMcc("4321")
            .build();
    private static final Acquirer ACQUIRER_GBP = Acquirer.builder()
            .acquirerDisplayName("acquirer for gateway GBP")
            .acquirerName("%s acquirer for gateway GBP".formatted(RUN_ID))
            .currencyList(new Currency[]{Currency.GBP})
            .acquirerMidMcc("4321")
            .build();
    private static final String COMPANY_NAME = "%s company 112172".formatted(RUN_ID);
    private static final String COMPANY_NAME_DELETION_TEST = "%s company 112173".formatted(RUN_ID);
    private static final String BUSINESS_UNIT_NAME_DELETION_TEST = "BU-1";
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
        TestUtils.createAcquirer(getApiRequestContext(), ACQUIRER_MOVE);
        TestUtils.createAcquirer(getApiRequestContext(), ACQUIRER_EUR);
        TestUtils.createAcquirer(getApiRequestContext(), ACQUIRER_GBP);
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME_DELETION_TEST);
        TestUtils.createBusinessUnit(
                getApiRequestContext(), COMPANY_NAME_DELETION_TEST, BUSINESS_UNIT_NAME_DELETION_TEST);
    }

    @Test
    @TmsLink("283")
    @Epic("System/Gateway")
    @Feature("Currency")
    @Description("The 'Currency' dropdown toggles and contains options")
    public void testOpenCurrencyDropdown() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
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
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab();

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
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
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

        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
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
                .clickGatewayTab()
                .getSelectCompany().selectCompany(company.companyName())
                .getSelectBusinessUnit().clickSelectBusinessUnitPlaceholder();

        Allure.step("Verify that all the Business units are presented in the list");
        assertThat(gatewayPage.getSelectBusinessUnit().getDropdownOptionList())
                .hasText(new String[]{"first", "second"});

        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
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

        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickCompaniesAndBusinessUnitsTab()
                .clickAddCompanyButton()
                .fillCompanyNameField(company.companyName())
                .fillCompanyTypeField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                //Behaviour is changed now
//                .getSelectCompany().selectCompany(company.companyName())
                .clickOnAddBusinessUnitButton()
                .fillBusinessUnitNameField(company.companyType())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickGatewayTab()
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
    @Feature("Merchant acquirer")
    @Description("Check possibility to select an appropriate acquirer to merchant")
    public void testSelectAcquirer() {
        GatewayPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[2])
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton();

        Allure.step("Verify the result of adding Acquirer within Gateway page table");
        assertThat(page.getTable().getCell(0, "Business unit")).hasText(expectedBusinessUnitsList[2]);
        assertThat(page.getTable().getCell(0, "Acquirer code")).hasText(ACQUIRER.getAcquirerCode());
        assertThat(page.getTable().getCell(0, "Acquirer config")).hasText(ACQUIRER.getAcquirerConfig());
        assertThat(page.getTable().getCell(0, "Status")).hasText("Active");
        assertThat(page.getTable().getCell(0, "Currencies")).hasText("USD, EUR");
        assertThat(page.getTable().getCell(0, "Priority")).hasText("0");

        page.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("835/847")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Move merchant-acquirer down to reduce their priority,"
            + " Move merchant-acquirer up to increase their priority")
    public void testMoveMerchantAcquirerDownAndUpButtons() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[2])
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton()
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER_MOVE.getAcquirerName())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Check that the first created acquirer priority is 0");
        assertThat(gatewayPage.getTable().getCell(0, "Acquirer MID")).hasText(ACQUIRER.getAcquirerDisplayName());
        assertThat(gatewayPage.getTable().getCell(1, "Acquirer MID")).hasText(ACQUIRER_MOVE.getAcquirerDisplayName());

        gatewayPage.getTable().clickMoveBusinessUnitAcquirerDownButton("0");

        Allure.step("Check that the second created acquirer priority is 0 now");
        assertThat(gatewayPage.getTable().getCell(0, "Acquirer MID")).hasText(ACQUIRER_MOVE.getAcquirerDisplayName());
        assertThat(gatewayPage.getTable().getCell(1, "Acquirer MID")).hasText(ACQUIRER.getAcquirerDisplayName());

        gatewayPage.getTable().clickMoveBusinessUnitAcquirerUpButton("1");

        Allure.step("Check that the first created acquirer priority is 0 again");
        assertThat(gatewayPage.getTable().getCell(0, "Acquirer MID")).hasText(ACQUIRER.getAcquirerDisplayName());
        assertThat(gatewayPage.getTable().getCell(1, "Acquirer MID")).hasText(ACQUIRER_MOVE.getAcquirerDisplayName());

        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("763")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Verify the active and inactive merchant acquirers can be added")
    public void testAddMerchantAcquirer() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[1])
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton()
                .clickAddBusinessUnitAcquirerButton()
                .checkInactiveRadiobutton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER_MOVE.getAcquirerName())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify that new Merchant acquirer is displayed and has Active status");
        assertThat(gatewayPage.getTable().getCell(0, "Status")).hasText("Active");

        Allure.step("Verify that new Merchant acquirer is displayed and has Inactive status");
        assertThat(gatewayPage.getTable().getCell(1, "Status")).hasText("Inactive");

        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("763")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Merchant acquirer can be successfully deleted")
    public void testDeleteMerchantAcquirer() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME_DELETION_TEST)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME_DELETION_TEST)
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone() //TODO remove alert closing BUG workaround
                .getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();

        Allure.step("Verify: Success deletion alert message is shown");
        assertThat(gatewayPage.getAlert().getMessage())
                .hasText("SUCCESSAcquirer MID was deleted successfully");
    }

    @Test
    @TmsLink("845")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Verify that merchant acquirer can be activated and deactivated by Super")
    public void testActivateDeactivateMerchantAcquirer() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[0])
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton()
                .getTable().clickDeactivateBusinessUnitAcquirerButton("0")
                .clickDeactivateButton();

        Allure.step("Verify that acquirer status is 'Inactive' ");
        assertThat(gatewayPage.getTable().getFirstRowCell("Status")).hasText("Inactive");

        gatewayPage
                .getTable().clickActivateBusinessUnitAcquirerButton("0")
                .clickActivateButton();

        Allure.step("Verify that acquirer status is 'Active' ");
        assertThat(gatewayPage.getTable().getFirstRowCell("Status")).hasText("Active");

        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("864")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Verify that entries can be sorted by Priority, Status, Acquirer and Currencies in Asc and Desc order")
    public void testSortEntries() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[0])
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton()
                .clickAddBusinessUnitAcquirerButton()
                .checkInactiveRadiobutton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER_EUR.getAcquirerName())
                .clickCreateButton()
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER_GBP.getAcquirerName())
                .clickCreateButton()
                .getTable().clickColumnHeader("Status");

        List<String> actualStatusList = gatewayPage.getTable().getColumnValues("Status");
        List<String> sortedStatusListDesc = new ArrayList<>(actualStatusList);
        sortedStatusListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Status in Desc order ");
        Assert.assertEquals(actualStatusList, sortedStatusListDesc);

        gatewayPage
                .getTable().clickColumnHeader("Status");
        actualStatusList = gatewayPage.getTable().getColumnValues("Status");
        List<String> sortedStatusListAsc = new ArrayList<>(actualStatusList);
        Collections.sort(sortedStatusListAsc);

        Allure.step("Verify that entries are sorted by Status in Asc order ");
        Assert.assertEquals(actualStatusList, sortedStatusListAsc);

        gatewayPage
                .getTable().clickColumnHeader("Priority");
        List<String> actualPriorityList = gatewayPage.getTable().getColumnValues("Priority");
        List<String> sortedPriorityListAsc = new ArrayList<>(actualPriorityList);
        Collections.sort(sortedPriorityListAsc);

        Allure.step("Verify that entries are sorted by Priority in Asc order");
        Assert.assertEquals(actualPriorityList, sortedPriorityListAsc);

        gatewayPage
                .getTable().clickColumnHeader("Priority");
        actualPriorityList = gatewayPage.getTable().getColumnValues("Priority");
        List<String> sortedPriorityListDesc = new ArrayList<>(actualPriorityList);
        sortedPriorityListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Priority in Desc order");
        Assert.assertEquals(actualPriorityList, sortedPriorityListDesc);

        gatewayPage
                .getTable().clickColumnHeader("Acquirer MID");
        List<String> actualAcquirerList = gatewayPage.getTable().getColumnValues("Acquirer MID");
        List<String> sortedAcquirerListAsc = new ArrayList<>(actualAcquirerList);
        Collections.sort(sortedPriorityListAsc);

        Allure.step("Verify that entries are sorted by Acquirer in Asc order");
        Assert.assertEquals(actualAcquirerList, sortedAcquirerListAsc);

        gatewayPage
                .getTable().clickColumnHeader("Acquirer MID");
        actualAcquirerList = gatewayPage.getTable().getColumnValues("Acquirer MID");
        List<String> sortedAcquirerListDesc = new ArrayList<>(actualAcquirerList);
        sortedAcquirerListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Acquirer in Desc order");
        Assert.assertEquals(actualAcquirerList, sortedAcquirerListDesc);

        gatewayPage
                .getTable().clickColumnHeader("Currencies");
        List<String> actualCurrenciesList = gatewayPage.getTable().getColumnValues("Currencies");
        List<String> sortedCurrenciesListAsc = new ArrayList<>(actualCurrenciesList);
        Collections.sort(sortedCurrenciesListAsc);

        Allure.step("Verify that entries are sorted by Currencies in Asc order");
        Assert.assertEquals(actualCurrenciesList, sortedCurrenciesListAsc);

        gatewayPage
                .getTable().clickColumnHeader("Currencies");
        actualCurrenciesList = gatewayPage.getTable().getColumnValues("Currencies");
        List<String> sortedCurrenciesListDesc = new ArrayList<>(actualCurrenciesList);
        sortedCurrenciesListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Currencies in Desc order");
        Assert.assertEquals(actualCurrenciesList, sortedCurrenciesListDesc);

        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("857")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Verify that click on Cancel button cancels the Merchant Acquirer status change")
    public void testCancelButtonInChangeMerchantAcquirerActivityDialog() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[0])
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton()
                .getTable().clickDeactivateBusinessUnitAcquirerButton("0")
                .clickCancelButton();

        Allure.step("Verify that acquirer status is still 'Active'");
        assertThat(gatewayPage.getTable().getFirstRowCell("Status")).hasText("Active");

        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("858")
    @Epic("System/Gateway")
    @Feature("Merchant acquirer")
    @Description("Verify that click on Cancel button cancels the Merchant Acquirer status change")
    public void testCloseButtonInChangeMerchantAcquirerActivityDialog() {
        GatewayPage gatewayPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickGatewayTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(expectedBusinessUnitsList[0])
                .clickAddBusinessUnitAcquirerButton()
                .getSelectAcquirerMid().selectAcquirerInDialog(ACQUIRER.getAcquirerName())
                .clickCreateButton()
                .getTable().clickDeactivateBusinessUnitAcquirerButton("0")
                .clickCloseIcon();

        Allure.step("Verify that acquirer status is still 'Active' ");
        assertThat(gatewayPage.getTable().getFirstRowCell("Status")).hasText("Active");

        gatewayPage.getTable().clickDeleteBusinessUnitAcquirer("0")
                .clickDeleteButton();
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.deleteCompany(getApiRequestContext(), company.companyName());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME_DELETION_TEST);
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER.getAcquirerName());
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER_MOVE.getAcquirerName());
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER_EUR.getAcquirerName());
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER_GBP.getAcquirerName());
        super.afterClass();
    }
}
