package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.AboutBlankPage;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TransactionsPageTest extends BaseTest {

    private static final String ADMIN_COMPANY_NAME = "A2 Company";

    private static final List<String> COLUMNS_HEADERS = List.of(
            "Creation Date",
            "Merchant ID",
            "NPGW Reference",
            "Merchant Reference",
            "Amount",
            "Currency",
            "Payment Method",
            "Status");

    @Test
    @TmsLink("108")
    @Epic("Transactions")
    @Feature("Navigation")
    @Description("User navigate to 'Transactions page' after clicking on 'Transactions' link on the header")
    public void testNavigateToTransactionsPage() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transactionsPage.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);

        Allure.step("Verify: Transactions Page Title");
        assertThat(transactionsPage.getPage()).hasTitle(Constants.TRANSACTIONS_URL_TITLE);
    }

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("128")
    @Epic("Transactions")
    @Feature("Currency")
    @Description("Displaying selected currency")
    public void testFilterDisplaysSelectedCurrency(String currency) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickCurrencySelector()
                .selectCurrency(currency)
                .clickRefreshDataButton();

        Allure.step("Verify: Filter displays the selected currency");
        assertThat(transactionsPage.getCurrencySelector()).containsText(currency);
    }

    @Test(dataProvider = "getCurrency", dataProviderClass = TestDataProvider.class)
    @TmsLink("319")
    @Epic("Transactions")
    @Feature("Currency")
    @Description("Filtering transactions by Currency")
    public void testFilterTransactionsByCurrency(String currency) {
        List<String> currencyValues = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getDateRangePicker().setDateRangeFields("01-04-2025", "30-04-2025")
                .clickCurrencySelector()
                .selectCurrency(currency)
                .getTable().getColumnValues("Currency");

        Allure.step("Verify: All values in the Currency column match the selected currency");
        assertTrue(currencyValues.stream().allMatch(value -> value.equals(currency)));
    }

    @Test
    @TmsLink("106")
    @Epic("Transactions")
    @Feature("Number of lines per page field")
    @Description("Displaying the number of rows on the screen when selecting Selector Rows.")
    public void testCountSelectorRows() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: default row count - 25");
        assertThat(transactionsPage.getRowsPerPageButton()).containsText("25");
    }

    @Test
    @TmsLink("127")
    @Epic("Transactions")
    @Feature("Selector rows options")
    @Description("Displaying rows per page options when clicking on Selector Rows")
    public void testCountOptionsSelectorRows() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickRowsPerPageButton();

        Allure.step("Verify: displaying all options when clicking on Selector Rows");
        assertThat(transactionsPage.getRowsPerPageOptions()).hasText("102550100");
    }

    @Test
    @TmsLink("130")
    @Epic("Transactions")
    @Feature("Pagination")
    @Description("Verifying that we can switch the page when we click next button")
    public void testPaginationNextButton() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getDateRangePicker().setDateRangeFields("01-04-2025", "01-05-2025")
                .clickNextPageButton();

        Allure.step("Verify: button 2 is active");
        assertThat(transactionsPage.getPaginationItemTwoActiveButton()).isVisible();
    }

    @Test
    @TmsLink("181")
    @Epic("Transactions")
    @Feature("Panel")
    @Description("Verify that on Transactions Page after clicking on Transactions user can see Panel:"
            + " Date range, Business unit, Currency, Payment method, Status, Amount, Reset filter, "
            + "Apply data, Download file, Settings.")
    public void testTheVisibilityOfTheControlPanelElementsOnTheTransactionsPage() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink();

        Allure.step("Verify: DataRange picker is visible");
        assertThat(transactionsPage.getDateRangePicker().getDateRangePickerField()).isVisible();

        Allure.step("Verify: Business Unit selector is visible");
        assertThat(transactionsPage.getBusinessUnitSelector()).isVisible();

        Allure.step("Verify: Currency selector is visible");
        assertThat(transactionsPage.getCurrencySelector()).isVisible();

        Allure.step("Verify: Payment method selector is visible");
        assertThat(transactionsPage.getPaymentMethodSelector()).isVisible();

        Allure.step("Verify: Status selector is visible");
        assertThat(transactionsPage.getStatusSelector()).isVisible();

        Allure.step("Verify: Amount button is visible");
        assertThat(transactionsPage.getAmountButton()).isVisible();

        Allure.step("Verify: Reset filter button is visible");
        assertThat(transactionsPage.getResetFilterButton()).isVisible();

        Allure.step("Verify: Apply data button is visible");
        assertThat(transactionsPage.getRefreshDataButton()).isVisible();

        Allure.step("Verify: Settings button is visible");
        assertThat(transactionsPage.getSettingsButton()).isVisible();

        Allure.step("Verify: Download button is visible");
        assertThat(transactionsPage.getDownloadButton()).isVisible();
    }

    @Test
    @TmsLink("229")
    @Epic("Transactions")
    @Feature("Status")
    @Description("Verify that user can see selector Status Options")
    public void testTheVisibilityOfTheStatusSelectorOptions() {
        List<String> options = List.of("ALL",
                "INITIATED",
                "PENDING",
                "SUCCESS",
                "FAILED",
                "CANCELLED",
                "EXPIRED");

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickStatusSelector();

        Allure.step("Verify: Selector Status Options are visible");
        assertEquals(transactionsPage.getStatusSelectorOptions(), options);
        Allure.step("Verify: Default selected option in status selector is 'ALL'");
        assertThat(transactionsPage.getActiveOption()).containsText("ALL");
    }

    @Test
    @TmsLink("263")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Choose amount popup functionality")
    public void testChooseAmountPopUp() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
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

        transactionsPage.clickAmountAppliedClearButton();

        Allure.step("Verify: Amount button is visible after reset");
        assertThat(transactionsPage.getAmountButton()).isVisible();
    }

    @Test
    @TmsLink("311")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Filtering transactions by Amount")
    public void testFilterTransactionsByAmount() {
        int amountFrom = 10;
        int amountTo = 500;

        List<String> amountValues = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getDateRangePicker().setDateRangeFields("01-04-2025", "01-05-2025")
                .clickAmountButton()
                .fillAmountFromField(String.valueOf(amountFrom))
                .fillAmountToField(String.valueOf(amountTo))
                .clickAmountApplyButton()
                .getTable().getColumnValues("Amount");

        Allure.step("Verify: Amount column has values within the specified amount range");
        assertTrue(amountValues.stream()
                .map(Integer::parseInt)
                .allMatch(value -> value >= amountFrom && value <= amountTo));
    }

    @Test
    @TmsLink("335")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("error message 'From should be lesser than To' appears")
    public void testErrorMessageByAmount() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
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
    @Feature("Status")
    @Description("Verify that user can see Payment Method Options")
    public void testTheVisibilityOfThePaymentMethodOptions() {
        List<String> options = List.of("ALL",
                "VISA",
                "MASTERCARD");

        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickPaymentMethodSelector();

        Allure.step("Verify: Payment Method Options are visible");
        assertEquals(transactionsPage.getPaymentMethodOptions(), options);
        Allure.step("Verify: Default selected option in Payment Method Options is 'ALL'");
        assertThat(transactionsPage.getActiveOption()).containsText("ALL");
    }

    @Test
    @TmsLink("340")
    @Epic("Transactions")
    @Feature("Data range")
    @Description("Error message is displayed when start date is after end date.")
    public void testErrorMessageForReversedDateRange() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .getDateRangePicker().setDateRangeFields("01-04-2025", "01-04-2024")
                .clickRefreshDataButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(transactionsPage.getDateRangePicker().getDataRangePickerErrorMessage()).hasText(
                "Start date must be before end date.");
    }

    @Test
    @TmsLink("350")
    @Epic("Transactions")
    @Feature("Transactions table header")
    @Description("Verify full lists of column headers in table and visible columns from Settings")
    public void testCheckUncheckAllVisibleColumns() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings();

        List<String> visibleColumnsLabels = transactionsPage
                .getVisibleColumnsLabels();

        List<String> headersList = transactionsPage
                .clickRefreshDataButton()
                .getTable().getColumnHeadersText();

        List<String> headersListAfterUncheckAllVisibleColumns = transactionsPage
                .clickSettingsButton()
                .uncheckAllCheckboxInSettings()
                .clickRefreshDataButton()
                .getTable().getColumnHeadersText();

        Allure.step("Verify: All column headers are displayed in the Settings");
        assertEquals(visibleColumnsLabels, COLUMNS_HEADERS);

        Allure.step("Verify: All column headers are displayed in the transactions table");
        assertEquals(headersList, COLUMNS_HEADERS);

        Allure.step("Verify: Column headers are not displayed in the transactions table "
                + "after it's unchecking in the Settings");
        assertEquals(headersListAfterUncheckAllVisibleColumns.size(), 0);
    }

    @Test
    @TmsLink("359")
    @Epic("Transactions")
    @Feature("Settings")
    @Description("Check/Uncheck Visible columns in the Settings and verify table column headers")
    public void testCheckUncheckOneVisibleColumn() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickSettingsButton()
                .checkAllCheckboxInSettings();

        COLUMNS_HEADERS.forEach(item -> {
            List<String> headersListAfterUncheckOne = transactionsPage
                    .uncheckVisibleColumn(item)
                    .clickRefreshDataButton()
                    .getTable().getColumnHeadersText();

            Allure.step("Verify: Only one column header is NOT displayed in the Transactions. And it's - '{item}'");
            assertTrue((headersListAfterUncheckOne.size() == COLUMNS_HEADERS.size() - 1)
                    && !headersListAfterUncheckOne.contains(item));

            transactionsPage
                    .clickSettingsButton()
                    .checkVisibleColumn(item);
        });

        transactionsPage
                .uncheckAllCheckboxInSettings();

        COLUMNS_HEADERS.forEach(item -> {
            List<String> headersListAfterCheckOnlyOne = transactionsPage
                    .checkVisibleColumn(item)
                    .clickRefreshDataButton()
                    .getTable().getColumnHeadersText();

            Allure.step("Verify: Only one column header is displayed in the transactions table. And it's - '{item}'");
            assertTrue((headersListAfterCheckOnlyOne.size() == 1) && headersListAfterCheckOnlyOne.contains(item));

            transactionsPage
                    .clickSettingsButton()
                    .uncheckVisibleColumn(item);
        });
    }

    @Test
    @TmsLink("354")
    @Epic("Transactions")
    @Feature("Amount")
    @Description("Edit Amount")
    public void testEditAmount() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
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
        TransactionsPage transactionsPage = new DashboardPage(getPage())
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

        transactionsPage.clickAmountApplyButton();

        Allure.step("Verify: Applied amount is visible");
        assertThat(transactionsPage.amountApplied("Amount: 0.00 - 0.00")).isVisible();
    }

    @Test
    @TmsLink("356")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("The presence of the dropdown options export table data to  file")
    public void testPresenceOfDownloadFilesOptions() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickDownloadButton();

        Allure.step("Verify: CVC option is visible");
        assertThat(transactionsPage.getDownloadCsvOption()).isVisible();

        Allure.step("Verify: EXCEL option is visible");
        assertThat(transactionsPage.getDownloadExcelOption()).isVisible();

        Allure.step("Verify: PDF option is visible");
        assertThat(transactionsPage.getDownloadPdfOption()).isVisible();
    }


    @Test(dataProvider = "getMenuItemName", dataProviderClass = TestDataProvider.class)
    @TmsLink("357")
    @Epic("Transactions")
    @Feature("Export table data")
    @Description("Download files: PDF, Excel, CVS")
    public void testDownloadFiles(String menuItemName) {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader().clickTransactionsLink()
                .clickDownloadButton();

        Allure.step("Verify: that files can be downloaded");
        Assert.assertTrue(transactionsPage.isFileAvailableAndNotEmpty(menuItemName));
    }

    @Test
    @TmsLink("520")
    @Epic("Transactions")
    @Feature("Business unit")
    @Description("Verify that the Company admin can see all the company's business units in the Business unit "
            + "dropdown list")
    public void testTheVisibilityOfTheAvailableBusinessUnitOptions(@Optional("UNAUTHORISED") String userRole) {
        List<String> businessUnitNames = List.of("Business unit 1", "Business unit 2", "Business unit 3",
                "Business unit 4");
        String companyAdminEmail = "companyAdmin@gmail.com";
        String companyAdminPassword = "CompanyAdmin1!";
        TestUtils.deleteUser(getApiRequestContext(), companyAdminEmail);
        TestUtils.deleteCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompany(getApiRequestContext(), ADMIN_COMPANY_NAME);
        TestUtils.createCompanyAdmin(
                getApiRequestContext(), ADMIN_COMPANY_NAME, companyAdminEmail, companyAdminPassword);
        businessUnitNames.forEach(businessUnitName -> TestUtils.createBusinessUnit(
                getApiRequestContext(), ADMIN_COMPANY_NAME, businessUnitName));

        TransactionsPage transactionsPage = new AboutBlankPage((getPage()))
                .navigate("/login")
                .loginAndChangePassword(companyAdminEmail, companyAdminPassword)
                .getAlert().waitUntilSuccessAlertIsGone()
                .getHeader().clickTransactionsLink()
                .getSelectBusinessUnit().clickSelectBusinessUnitPlaceholder();

        Allure.step("Verify: Company's business units are visible");
        assertThat(transactionsPage.getSelectBusinessUnit().getDropdownOptionList()).hasText(businessUnitNames
                .toArray(String[]::new));
    }
}
