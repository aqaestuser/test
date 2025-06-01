package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.dialog.reports.ReportsParametersDialog;

import java.util.HashSet;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ReportsPageTest extends BaseTest {

    private static final String COMPANY_NAME = "Generate report%s".formatted(RUN_ID);
    private static final String MERCHANT_TITLE = "Generate report merchant%s".formatted(RUN_ID);
    private BusinessUnit businessUnit;

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        businessUnit = TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_NAME, MERCHANT_TITLE);
    }

    @Test
    @TmsLink("153")
    @Epic("Reports")
    @Feature("Navigation")
    @Description("User navigate to 'Reports page' after clicking on 'Reports' link on the header")
    public void testNavigateToReportsPage() {
        ReportsPage reportsPage = new DashboardPage(getPage())
                .clickReportsLink();

        Allure.step("Verify: Reports Page URL");
        assertThat(reportsPage.getPage()).hasURL(Constants.REPORTS_PAGE_URL);

        Allure.step("Verify: Reports Page Title");
        assertThat(reportsPage.getPage()).hasTitle(Constants.REPORTS_URL_TITLE);
    }

    @Test
    @TmsLink("405")
    @Epic("Reports")
    @Feature("Data range")
    @Description("Error message is displayed when start date is after end date.")
    public void testErrorMessageForReversedDateRange() {
        ReportsPage reportsPage = new DashboardPage(getPage())
                .clickReportsLink()
                .getSelectDateRange().setDateRangeFields("01-04-2025", "01-04-2024")
                .clickRefreshDataButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(reportsPage.getSelectDateRange().getErrorMessage())
                .hasText("Start date must be before end date.");
    }

    @Test
    @TmsLink("510")
    @Epic("Reports")
    @Feature("Generate report")
    @Description("Verify content of 'Generation Parameters dialog'")
    public void testContentOfGenerationParametersDialog() {
        ReportsParametersDialog generationParametersDialog = new DashboardPage(getPage())
                .clickReportsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE)
                .clickGenerateReportButton();

        Allure.step("Verify: Dialog header text");
        assertThat(generationParametersDialog.getDialogHeader()).hasText("Generation Parameters");

        Allure.step("Verify: 'Generate' button is enabled");
        assertThat(generationParametersDialog.getGenerateButton()).isEnabled();

        Allure.step("Verify: All report column names are listed in the 'Generation Parameters dialog'");
        Assert.assertEquals(new HashSet<>(generationParametersDialog.getReportColumns()), new HashSet<>(List.of(
                "Merchant Name",
                "Business Unit",
                "Merchant ID",
                "Creation Date",
                "TimeZone",
                "Transaction Type",
                "Status",
                "Transaction ID",
                "External ID",
                "Gross Currency",
                "Gross Credit",
                "Payment Method")));
    }

    @Test
    @TmsLink("512")
    @Epic("Reports")
    @Feature("Generate report")
    @Description("Check/uncheck reports columns in the 'Generation Parameters dialog'")
    public void testCheckboxesOfGenerationParameters() {
        ReportsParametersDialog generationParametersDialog = new DashboardPage(getPage())
                .clickReportsLink()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE)
                .clickGenerateReportButton();

        Allure.step("Verify: All report columns are checked at first opening the 'Generation Parameters dialog'");
        Assert.assertTrue(generationParametersDialog.isAllColumnsChecked());

        generationParametersDialog
                .hoverOnReportColumnsCheckbox();

        Allure.step("Verify: Tooltip 'Deselect all' appears after hover on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isTextVisible("Deselect all"));

        generationParametersDialog
                .clickReportColumnsCheckbox();

        Allure.step("Verify: All report columns are unchecked after click on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isAllColumnsUnchecked());

        generationParametersDialog
                .clickCloseIcon()
                .clickTransactionsLink()
                .clickReportsLink()
                .clickGenerateReportButton();

        Allure.step("Verify: All report columns remained unchecked after exiting the 'Generation Parameters dialog'");
        Assert.assertTrue(generationParametersDialog.isAllColumnsUnchecked());

        generationParametersDialog
                .hoverOnReportColumnsCheckbox();

        Allure.step("Verify: Tooltip 'Select all' appears after hover on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isTextVisible("Select all"));

        generationParametersDialog
                .clickReportColumnsCheckbox();

        Allure.step("Verify: All report columns are checked after click on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isAllColumnsChecked());

        generationParametersDialog
                .clickAllColumnCheckboxesOneByOne();

        Allure.step("Verify: All report columns are unchecked after clicking on them one by one");
        Assert.assertTrue(generationParametersDialog.isAllColumnsUnchecked());

        generationParametersDialog
                .clickAllColumnCheckboxesOneByOne();

        Allure.step("Verify: All report columns are checked after clicking on them one by one");
        Assert.assertTrue(generationParametersDialog.isAllColumnsChecked());
    }

    @Test
    @TmsLink("653")
    @Epic("Reports")
    @Feature("Reset filter")
    @Description("'Reset filter' clears selected options to default")
    public void testResetFilter() {
        ReportsPage reportsPage = new ReportsPage(getPage())
                .clickReportsLink()
                .refreshReports();

        String defaultStartDate = reportsPage.getSelectDateRange().getStartDate().textContent();
        String defaultEndDate = reportsPage.getSelectDateRange().getEndDate().textContent();

        reportsPage
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(MERCHANT_TITLE)
                .getSelectDateRange().setDateRangeFields("01-04-2025", "01-05-2025")
                .clickResetFilterButton();

        Allure.step("Verify: the selected company field is empty after reset");
        assertThat(reportsPage.getSelectCompany().getSelectCompanyField()).isEmpty();

        Allure.step("Verify: the selected business unit field is empty after reset");
        assertThat(reportsPage.getSelectBusinessUnit().getSelectBusinessUnitField()).isEmpty();

        Allure.step("Verify: the selected date picker dates are returned to default");
        assertThat(reportsPage.getSelectDateRange().getStartDate()).hasText(defaultStartDate);
        assertThat(reportsPage.getSelectDateRange().getEndDate()).hasText(defaultEndDate);
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteBusinessUnit(getApiRequestContext(), COMPANY_NAME, businessUnit);
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
