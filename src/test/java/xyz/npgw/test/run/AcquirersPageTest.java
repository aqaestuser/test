package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AcquirersPageTest extends BaseTest {

    @Test
    @TmsLink("134")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("The visibility of elements in the 'Acquirers List' control panel")
    public void testVisibilityAcquirersListControlTab() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab();

        Allure.step("Verify: Add Acquirer Button is visible");
        assertThat(acquirersPage.getAddAcquirerButton()).isVisible();

        Allure.step("Verify: Select Acquirer label is visible");
        assertThat(acquirersPage.getSelectAcquirerLabel()).isVisible();

        Allure.step("Verify: Status label is visible");
        assertThat(acquirersPage.getStatusLabel()).isVisible();

        Allure.step("Verify: Reset Filter Button is visible");
        assertThat(acquirersPage.getResetFilterButton()).isVisible();

        Allure.step("Verify: Refresh data Button is visible");
        assertThat(acquirersPage.getRefreshDataButton()).isVisible();
    }

    @Test
    @TmsLink("157")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("The visibility of the 'Acquirers List' header, which contains a list of Acquirers.")
    public void testVisibilityHeaderAndAcquirersList() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab();

        Allure.step("Verify: 'Acquirer name' header is visible");
        assertThat(acquirersPage.getAcquirerNameHeader()).isVisible();

        Locator acquirersList = acquirersPage.getAcquirersList();

        Allure.step(String.format(
                "Verify: Acquirers list is visible and contains elements. INFO: (%d elements)", acquirersList.count()));
        assertThat(acquirersList.first()).isVisible();
        assertThat(acquirersList.last()).isVisible();
    }

    @Test
    @TmsLink("168")
    @Epic("System/Acquirers")
    @Feature("Select acquirer")
    @Description("Selecting the 'Select acquirer' field opens a dropdown with Acquirers list.")
    public void testSelectAcquirerDropdownFunctionality() {
        Locator dropdownAcquirerList = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickSelectAcquirerPlaceholder()
                .getSelectAcquirersDropdownItems();

        Allure.step(String.format(
                "Verify: Dropdown list is not empty. INFO: (%d elements)", dropdownAcquirerList.count()));
        assertThat(dropdownAcquirerList).not().hasCount(0);
    }

    @Test(enabled = false)
    @TmsLink("187")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("The 'Status' dropdown toggles and contains options All, Active, Inactive.")
    public void testOpenStatusDropdown() {
        Locator actualOptions = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAcquirerStatusPlaceholder()
                .getAcquirerStatusOptions();

        Allure.step("Verify: The 'Status' dropdown toggles and contains options All, Active, Inactive.");
        assertThat(actualOptions).hasText(new String[]{"All", "Active", "Inactive"});
    }

    @Ignore("fail after latest update")
    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("243")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("Filter acquirers by status.")
    public void testFilterAcquirersByStatus(String status) {
        Locator acquirersList = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAcquirerStatusPlaceholder()
                .selectAcquirerStatus(status)
                .getAcquirersList();

        Allure.step(String.format("Verify: The 'Acquirers' list shows only '%s' items after filtering.", status));
        for (Locator acquirer : acquirersList.all()) {
            assertThat(acquirer).containsText(status);
        }
    }

    @Test
    @TmsLink("268")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("Verify that re-selecting an already selected status keeps the selection unchanged.")
    public void testRetainStatusWhenReSelectingSameOption() {

        List<String> expectedOptions = List.of("All", "Active", "Inactive");

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab();

        Locator actualStatus = acquirersPage.getAcquirerStatusValue();

        for (String status : expectedOptions) {
            acquirersPage
                    .clickAcquirerStatusPlaceholder()
                    .selectAcquirerStatus(status);

            Allure.step("Verify placeholder matches expected value: " + status);
            assertThat(actualStatus).hasText(status);

            acquirersPage
                    .clickAcquirerStatusPlaceholder()
                    .selectAcquirerStatus(status);

            Allure.step("Verify again placeholder matches expected value: " + status);
            assertThat(actualStatus).hasText(status);
        }
    }

    @Test()
    @TmsLink("380")
    @Epic("System/Acquirers")
    @Feature("Rows Per Page")
    @Description("Verify the default 'Rows Per Page' value is 25 and the dropdown contains the correct options.")
    public void testVerifyRowsPerPageDropdownOptions() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab();

        Allure.step("Verify: The default 'Rows Per Page' value is set to 25");
        assertThat(acquirersPage.getRowsPerPage()).hasText("25");

        acquirersPage
                .clickRowsPerPageChevron();

        Allure.step("Verify: Dropdown is visible");
        assertThat(acquirersPage.getRowsPerPageDropdown()).isVisible();

        Allure.step("Verify: The dropdown contains all four options: 10, 25, 50, 100");
        assertThat(acquirersPage.getRowsPerPageOptions()).hasText(new String[]{"10", "25", "50", "100"});
    }
}
