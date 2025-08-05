package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.transactions.SuperTransactionsPage;

import java.util.List;
import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;

public class TransactionsPageTest extends BaseTest {

    private static final String COMPANY_NAME = "%s test request company".formatted(RUN_ID);
    private static final String MERCHANT_TITLE = "%s test request merchant".formatted(RUN_ID);
    private final String[] businessUnitNames = new String[]{"Business unit 1", "Business unit 2", "Business unit 3"};
    private BusinessUnit businessUnit;

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createBusinessUnits(getApiRequestContext(), getCompanyName(), businessUnitNames);
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        businessUnit = TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_NAME, MERCHANT_TITLE);
    }

    @Test
    @TmsLink("108")
    @Epic("Transactions")
    @Feature("Navigation")
    @Description("User navigate to 'Transactions page' after clicking on 'Transactions' link on the header")
    public void testNavigateToTransactionsPage() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transactionsPage.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);

        Allure.step("Verify: Transactions Page Title");
        assertThat(transactionsPage.getPage()).hasTitle(Constants.TRANSACTIONS_URL_TITLE);
    }

    @Test
    @TmsLink("181")
    @Epic("Transactions")
    @Feature("Panel")
    @Description("Verify that on the Transactions Page user can see Panel: Company"
            + " Business unit, Date range, Currency, Card type, Status, Trx Ids, Amount, Reset filter, "
            + "Apply data, Download file, Settings.")
    public void testVisibilityOfControlPanelElements() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Company selector is visible");
        assertThat(transactionsPage.getSelectCompany().getSelectCompanyField()).isVisible();

        Allure.step("Verify: Business Unit selector is visible");
        assertThat(transactionsPage.getBusinessUnitSelector()).isVisible();

        Allure.step("Verify: DataRange picker is visible");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).isVisible();

        Allure.step("Verify: Currency selector is visible");
        assertThat(transactionsPage.getCurrencySelector()).isVisible();

        Allure.step("Verify: Card type selector is visible");
        assertThat(transactionsPage.getCardTypeSelector()).isVisible();

        Allure.step("Verify: Status selector is visible");
        assertThat(transactionsPage.getSelectStatus().getStatusSelector()).isVisible();

        Allure.step("Verify: Search 'Trx Ids'  is visible");
        assertThat(transactionsPage.getSearchTrxIdsButton()).isVisible();

        Allure.step("Verify: Amount button is visible");
        assertThat(transactionsPage.getAmountButton()).isVisible();

        Allure.step("Verify: Reset filter button is visible");
        assertThat(transactionsPage.getResetFilterButton()).isVisible();

        Allure.step("Verify: Apply data button is visible");
        assertThat(transactionsPage.getRefreshDataButton()).isVisible();

        Allure.step("Verify: Download button is visible");
        assertThat(transactionsPage.getDownloadButton()).isVisible();

        Allure.step("Verify: Settings button is visible");
        assertThat(transactionsPage.getSettingsButton()).isVisible();
    }

    @Test
    @TmsLink("229")
    @Epic("Transactions")
    @Feature("Status")
    @Description("Verify that user can see selector Status Options")
    public void testTheVisibilityOfTheStatusSelectorOptions() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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
    public void testChooseAmountPopUp() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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
    public void testErrorMessageByAmount() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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
    public void testTheVisibilityOfTheCardTypeOptions() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickCardTypeSelector();

        Allure.step("Verify: Payment Method Options are visible");
        assertEquals(transactionsPage.getCardTypeOptions(), List.of("ALL", "VISA", "MASTERCARD"));

        Allure.step("Verify: Default selected option in Payment Method Options is 'ALL'");
        assertThat(transactionsPage.getSelectStatus().getStatusValue()).containsText("ALL");
    }

    @Test
    @TmsLink("340")
    @Epic("Transactions")
    @Feature("Date range")
    @Description("Error message is displayed when start date is after end date.")
    public void testErrorMessageForReversedDateRange() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .getSelectDateRange().setDateRangeFields("01-04-2025", "01-04-2024")
                .clickSettingsButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(transactionsPage.getSelectDateRange().getErrorMessage())
                .hasText("Start date must be before end date.");
    }

    @Test
    @TmsLink("354")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Edit Amount")
    public void testEditAmount() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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
    public void testResetAmountValues() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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

    @Ignore("Download button is unavailable")
    @Test
    @TmsLink("356")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("The presence of the dropdown options export table data to file")
    public void testPresenceOfDownloadFilesOptions() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .clickDownloadButton();

        Allure.step("Verify: CSV option is visible");
        assertThat(transactionsPage.getDownloadCsvOption()).isVisible();

        Allure.step("Verify: EXCEL option is visible");
        assertThat(transactionsPage.getDownloadExcelOption()).isVisible();

        Allure.step("Verify: PDF option is visible");
        assertThat(transactionsPage.getDownloadPdfOption()).isVisible();
    }

    @Test(dataProvider = "getExportFileType", dataProviderClass = TestDataProvider.class)
    @TmsLink("357")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("Download files: PDF, Excel, CSV")
    public void testDownloadFiles(String fileType) {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields(TestUtils.lastBuildDate(getApiRequestContext()))
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .clickDownloadButton();

        Allure.step("Verify: that files can be downloaded");
        Assert.assertTrue(transactionsPage.isFileAvailableAndNotEmpty(fileType));
    }

    @Test
    @TmsLink("503")
    @Epic("Transactions")
    @Feature("Business unit")
    @Description("Verify that the Company admin can see all the company's business units in the Business unit "
            + "dropdown list")
    public void testTheVisibilityOfTheAvailableBusinessUnitOptions(@Optional("ADMIN") String userRole) {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage((getPage()))
                .getHeader().clickTransactionsLink()
                .getSelectBusinessUnit().clickSelectBusinessUnitPlaceholder();

        Allure.step("Verify: Company's business units are visible");
        assertThat(transactionsPage.getSelectBusinessUnit().getDropdownOptionList()).hasText(businessUnitNames);
    }

    @Ignore("Right now Refresh button is unavailable for only currency changed")
    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("567")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Currency' to default value ( ALL)")
    public void testResetCurrency(String currency) {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Filter displays 'ALL' by default");
        assertThat(transactionsPage.getCurrencySelector()).containsText("ALL");

        transactionsPage
                .clickCurrencySelector()
                .selectCurrency(currency);

        Allure.step("Verify: Filter displays the selected currency");
        assertThat(transactionsPage.getCurrencySelector()).containsText(currency);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: Filter displays 'ALL' after applying 'Reset filter' button ");
        assertThat(transactionsPage.getCurrencySelector()).containsText("ALL");
    }

    @Ignore("failed on .getRequestData")
    @Test
    @TmsLink("620")
    @Epic("Transactions")
    @Feature("Refresh data")
    @Description("Verify the request to server contains all the information from the filter")
    public void testRequestToServer() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectDateRange().setDateRangeFields("01-05-2025", "07-05-2025")
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE)
                .clickCurrencySelector()
                .selectCurrency("USD")
                .selectCardType("VISA")
                .clickAmountButton()
                .fillAmountFromField("500")
                .fillAmountToField("10000")
                .clickAmountApplyButton();

        Allure.step("Verify: merchant ID is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains(businessUnit.merchantId()));

        Allure.step("Verify: dateFrom is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("2025-05-01T00:00:00.000Z"));

        Allure.step("Verify: dateTo is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("2025-05-07T23:59:59.999Z"));

        Allure.step("Verify: currency is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("USD"));

        Allure.step("Verify: paymentMethod is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("VISA"));

        Allure.step("Verify:amountFrom is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("500"));

        Allure.step("Verify: amountTo is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("10000"));
    }

    @Ignore("failed on .getRequestData")
    @Test
    @TmsLink("621")
    @Epic("Transactions")
    @Feature("Refresh data")
    @Description("Verify the status is sent to the server")
    public void testStatusRequestServer() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE)
                .getSelectStatus().clickSelector()
                .getSelectStatus().clickValue("SUCCESS")
                .getSelectStatus().clickSelector();

        Allure.step("Verify: status is sent to the server");
        assertTrue(transactionsPage.getRequestData().contains("SUCCESS"));
    }

    @Ignore("Right now Refresh button is unavailable for only Cart type changed")
    @Test(dataProvider = "getCardType", dataProviderClass = TestDataProvider.class)
    @TmsLink("598")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Card Type' to default value ( ALL)")
    public void testResetCardType(String cardType) {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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

    @Ignore("Right now Refresh button is unavailable for only status changed")
    @Test(dataProvider = "getStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("639")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Status' to default value ( ALL)")
    public void testResetStatus(String status) {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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
    public void testResetMultiStatus(String status1, String status2) {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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

    @Ignore("Right now Refresh button is unavailable for only amount changed")
    @Test
    @TmsLink("668")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' button change 'Amount' to default value ( AMOUNT)")
    public void testResetAmount() {
        final String amountFrom = "10";
        final String amountTo = "20";
        final String chosenAmount = "Amount: " + amountFrom + " - " + amountTo;

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
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

    @Ignore("Right now Refresh button is unavailable for only Company selected")
    @Test
    @TmsLink("686")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' clean 'Company' input field")
    public void testResetCompany() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: the 'Company' input field is empty by default");
        assertThat(transactionsPage.getSelectCompany().getSelectCompanyField()).isEmpty();

        transactionsPage
                .getSelectCompany().selectCompany(COMPANY_NAME);

        Allure.step("Verify: selected company is displayed in the 'Company' input field");
        assertThat(transactionsPage.getSelectCompany().getSelectCompanyField()).hasValue(COMPANY_NAME);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: the 'Company' input field is empty after reset");
        assertThat(transactionsPage.getSelectCompany().getSelectCompanyField()).isEmpty();
    }

    @Test
    @TmsLink("701")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that 'Reset filter' clean 'Business Unit' input field")
    public void testResetBusinessUnit() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: the 'Business Unit' input field is empty by default");
        assertThat(transactionsPage.getSelectBusinessUnit().getSelectBusinessUnitField()).isEmpty();

        transactionsPage
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE);

        Allure.step("Verify: selected Business Unit is displayed in the 'Business Unit' input field");
        assertThat(transactionsPage.getSelectBusinessUnit().getSelectBusinessUnitField()).hasValue(MERCHANT_TITLE);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: the 'Business Unit' input field is empty after reset");
        assertThat(transactionsPage.getSelectBusinessUnit().getSelectBusinessUnitField()).isEmpty();
    }

    @Ignore("Right now Refresh button is unavailable for only data range changed")
    @Test
    @TmsLink("736")
    @Epic("Transactions")
    @Feature("Reset filter")
    @Description("Verify, that ")
    public void testResetData() {
        final String startDate = "01-04-2025";
        final String endDate = "30-04-2025";
        final String dataFrom = startDate.replaceAll("-", "/");
        final String dataTo = endDate.replaceAll("-", "/");
        final String selectedRange = "Date range" + dataFrom + "-" + dataTo;
        final String currentRange = TestUtils.getCurrentRange();

        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: the 'Data' input field value is current month by default");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).hasText(currentRange);

        transactionsPage
                .getSelectDateRange().setDateRangeFields(startDate, endDate);

        Allure.step("Verify: the 'Data' input field value is checked period");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).hasText(selectedRange);

        transactionsPage
                .clickResetFilterButton();

        Allure.step("Verify: the 'Data' input field value is current month after reset");
        assertThat(transactionsPage.getSelectDateRange().getDateRangeField()).hasText(currentRange);
    }

    @Test
    @TmsLink("851")
    @Epic("Transactions")
    @Feature("Transactions Search")
    @Description("Verify that 'NPGW reference' and 'Business unit reference' fields appear when clicking on 'Trx IDs'.")
    public void testSearchOptionsVisibleAfterClickingTrxIds() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickSearchTrxIdsButton();

        Allure.step("Verify: 'NPGW reference' is visible ");
        assertThat(transactionsPage.getNpgwReferenceField()).isVisible();

        Allure.step("Verify: 'Business unit reference' is visible ");
        assertThat(transactionsPage.getBusinessUnitReference()).isVisible();
    }

    @Test
    @TmsLink("853")
    @Epic("Transactions")
    @Feature("Transactions Search")
    @Description("Verify that 'NPGW reference' and 'Business unit reference' fields appear when clicking on 'Trx IDs'.")
    public void testTransactionSearchByNpgwReference() {
        SuperTransactionsPage transactionsPage = new SuperDashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN);

        List<Locator> npgwReference = transactionsPage.getTable().getColumnCells("NPGW reference");

        int index1 = new Random().nextInt(npgwReference.size());
        int index2;
        do {
            index2 = new Random().nextInt(npgwReference.size());
        } while (index2 == index1);

        String npgwReferenceText1 = npgwReference.get(index1).innerText();
        String npgwReferenceText2 = npgwReference.get(index2).innerText();

        Locator filteredRows1 = transactionsPage
                .clickSearchTrxIdsButton()
                .fillNpgwReference(npgwReferenceText1)
                .clickTrxIdAppliedButton()
                .getTable().getRows();

        Allure.step("Verify: Table has only one row with the N1 NPGW reference");
        assertThat(filteredRows1).hasCount(1);
        assertThat(filteredRows1).containsText(npgwReferenceText1);

        Locator filteredRows2 = transactionsPage
                .clickTrxIdPencilIcon()
                .clickNpgwReferenceClearIcon()
                .fillNpgwReference(npgwReferenceText2)
                .clickTrxIdAppliedButton()
                .getTable().getRows();

        Allure.step("Verify: Table has only one row with the N2 NPGW reference");
        assertThat(filteredRows2).hasCount(1);
        assertThat(filteredRows2).containsText(npgwReferenceText2);

        Locator tableTransactionNotFiltered = transactionsPage
                .clickTrxIdClearIcon()
                .getTable().getRows();

        Allure.step("Verify: Table contains more than one row");
        assertTrue(tableTransactionNotFiltered.count() > 1, "Expected more than one transaction row");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
