package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.ReportsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ReportsPageTest extends BaseTest {

    @Test
    @TmsLink("153")
    @Epic("Reports")
    @Feature("Navigation")
    @Description("User navigate to 'Reports page' after clicking on 'Reports' link on the header")
    public void testNavigateToReportsPage() {
        ReportsPage reportsPage = new DashboardPage(getPage())
                .getHeader()
                .clickReportsLink();

        Allure.step("Verify: Reports Page URL");
        assertThat(reportsPage.getPage()).hasURL(Constants.REPORTS_PAGE_URL);

        Allure.step("Verify: Reports Page Title");
        assertThat(reportsPage.getPage()).hasTitle(Constants.REPORTS_URL_TITLE);
    }

    @Test
    public void testReportsTableContent() {
//        Locator tableRows =
                new DashboardPage(getPage())
                .getHeader()
                .clickUserProfileButton()
                .clickUserProfileLogOutButton()
                        .checkRememberMeCheckbox();
                getPage().pause();
//                .
//                .clickReportsLink()
//                .getBaseTable()
//                .getRowsWithoutHeader();

//        Allure.step("Verify: Reports Page URL");
//        assertThat(tableRows).hasCount(1);
//
//        Allure.step("Verify: Reports Page Title");
//        assertThat(tableRows).hasText("Example Merchant report2025-02-07 15:00:127.8 kBDownload");
    }
}
