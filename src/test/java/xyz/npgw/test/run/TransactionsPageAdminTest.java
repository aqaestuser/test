package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.AdminDashboardPage;
import xyz.npgw.test.page.transactions.AdminTransactionsPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class TransactionsPageAdminTest extends BaseTest {

    private static final String COMPANY_NAME = "%s test request company".formatted(RUN_ID);
    private final String[] businessUnitNames = new String[]{"Business unit 1", "Business unit 2", "Business unit 3"};

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createBusinessUnits(getApiRequestContext(), getCompanyName(), businessUnitNames);
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
    }

    @Test
    @TmsLink("108")
    @Epic("Transactions")
    @Feature("Navigation")
    @Description("User navigate to 'Transactions page' after clicking on 'Transactions' link on the header")
    public void testNavigateToTransactionsPageAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transactionsPage.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);

        Allure.step("Verify: Transactions Page Title");
        assertThat(transactionsPage.getPage()).hasTitle(Constants.TRANSACTIONS_URL_TITLE);
    }

    @Test
    @TmsLink("229")
    @Epic("Transactions")
    @Feature("Status")
    @Description("Verify that user can see selector Status Options")
    public void testTheVisibilityOfTheStatusSelectorOptionsAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectStatus().clickSelector();

        Allure.step("Verify: Selector Status Options are visible");
        assertThat(transactionsPage.getSelectStatus().getStatusOptions()).hasText(new String[]{
                "ALL",
                "INITIATED",
                "PENDING",
                "SUCCESS",
                "FAILED",
                "CANCELLED",
                "EXPIRED",
                "PARTIAL_REFUND",
                "REFUND",
        });

        Allure.step("Verify: Default selected option in status selector is 'ALL'");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).containsText("ALL");
    }

    @Test
    @TmsLink("263")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Choose amount popup functionality")
    public void testChooseAmountPopUpAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("10")
                .fillAmountFromField("20")
                .clickAmountClearButton()
                .fillAmountFromField("100")
                .clickAmountFromIncreaseArrow()
                .clickAmountFromIncreaseArrow()
                .clickAmountFromDecreaseArrow()
                .clickClearAmountToButton()
                .fillAmountToField("5000")
                .clickAmountToIncreaseArrow()
                .clickAmountToDecreaseArrow()
                .clickAmountToDecreaseArrow()
                .clickAmountApplyButton();

        Allure.step("Verify: Applied amount is visible");
        assertThat(transactionsPage.amountApplied("Amount: 101 - 4999")).isVisible();

        transactionsPage
                .clickAmountAppliedClearButton();

        Allure.step("Verify: Amount button is visible after reset");
        assertThat(transactionsPage.getAmountButton()).isVisible();
    }

    @Test
    @TmsLink("335")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Error message 'From should be lesser than To' appears")
    public void testErrorMessageByAmountAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10");

        Allure.step("Verify: error message 'From should be lesser than To' appears");
        assertThat(transactionsPage.getAmountErrorMessage()).hasText("\"From\" should be lesser than \"To");
    }

    @Test
    @TmsLink("342")
    @Epic("Transactions")
    @Feature("Card type")
    @Description("Verify that user can see 'Card type' options")
    public void testTheVisibilityOfTheCardTypeOptionsAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickCardTypeSelector();

        Allure.step("Verify: Payment Method Options are visible");
        assertEquals(transactionsPage.getCardTypeOptions(), List.of("ALL", "VISA", "MASTERCARD"));

        Allure.step("Verify: Default selected option in Payment Method Options is 'ALL'");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).containsText("ALL");
    }

    @Test
    @TmsLink("354")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Edit Amount")
    public void testEditAmountAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10000")
                .clickAmountApplyButton()
                .clickAmountEditButton()
                .fillAmountFromField("500")
                .fillAmountToField("10300");

        Allure.step("Verify: Edited amount is visible");
        assertThat(transactionsPage.amountApplied("Amount: 500 - 10300")).isVisible();
    }

    @Test
    @TmsLink("355")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Reset Amount Values")
    public void testResetAmountValuesAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10000")
                .clickAmountApplyButton()
                .clickAmountAppliedClearButton()
                .clickAmountButton();

        Allure.step("Verify: From Amount is zero");
        assertThat(transactionsPage.getAmountFromInputField()).hasValue("0.00");

        Allure.step("Verify: To Amount is zero");
        assertThat(transactionsPage.getAmountToInputField()).hasValue("0.00");

        transactionsPage
                .clickAmountApplyButton();

        Allure.step("Verify: Applied amount is visible");
        assertThat(transactionsPage.amountApplied("Amount: 0.00 - 0.00")).isVisible();
    }

    @Ignore("Empty bu list")
    @Test
    @TmsLink("503")
    @Epic("Transactions")
    @Feature("Business unit")
    @Description("Verify that the Company admin can see all the company's business units in the Business unit "
            + "dropdown list")
    public void testTheVisibilityOfTheAvailableBusinessUnitOptionsAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage((getPage()))
                .getHeader().clickTransactionsLink()
                .getSelectBusinessUnit().clickSelectBusinessUnitPlaceholder();

        Allure.step("Verify: Company's business units are visible");
        assertThat(transactionsPage.getSelectBusinessUnit().getDropdownOptionList()).hasText(businessUnitNames);
    }

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("567")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Currency' to default value ( ALL)")
    public void testResetCurrencyAsAdmin(String currency) {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getSelectCurrency().getCurrencySelector()).containsText("ALL");

        transactionsPage.getSelectCurrency().select(currency);

        Allure.step("Verify: Filter displays the selected currency");
        assertThat(transactionsPage.getSelectCurrency().getCurrencySelector()).containsText(currency);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button ");
        assertThat(transactionsPage.getSelectCurrency().getCurrencySelector()).containsText("ALL");
    }

    @Test(dataProvider = "getCardType", dataProviderClass = TestDataProvider.class)
    @TmsLink("598")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Card Type' to default value ( ALL)")
    public void testResetCardTypeAsAdmin(String cardType) {
        AdminTransactionsPage transactionsPage = new AdminTransactionsPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getCardTypeValue()).containsText("ALL");

        transactionsPage
                .selectCardType(cardType);

        Allure.step("Verify: Filter displays the selected payment method");
        assertThat(transactionsPage.getCardTypeValue()).containsText(cardType);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button");
        assertThat(transactionsPage.getCardTypeValue()).containsText("ALL");
    }

    @Test(dataProvider = "getStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("639")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Status' to default value ( ALL)")
    public void testResetStatusAsAdmin(String status) {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");

        transactionsPage
                .getSelectStatus().selectTransactionStatuses(status);

        Allure.step("Verify: Filter displays the selected Status");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText(status);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");
    }

    @Ignore("multistatus not working atm")
    @Test(dataProvider = "getMultiStatus2", dataProviderClass = TestDataProvider.class)
    @TmsLink("655")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Status' (two options are checked) to default value ( ALL)")
    public void testResetMultiStatusAsAdmin(String status1, String status2) {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");

        transactionsPage
                .getSelectStatus().selectTransactionStatuses(status1, status2);

        Allure.step("Verify: Filter displays the selected Status");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText(status1 + ", " + status2);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).hasText("ALL");
    }

    @Test
    @TmsLink("668")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Amount' to default value ( AMOUNT)")
    public void testResetAmountAsAdmin() {
        final String amountFrom = "10";
        final String amountTo = "20";
        final String chosenAmount = "Amount: " + amountFrom + " - " + amountTo;

        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Filter 'Amount' displays 'Amount' by default");
        assertThat(transactionsPage.getAmountButton()).isVisible();
        assertThat(transactionsPage.getAmountButton()).hasText("Amount");

        transactionsPage
                .clickAmountButton()
                .fillAmountFromField(amountFrom)
                .fillAmountToField(amountTo)
                .clickAmountApplyButton();

        Allure.step("Verify: Filter 'Amount' displays 'Amount: {amountFrom}- {amountTo}'");
        assertThat(transactionsPage.amountApplied(chosenAmount)).isVisible();
        assertThat(transactionsPage.amountApplied(chosenAmount)).hasText(chosenAmount);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter 'Amount' displays 'Amount' by default");
        assertThat(transactionsPage.getAmountButton()).isVisible();
        assertThat(transactionsPage.getAmountButton()).hasText("Amount");
    }

    @Test
    @TmsLink("736")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that date picker contains default value before and after reset filter")
    public void testResetDataAsTestAdmin() {
        final String dateRange = "01/04/2025-30/04/2025";
        final String defaultRange = TestUtils.getCurrentRange();

        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: the 'Data' input field value is current month by default");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).containsText(defaultRange);

        transactionsPage
                .getSelectDateRange().setDateRangeFields(dateRange);

        Allure.step("Verify: the 'Data' input field value is checked period");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).containsText(dateRange);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: the 'Data' input field value is current month after reset");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).containsText(defaultRange);
    }

    @Test
    @TmsLink("851")
    @Epic("Transactions")
    @Feature("Transactions Search")
    @Description("Verify that 'NPGW reference' and 'Business unit reference' fields appear when clicking on 'Trx IDs'.")
    public void testSearchOptionsVisibleAfterClickingTrxIdsAsAdmin() {
        AdminTransactionsPage transactionsPage = new AdminDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickSearchTrxIdsButton();

        Allure.step("Verify: 'NPGW reference' is visible ");
        assertThat(transactionsPage.getNpgwReferenceField()).isVisible();

        Allure.step("Verify: 'Business unit reference' is visible ");
        assertThat(transactionsPage.getBusinessUnitReference()).isVisible();
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
