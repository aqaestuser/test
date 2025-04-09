package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.systemadministration.AcquirersPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AcquirersPageTest extends BaseTest {

    @Test
    @TmsLink("134")
    @Epic("SA/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify: The visibility of elements in the 'Acquirers List' control panel")
    public void testVisibilityAcquirersListControlTab() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
                .clickAcquirersTab();

        Allure.step("Verify: Add Acquirer Button is visible");
        assertThat(acquirersPage.getAddAcquirerButton()).isVisible();

        Allure.step("Verify: Select Acquirer label is visible");
        assertThat(acquirersPage.getSelectAcquirerLabel()).isVisible();

        Allure.step("Verify: Status label is visible");
        assertThat(acquirersPage.getStatusLabel()).isVisible();

        Allure.step("Verify: Reset Filter Button is visible");
        assertThat(acquirersPage.getResetFilterButton()).isVisible();

        Allure.step("Verify: Apply Filter Button is visible");
        assertThat(acquirersPage.getApplyFilterButton()).isVisible();
    }

    @Test
    @TmsLink("157")
    @Epic("SA/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify: The visibility of the 'Acquirers List' header, which contains a list of Acquirers.")
    public void testVisibilityHeaderAndAcquirersList() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
                .clickAcquirersTab();

        Allure.step("Verify: Acquirers list header is visible");
        assertThat(acquirersPage.getAcquirersListHeader()).isVisible();

        Locator acquirersList = acquirersPage.getAcquirersList();

        Allure.step(String.format(
                "Verify: Acquirers list is visible and contains elements. INFO: (%d elements)", acquirersList.count()));
        assertThat(acquirersList.first()).isVisible();
        assertThat(acquirersList.last()).isVisible();
    }

    @Test
    @TmsLink("168")
    @Epic("SA/Acquirers")
    @Feature("Select acquirer")
    @Description("Verify: Selecting the 'Select acquirer' field opens a dropdown with Acquirers list.")
    public void testSelectAcquirerDropdownFunctionality() {
        Locator dropdownAcquirerList = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
                .clickAcquirersTab()
                .clickSelectAcquirerPlaceholder()
                .getSelectAcquirersDropdownItems();

        Allure.step(String.format(
                "Verify: Dropdown list is not empty. INFO: (%d elements)", dropdownAcquirerList.count()));
        assertThat(dropdownAcquirerList).not().hasCount(0);
    }

    @Test
    @TmsLink("187")
    @Epic("SA/Acquirers")
    @Feature("Status")
    @Description("Verify: The 'Status' dropdown toggles and contains options All, Active, Inactive.")
    public void testOpenStatusDropdown() {
        Locator actualOptions = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
                .clickAcquirersTab()
                .clickAcquirerStatusPlaceholder()
                .getAcquirerStatusOptions();

        assertThat(actualOptions).hasText(new String[] {"All", "Active", "Inactive"});
    }
}
