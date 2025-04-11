package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.systemadministration.AcquirersPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AcquirersPageTest extends BaseTest {

    @Test
    @TmsLink("134")
    @Epic("SA/Acquirers")
    @Feature("Acquirers list")
    @Description("The visibility of elements in the 'Acquirers List' control panel")
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
    @Description("The visibility of the 'Acquirers List' header, which contains a list of Acquirers.")
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
    @Description("Selecting the 'Select acquirer' field opens a dropdown with Acquirers list.")
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
    @Description("The 'Status' dropdown toggles and contains options All, Active, Inactive.")
    public void testOpenStatusDropdown() {
        Locator actualOptions = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
                .clickAcquirersTab()
                .clickAcquirerStatusPlaceholder()
                .getAcquirerStatusOptions();

        Allure.step("Verify: The 'Status' dropdown toggles and contains options All, Active, Inactive.");
        assertThat(actualOptions).hasText(new String[]{"All", "Active", "Inactive"});
    }

    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("243")
    @Epic("SA/Acquirers")
    @Feature("Status")
    @Description("Filter acquirers by status.")
    public void testFilterAcquirersByStatus(String status) {
        Locator acquirersList = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
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
    @TmsLink("239")
    @Epic("SA/Acquirers")
    @Feature("Edit acquirers")
    @Description("Verifies that all form field placeholders are set correctly")
    public void testVerifyPlaceholdersEditForm() {
        List<String> expectedPlaceholders = List.of(
                "Enter acquirer name", "Enter acquirer code", "Enter challenge URL", "Enter fingerprint URL",
                "Enter resource URL", "Enter notification queue", "Enter priority", "Enter acquirer config",
                "Search...", "Select timezone");

        List<String> actualPlaceholders = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
                .clickAcquirersTab()
                .clickEditButtonForAcquirer("acquirer1")
                .clearInputFields()
                .getPlaceholdersOrTextsFromFields();

        Allure.step("Verify placeholders match expected values for all fields");
        assertEquals(actualPlaceholders, expectedPlaceholders);
    }

    @Test
    @TmsLink("268")
    @Epic("SA/Acquirers")
    @Feature("Status")
    @Description("Verify that re-selecting an already selected status keeps the selection unchanged.")
    public void testRetainStatusWhenReSelectingSameOption() {

        List<String> expectedOptions = List.of("All", "Active", "Inactive");

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader()
                .clickSystemAdministrationLink()
                .getSystemAdministrationMenuComponent()
                .clickAcquirersTab();

        for (String status : expectedOptions) {
            Locator actualStatus = acquirersPage
                    .clickAcquirerStatusPlaceholder()
                    .selectAcquirerStatus(status)
                    .getAcquirerStatusPlaceholder();

            Allure.step("Verify placeholder matches expected value: " + status);
            assertThat(actualStatus).hasText(status);

            acquirersPage.clickAcquirerStatusPlaceholder()
                    .selectAcquirerStatus(status)
                    .getAcquirerStatusPlaceholder();

            Allure.step("Verify again placeholder matches expected value: " + status);
            assertThat(actualStatus).hasText(status);
        }
    }
}
