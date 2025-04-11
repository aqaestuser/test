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
import xyz.npgw.test.page.TransactionsPage;
import xyz.npgw.test.page.component.HeaderComponent;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HeaderTest extends BaseTest {

    @Test
    @TmsLink("209")
    @Epic("Header")
    @Feature("Logo")
    @Description("Check that Logo in header contains text 'NPGW' and image")
    public void testLogoContainsTextAndImage() {
        HeaderComponent headerComponent = new DashboardPage(getPage()).getHeader();

        Allure.step("Verify: Logo contains text 'NPGW'");
        assertThat(headerComponent.getLogo()).hasText(Constants.LOGO_TEXT);

        Allure.step("Verify: Logo contains image");
        Assert.assertTrue(headerComponent.getImg().isVisible(), "Image inside logo should be visible");
        Assert.assertNotNull(headerComponent.getImg().getAttribute("src"), "Image should have a 'src' attribute");
    }

    @Test
    @TmsLink("211")
    @Epic("Header")
    @Feature("Transactions menu item")
    @Description("Check after clicking on Transactions user redirected to Transactions page")
    public void testTransactionsLink() {
        TransactionsPage transactionsPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink();

        Allure.step("Verify: Transactions Page URL");
        assertThat(transactionsPage.getPage()).hasURL(Constants.TRANSACTIONS_PAGE_URL);
    }

    @Test
    @TmsLink("242")
    @Epic("Header")
    @Feature("Logo")
    @Description("Check that click on Logo return user to the dashboard page from other pages")
    public void testReturtDashbordPageClickLogo() {
        DashboardPage dashboardPage = new DashboardPage(getPage())
                .getHeader()
                .clickTransactionsLink()
                .getHeader()
                .clickLogoButton();

        Allure.step("Verify: Transactions Page URL");
        assertThat(dashboardPage.getPage()).hasURL(Constants.DASHBOARD_PAGE_URL);
    }
}
