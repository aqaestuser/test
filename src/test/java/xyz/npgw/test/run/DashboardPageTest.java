package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.BusinessUnit;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;

public class DashboardPageTest extends BaseTest {

    @Test
    @TmsLink("151")
    @Epic("Dashboard")
    @Feature("Navigation")
    @Description("User navigate to 'Dashboard page' after login")
    public void testNavigateToDashboardAfterLogin() {
        DashboardPage dashboardPage = new DashboardPage(getPage());

        Allure.step("Verify: Dashboard Page URL");
        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);

        Allure.step("Verify: Dashboard Page Title");
        assertThat(dashboardPage.getPage()).hasTitle(Constants.DASHBOARD_URL_TITLE);
    }

    @Test
    @TmsLink("403")
    @Epic("Dashboard")
    @Feature("Data range")
    @Description("Error message is displayed when start date is after end date.")
    public void testErrorMessageForReversedDateRange() {
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .getDateRangePicker()
                .setDateRangeFields("01-04-2025", "01-04-2024")
                .clickRefreshDataButton();

        Allure.step("Verify: error message is shown for invalid date range");
        assertThat(dashboardPage.getDateRangePicker().getDataRangePickerErrorMessage()).hasText(
                "Start date must be before end date.");
    }

    @Test
    @TmsLink("575")
    @Epic("Dashboard")
    @Feature("Chart Display")
    @Description("All key chart elements are correctly displayed")
    public void testVisibleChartElementsAreDisplayedCorrectly() {
        DashboardPage dashboardPage = new DashboardPage(getPage());

        Allure.step("Verify: Y-axis percentage labels are correctly displayed");
        assertThat(dashboardPage.getYAxisLabels())
                .hasText(new String[]{"100%", "80%", "60%", "40%", "20%", "0%"});

        Allure.step("Verify: status chart legend labels are correctly displayed");
        assertThat(dashboardPage.getXAxisTexts())
                .hasText(new String[]{"INITIATED", "SUCCESS", "FAILED"});

        Allure.step("Verify: currency legend labels are correctly displayed");
        assertThat(dashboardPage.getCurrencyLegendLabels())
                .hasText(new String[]{"EUR", "USD", "GBP"});
    }

    @Test
    @TmsLink("577")
    @Epic("Dashboard")
    @Feature("Reset filter")
    @Description("'Reset filter' clears selected options to default")
    public void testResetFilter() {
        final String companyName = "testResetFilter";
        TestUtils.deleteCompany(getApiRequestContext(), companyName);
        TestUtils.createCompany(getApiRequestContext(), companyName);
        TestUtils.createBusinessUnit(getApiRequestContext(), companyName, "testResetFilter");

        DashboardPage dashboardPage = new DashboardPage(getPage())
                .refreshDashboard()
                .getSelectCompany().selectCompany(companyName)
                .getSelectBusinessUnit().selectBusinessUnit("testResetFilter")
                .clickCurrencySelector()
                .selectCurrency("EUR")
                .clickResetFilterButton();

        Allure.step("Verify: the selected company field is empty after reset");
        assertThat(dashboardPage.getSelectCompany().getSelectCompanyField()).hasValue("");

        Allure.step("Verify: the selected business unit field is empty after reset");
        assertThat(dashboardPage.getSelectBusinessUnit().getSelectBusinessUnitField()).hasValue("");

        Allure.step("Verify: the currency selector displays 'ALL' after reset");
        assertThat(dashboardPage.getCurrencySelector()).containsText("ALL");
    }

    @Test
    @TmsLink("609")
    @Epic("Dashboard")
    @Feature("Refresh data")
    @Description("Correct merchant ID is sent to the server")
    public void testCheckMerchantId() {
        final String companyName = "Amazon";
        final String merchantTitle = "Amazon business unit 1";
        TestUtils.deleteCompany(getApiRequestContext(), companyName);
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), companyName);
        BusinessUnit businessUnit = TestUtils.createBusinessUnit(getApiRequestContext(), companyName, merchantTitle);

        DashboardPage dashboardPage = new DashboardPage(getPage())
                .refreshDashboard()
                .getSelectCompany().selectCompany(companyName)
                .getSelectBusinessUnit().selectBusinessUnit(merchantTitle);

        Allure.step("Verify: correct merchant ID is sent to the server");
        assertTrue(dashboardPage.getRequestData().contains(businessUnit.merchantId()));
    }

    @Test
    @TmsLink("600")
    @Epic("Dashboard")
    @Feature("Transaction summary")
    @Description("Correct transaction summary is displayed on Dashboard page")
    public void testTransactionSummary() {
        Pattern pattern = Pattern.compile("(INITIATED|SUCCESS|FAILED)EUR.*USD.*GBP.*");
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .getDateRangePicker().setDateRangeFields("01-05-2025", "31-05-2025")
                .clickRefreshDataButton()
                .clickCountButton();

        Allure.step("Verify: INITIATED main block contents");
        assertThat(dashboardPage.getInitiatedBlock()).containsText(pattern);

        Allure.step("Verify: SUCCESS main block contents");
        assertThat(dashboardPage.getSuccessBlock()).containsText(pattern);

        Allure.step("Verify: FAILED main block contents");
        assertThat(dashboardPage.getFailedBlock()).containsText(pattern);

        Allure.step("Verify: INITIATED count block contents");
        assertThat(dashboardPage.getLifecycleInitiatedBlock()).containsText(pattern);

        Allure.step("Verify: SUCCESS count block contents");
        assertThat(dashboardPage.getLifecycleSuccessBlock()).containsText(pattern);

        Allure.step("Verify: FAILED count block contents");
        assertThat(dashboardPage.getLifecycleFailedBlock()).containsText(pattern);

        dashboardPage
                .clickAmountButton();

        Allure.step("Verify: INITIATED amount block contents");
        assertThat(dashboardPage.getLifecycleInitiatedBlock()).containsText(pattern);

        Allure.step("Verify: SUCCESS amount block contents");
        assertThat(dashboardPage.getLifecycleSuccessBlock()).containsText(pattern);

        Allure.step("Verify: FAILED amount block contents");
        assertThat(dashboardPage.getLifecycleFailedBlock()).containsText(pattern);
    }
}
