package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.BoundingBox;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.SystemConfig;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.common.TableComponent;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static xyz.npgw.test.common.util.TestUtils.createAcquirer;
import static xyz.npgw.test.common.util.TestUtils.deleteAcquirer;
import static xyz.npgw.test.common.util.TestUtils.getAcquirer;

public class AcquirersPageTest extends BaseTest {

    private static final List<String> COLUMNS_HEADERS = List.of(
            "Acquirer name",
            "Acquirer code",
            "Currencies",
            "Acquirer config",
            "System config",
            "Status",
            "Actions");

    String[] rowsPerPageOptions = new String[]{"10", "25", "50", "100"};

    @Test
    @TmsLink("134")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("The visibility of elements in the 'Acquirers List' control panel")
    public void testVisibilityAcquirersListControlTab() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        Allure.step("Verify: Add Acquirer Button is visible");
        assertThat(acquirersPage.getAddAcquirerButton()).isVisible();

        Allure.step("Verify: Select Acquirer label is visible");
        assertThat(acquirersPage
                .getSelectAcquirer().getSelectAcquirerField())
                .isVisible();

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
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

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
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().clickSelectAcquirerPlaceholder()
                .getSelectAcquirer().getSelectAcquirersDropdownItems();

        Allure.step(String.format(
                "Verify: Dropdown list is not empty. INFO: (%d elements)", dropdownAcquirerList.count()));
        assertThat(dropdownAcquirerList).not().hasCount(0);
    }

    @Test
    @TmsLink("187")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("The 'Status' dropdown toggles and contains options All, Active, Inactive.")
    public void testOpenStatusDropdown() {
        Locator actualOptions = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAcquirerStatusPlaceholder()
                .getAcquirerStatusOptions();

        Allure.step("Verify: The 'Status' dropdown toggles and contains options All, Active, Inactive.");
        assertThat(actualOptions).hasText(new String[]{"All", "Active", "Inactive"});
    }

    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("243")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("Filter acquirers by status.")
    public void testFilterAcquirersByStatus(String status) {
        List<Locator> statuses = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAcquirerStatusPlaceholder()
                .selectAcquirerStatus(status)
                .getAcquirersStatus();

        Allure.step(String.format("Verify: The 'Acquirers' list shows only '%s' items after filtering.", status));
        for (Locator actualStatus : statuses) {
            assertThat(actualStatus).containsText(status);
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
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

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
    public void testRowsPerPageDropdownOptions() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        Allure.step("Verify: The default 'Rows Per Page' value is set to 25");
        assertThat(acquirersPage.getRowsPerPage()).hasText("25");

        acquirersPage
                .clickRowsPerPageChevron();

        Allure.step("Verify: Dropdown is visible");
        assertThat(acquirersPage.getRowsPerPageDropdown()).isVisible();

        Allure.step("Verify: The dropdown contains all four options: 10, 25, 50, 100");
        assertThat(acquirersPage.getRowsPerPageOptions()).hasText(rowsPerPageOptions);
    }

    @Test
    @TmsLink("382")
    @Epic("System/Acquirers")
    @Feature("Rows Per Page")
    @Description("Verify Selecting 'Rows Per Page' Option Updates the Field Value.")
    public void testSelectingRowsPerPageOptionUpdatesFieldValue() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        for (String option : rowsPerPageOptions) {
            acquirersPage
                    .clickRowsPerPageChevron()
                    .selectRowsPerPageOption(option);

            Allure.step(String.format("Verify: The Rows Per Page' value is set to '%s'", option));
            assertThat(acquirersPage.getRowsPerPage()).hasText(option);
        }
    }

    @Test
    @TmsLink("385")
    @Epic("System/Acquirers")
    @Feature("Rows Per Page")
    @Description("Verify that selecting a 'Rows Per Page' option displays the correct number of rows in the table.")
    public void testRowsPerPageSelectionDisplaysCorrectNumberOfRows() {
        List<Integer> totalRows = new ArrayList<>();

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        for (String option : rowsPerPageOptions) {
            acquirersPage
                    .clickRowsPerPageChevron()
                    .selectRowsPerPageOption(option);

            int rowsSum = 0;

            do {
                int actualRowCount = acquirersPage.getTable().getTableRows().count();
                rowsSum += actualRowCount;

                Allure.step(String.format(
                        "Verify: The table contains '%s' rows less than or equal to '%s'", actualRowCount, option));
                Assert.assertTrue(actualRowCount <= Integer.parseInt(option));

            } while (!acquirersPage.isLastPage() && acquirersPage.clickNextPage() != null);

            totalRows.add(rowsSum);
        }

        Assert.assertEquals(totalRows.stream().distinct().count(), 1,
                "Total rows should be the same for all 'Rows Per Page' options");
    }

    @Test
    @TmsLink("432")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify Acquirers table contains correct column headers")
    public void testDisplayCorrectColumnHeadersInAcquirersTable() {
        List<String> acquirerTableHeaders = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getTable()
                .getColumnHeadersText();


        Allure.step("Verify: The Acquirer table contains correct column headers");
        Assert.assertEquals(acquirerTableHeaders, COLUMNS_HEADERS, "Mismatch in Acquirer table columns");
    }

    @Test
    @TmsLink("463")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description(
            "Verifies that the Acquirers table displays correct data for the selected acquirer and shows relevant "
                    + "action buttons.")
    public void testDisplaySingleRowWhenAcquirerIsSelected() {
        Acquirer acquirer = new Acquirer(
                "NGenius",
                "default",
                new SystemConfig(),
                "Acquirer 11.002.01",
                new String[]{"USD", "EUR"},
                true);

        deleteAcquirer(getApiRequestContext(), acquirer.acquirerName());
        createAcquirer(getApiRequestContext(), acquirer);

        Map<String, String> expectedColumnValues = Map.of(
                COLUMNS_HEADERS.get(0), acquirer.acquirerName(),
                COLUMNS_HEADERS.get(1), acquirer.acquirerCode(),
                COLUMNS_HEADERS.get(2), String.join(", ", acquirer.currencyList()),
                COLUMNS_HEADERS.get(3), acquirer.acquirerConfig(),
                COLUMNS_HEADERS.get(4), String.join("\n",
                        acquirer.systemConfig().challengeUrl(),
                        acquirer.systemConfig().fingerprintUrl(),
                        acquirer.systemConfig().resourceUrl(),
                        acquirer.systemConfig().notificationQueue()),
                COLUMNS_HEADERS.get(5), acquirer.isActive() ? "Active" : "Inactive"
        );

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().typeAcquirerNameToSelectAcquirerInputField(acquirer.acquirerName())
                .getSelectAcquirer().clickAcquirerInDropdown(acquirer.acquirerName());

        TableComponent table = acquirersPage.getTable();

        Locator row = table.getTableRows();

        Allure.step("Verify: List of acquirers has only 1 row in the table");
        assertThat(row).hasCount(1);

        for (int i = 0; i < COLUMNS_HEADERS.size() - 1; i++) {
            String header = COLUMNS_HEADERS.get(i);
            String expected = expectedColumnValues.get(header);
            String actual = table.getColumnValues(header).get(0);

            Allure.step(String.format("Verify that displayed '%s' is: %s", header, expected));
            Assert.assertEquals(
                    actual,
                    expected,
                    String.format("%s in the table does not match the expected value", header)
            );
        }

        Allure.step("Verify: Edit button is visible");
        assertThat(acquirersPage.getEditAcquirerButton(row)).isVisible();

        Allure.step("Verify: Activate/Deactivate acquirer button is visible");
        assertThat(acquirersPage.getChangeAcquirerActivityButton(row)).isVisible();

        Allure.step("Verify: Pagination shows only one page labeled '1'");
        assertThat(acquirersPage.getPaginationItems()).isVisible();
        assertThat(acquirersPage.getPaginationItems()).hasText("1");
    }

    @Test(expectedExceptions = {AssertionError.class})
    @TmsLink("487")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("Verifies that table column headers are displayed correctly on each page when navigating"
            + " through paginated results.")
    public void testColumnHeadersDisplayCorrectlyOnAllPages() {
        Allure.step("Test disabled / Known bug: The column is not visible on the screen!");

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        for (String option : rowsPerPageOptions) {
            acquirersPage
                    .clickRowsPerPageChevron()
                    .selectRowsPerPageOption(option);
            do {
                String activePage = acquirersPage.getActivePage().innerText();

                Allure.step(
                        String.format("Verify: All table column headers are on page '%s' with '%s' pagination",
                                activePage, option));
                Assert.assertEquals(
                        acquirersPage.getTable().getTableColumnHeader().allTextContents(),
                        COLUMNS_HEADERS,
                        String.format(
                                "Column headers do not match expected headers on page '%s' with '%s' pagination!",
                                activePage, option));

                BoundingBox table = acquirersPage.getTable().getTableHeader().boundingBox();
                double pageWidth = getPage().viewportSize().width;

                Assert.assertTrue(table.x >= 0 && (table.x + table.width) <= pageWidth, String.format(
                        "The header is not fully visible within the viewport on page '%s' with '%s' pagination!",
                        activePage, option));

            } while (!acquirersPage.isLastPage() && acquirersPage.clickNextPage() != null);
        }
    }


    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("557")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify that Acquirer with status 'Active/Inactive' is displayed correctly in the list")
    public void testAcquirerStatusDisplaysCorrectly(String status) {
        String acquirerName = "ZAcquirer status check";
        boolean isFound = false;

        if (getAcquirer(getApiRequestContext(), acquirerName)) {
            deleteAcquirer(getApiRequestContext(), acquirerName);
        }

        Acquirer acquirer = new Acquirer(
                "",
                "Acquirer Config",
                new SystemConfig(
                        "https://challenge.example.com",
                        "https://fingerprint.example.com",
                        "https://resource.example.com",
                        "notification-queue"),
                acquirerName,
                new String[]{"USD"},
                status.equals("Active")
        );

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerName(acquirerName)
                .fillAcquirerForm(acquirer)
                .clickCreateButton();

        do {
            if (acquirersPage.getTable().getRowByPrimaryColumn(acquirerName).count() > 0) {
                isFound = true;
                break;
            }
        } while (!acquirersPage.isLastPage() && acquirersPage.clickNextPage() != null);

        Allure.step(String.format(
                "Verify: After creation '%s' it shows up. Page: %s",
                acquirerName,
                acquirersPage.getActivePage() != null
                        ? acquirersPage.getActivePage().innerText()
                        : "unknown")
        );
        Assert.assertTrue(
                isFound, "Acquirer with name '%s' was not found in the table");

        Allure.step(String.format("Verify: '%s' has status %s", acquirerName, status)
        );
        assertThat(acquirersPage.getTable().getColumnCellsByRowText("Status", acquirerName)).hasText(status);
    }
}
