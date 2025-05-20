package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.ReportsPage;
import xyz.npgw.test.page.dialog.reports.ReportsParametersDialog;

import java.util.HashSet;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ReportsPageTest extends BaseTest {

    private static final List<String> REPORT_COLUMNS = List.of(
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
            "Payment Method"
    );

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
                .getDateRangePicker().setDateRangeFields("01-04-2025", "01-04-2024")
                .clickRefreshDataButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(reportsPage.getDateRangePicker().getDataRangePickerErrorMessage()).hasText(
                "Start date must be before end date.");
    }

    @Test
    @TmsLink("510")
    @Epic("Reports")
    @Feature("Generate report")
    @Description("Verify content of 'Generation Parameters dialog'")
    public void testContentOfGenerationParametersDalog() {
        ReportsParametersDialog generationParametersDialog = new DashboardPage(getPage())
                .clickReportsLink()
                .clickGenerateReportButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(generationParametersDialog.getDialogHeader()).hasText("Generation Parameters");

        Allure.step("Verify: 'Generate' button is disabled with not selected business unit");
        assertThat(generationParametersDialog.getGenerateButton()).isDisabled();

        Allure.step("Verify: All report column names are listed in the 'Generation Parameters dialog'");
        Assert.assertEquals(
                new HashSet<>(generationParametersDialog.getReportColumns()), new HashSet<>(REPORT_COLUMNS));
    }

    @Test
    @TmsLink("512")
    @Epic("Reports")
    @Feature("Generate report")
    @Description("Check/uncheck reports columns in the 'Generation Parameters dialog'")
    public void testCheckboxesOfGenerationParameters() {
        ReportsParametersDialog generationParametersDialog = new DashboardPage(getPage())
                .clickReportsLink()
                .clickGenerateReportButton();

        Allure.step("Verify: All report columns are checked at first opening the 'Generation Parameters dialog'");
        Assert.assertTrue(generationParametersDialog.isAllColumnnsChecked());

        generationParametersDialog
                .hoverOnReportColumnsCheckbox();

        Allure.step("Verify: Tooltip 'Deselect all' appears after hover on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isTextVisible("Deselect all"));

        generationParametersDialog
                .clickReportColumnsCheckbox();

        Allure.step("Verify: All report columns are unchecked after click on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isAllColumnnsUnchecked());

        generationParametersDialog
                .clickCloseIcon()
                .clickTransactionsLink()
                .clickReportsLink()
                .clickGenerateReportButton();

        Allure.step("Verify: All report columns remained unchecked after exiting the 'Generation Parameters dialog'");
        Assert.assertTrue(generationParametersDialog.isAllColumnnsUnchecked());

        generationParametersDialog
                .hoverOnReportColumnsCheckbox();

        Allure.step("Verify: Tooltip 'Select all' appears after hover on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isTextVisible("Select all"));

        generationParametersDialog
                .clickReportColumnsCheckbox();

        Allure.step("Verify: All report columns are checked after click on the 'Report columns' checkbox");
        Assert.assertTrue(generationParametersDialog.isAllColumnnsChecked());

        generationParametersDialog
                .clickAllColumnCheckboxesOneByOne();

        Allure.step("Verify: All report columns are unchecked after clicking on them one by one");
        Assert.assertTrue(generationParametersDialog.isAllColumnnsUnchecked());

        generationParametersDialog
                .clickAllColumnCheckboxesOneByOne();

        Allure.step("Verify: All report columns are checked after clicking on them one by one");
        Assert.assertTrue(generationParametersDialog.isAllColumnnsChecked());
    }
}
