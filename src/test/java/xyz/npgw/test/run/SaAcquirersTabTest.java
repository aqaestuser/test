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
                .clickSystemAdministrationLink()
                .clickAcquirersButton();

        Allure.step("Verify: Add Acquirer Button is visible");
        assertThat(saAcquirersTab.getAddAcquirerButton()).isVisible();

        Allure.step("Verify: Select Acquirer label is visible");
        assertThat(saAcquirersTab.getSelectAcquirerLabel()).isVisible();

        Allure.step("Verify: Status label is visible");
        assertThat(saAcquirersTab.getStatusLabel()).isVisible();

        Allure.step("Verify: Reset Filter Button is visible");
        assertThat(saAcquirersTab.getResetFilterButton()).isVisible();

        Allure.step("Verify: Apply Filter Button is visible");
        assertThat(saAcquirersTab.getApplyFilterButton()).isVisible();
    }
}
