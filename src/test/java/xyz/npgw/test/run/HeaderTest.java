package xyz.npgw.test.run;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.component.HeaderComponent;
import xyz.npgw.test.testdata.Constants;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HeaderTest extends BaseTest {

    @Test
    @TmsLink("209")
    @Epic("Header")
    @Feature("Logo")
    @Description("Check that the header contains Logo 'NPGW' ")
    public void testLogoText() {
        HeaderComponent logo = new HeaderComponent(getPage());

        Allure.step("Verify: Logo contains text 'NPGW'");
        assertThat(logo.getPage()).hasTitle(Constants.HEADER_TITLE);
    }

    @Test
    @TmsLink("210")
    @Epic("Header")
    @Feature("Menu item Dashboard")
    @Description("Check that the Dashboard Button is visible")
    public void testDashboardButton() {
        DashboardPage dashboardButton = new DashboardPage(getPage())
                .getHeader()
                .clickDashboardLink();

        Allure.step("Verify: Dashboard Page URL");
        assertThat(dashboardButton.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);

        Allure.step("Verify: Dashboard Button is visible");
        assertThat(dashboardButton.getDashboardButton()).isVisible();
    }

    @Test
    @TmsLink("211")
    @Epic("Header")
    @Feature("Menu item Transactions")
    @Description("Check that the Transactions Button is visible")

    public void testTransactions() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transactionsPage.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);

        Allure.step("Verify: Transactions Button is visible");
        assertThat(transactionsPage.getTransactionsButton()).isVisible();
    }
}
