package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
                .hasText(new String[]{"INITIATED", "FAILED"});

        Allure.step("Verify: currency legend labels are correctly displayed");
        assertThat(dashboardPage.getCurrencyLegendLabels())
                .hasText(new String[]{"USD", "EUR"});
    }
}
