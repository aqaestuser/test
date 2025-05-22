package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
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

    @Ignore
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
                .hasText(new String[]{"INITIATED", "FAILED"});

        Allure.step("Verify: currency legend labels are correctly displayed");
        assertThat(dashboardPage.getCurrencyLegendLabels())
                .hasText(new String[]{"USD", "EUR"});
    }

    @Ignore
    // TODO: Add business unit check when enabled
    @Test
    @TmsLink("577")
    @Epic("Dashboard")
    @Feature("Reset filter")
    @Description("'Reset filter' clears selected options to default")
    public void testResetFilter() {
        final String companyName = "framework";
        TestUtils.createCompanyIfNeeded(getApiRequestContext(), companyName);

        DashboardPage dashboardPage = new DashboardPage(getPage())
                .getSelectCompany().selectCompany(companyName)
                .clickCurrencySelector()
                .selectCurrency("EUR")
                .clickResetFilterButton();

        Allure.step("Verify: the selected company field is empty after reset");
        assertThat(dashboardPage.getSelectCompany().getSelectCompanyField()).hasValue("");

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

    @Ignore
    @Test
    @TmsLink("600")
    @Epic("Dashboard")
    @Feature("Transaction summary")
    @Description("Correct transaction summary is displayed on Dashboard page")
    public void testTransactionSummary() {
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .getDateRangePicker().setDateRangeFields("01-04-2025", "16-04-2025")
                .clickRefreshDataButton()
                .clickCountButton();

        Allure.step("Verify: INITIATED block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getInitiatedBlock())
                .containsText(Pattern.compile("INITIATEDUSD.*EUR.*"));

        Allure.step("Verify: PENDING block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getPendingBlock())
                .containsText(Pattern.compile("PENDINGUSD.*EUR.*"));

        Allure.step("Verify: SUCCESS block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getSuccessBlock())
                .containsText(Pattern.compile("SUCCESSUSD.*EUR.*"));

        Allure.step("Verify: FAILED block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getFailedBlock())
                .containsText(Pattern.compile("FAILEDUSD.*EUR.*"));

        Allure.step("Verify: INITIATED block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecycleInitiatedBlock())
                .containsText(Pattern.compile("INITIATEDUSD.*EUR.*"));

        Allure.step("Verify: PENDING block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecyclePendingBlock())
                .containsText(Pattern.compile("PENDINGUSD.*EUR.*"));

        Allure.step("Verify: SUCCESS block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecycleSuccessBlock())
                .containsText(Pattern.compile("SUCCESSUSD.*EUR.*"));

        Allure.step("Verify: FAILED block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecycleFailedBlock())
                .containsText(Pattern.compile("FAILEDUSD.*EUR.*"));

        dashboardPage
                .clickAmountButton();

        Allure.step("Verify: INITIATED block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecycleInitiatedBlock())
                .containsText(Pattern.compile("INITIATEDUSD.*EUR.*"));

        Allure.step("Verify: PENDING block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecyclePendingBlock())
                .containsText(Pattern.compile("PENDINGUSD.*EUR.*"));

        Allure.step("Verify: SUCCESS block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecycleSuccessBlock())
                .containsText(Pattern.compile("SUCCESSUSD.*EUR.*"));

        Allure.step("Verify: FAILED block contains 'USD' and 'EUR' currency");
        assertThat(dashboardPage.getLifecycleFailedBlock())
                .containsText(Pattern.compile("FAILEDUSD.*EUR.*"));
    }
}
