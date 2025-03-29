package xyz.npgw.test.run;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.Constants;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.LoginPage;

public class HeaderTest extends BaseTest {

    @Test
    @TmsLink("150")
    @Epic("Navigation")
    @Feature("Header Links")
    @Description("User navigates to 'Dashboard' from Header")
    public void testNavigateToAllTabs() {
        new LoginPage(getPage())
                .login(Constants.USER_EMAIL, Constants.USER_PASSWORD)
                .clickDashboardLink()
                .clickTransactionsLink()
                .clickReportsLink()
                .clickSystemAdministrationLink()
                .clickLogOutButton();
    }
}
