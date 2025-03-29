package xyz.npgw.test.run;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.SaAcquirersTab;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SaAcquirersTabTest extends BaseTest {

    @Test
    @TmsLink("134")
    @Epic("SA/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify: The visibility of elements in the 'Acquirers List' control panel")
    public void testVisibilityAcquirersListControlTab() {
        SaAcquirersTab saAcquirersTab = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .clickAcquirersButton();

        Allure.step("Verify: Add Acquirer Img is visible");
        assertThat(saAcquirersTab.getAddAcquirerImg()).isVisible();

        Allure.step("Verify: Select Acquirer placeholder is visible");
        assertThat(saAcquirersTab.getSelectAcquirerPlaceholder()).isVisible();

        Allure.step("Verify: Status placeholder is visible");
        assertThat(saAcquirersTab.getStatusPlaceholder()).isVisible();

        Allure.step("Verify: Reset Filter Img is visible");
        assertThat(saAcquirersTab.getResetFilterImg()).isVisible();

        Allure.step("Verify: Apply Filter Img is visible");
        assertThat(saAcquirersTab.getApplyFilterImg()).isVisible();
    }
}
