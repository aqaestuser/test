package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.BoundingBox;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import lombok.Getter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.SystemConfig;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.common.table.AcquirersTableComponent;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;

public class AcquirersPageTest extends BaseTest {

    private static final List<String> COLUMNS_HEADERS = List.of(
            "Entity name",
            "Display name",
            "Acquirer code",
            "MID",
            "MCC",
            "Currencies",
            "Acquirer config",
            "System config",
            "Status",
            "Actions");

    protected static final Acquirer ACQUIRER = new Acquirer(
            "display name",
            "acquirer mid",
            "NGenius",
            "default",
            new Currency[]{Currency.USD, Currency.EUR},
            new SystemConfig(),
            true,
            "%s acquirer 11.002.01".formatted(RUN_ID),
            "4321");

    private static final Acquirer CHANGE_STATE_ACQUIRER = new Acquirer(
            "display name",
            "acquirer mid",
            "NGenius",
            "default",
            new Currency[]{Currency.USD, Currency.EUR},
            new SystemConfig(),
            true,
            "%s acquirer activate and deactivate".formatted(RUN_ID),
            "1234");

    private static final String ACTIVE_ACQUIRER_NAME = "%s active acquirer".formatted(RUN_ID);
    private static final String INACTIVE_ACQUIRER_NAME = "%s inactive acquirer".formatted(RUN_ID);

    String[] rowsPerPageOptions = new String[]{"10", "25", "50", "100"};

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createAcquirer(getApiRequestContext(), ACQUIRER);
        TestUtils.createAcquirer(getApiRequestContext(), CHANGE_STATE_ACQUIRER);
    }

    @Test
    @TmsLink("134")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("The visibility of elements in the 'Acquirers List' control panel")
    public void testVisibilityAcquirersListControlTab() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        Allure.step("Verify: Add Acquirer Button is visible");
        assertThat(acquirersPage.getAddAcquirerButton()).isVisible();

        Allure.step("Verify: Select Acquirer label is visible");
        assertThat(acquirersPage.getSelectAcquirer().getSelectAcquirerField()).isVisible();

        Allure.step("Verify: Status label is visible");
        assertThat(acquirersPage.getSelectStatus().getStatusSelector()).isVisible();

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
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        Allure.step("Verify: Table column header is visible");
        assertThat(acquirersPage.getTable().getColumnHeader("Entity name")).isVisible();

        List<Locator> acquirersList = acquirersPage.getTable().getColumnCells("Entity name");

//        TODO refactor this
        Allure.step("Verify: Acquirers list is visible and contains elements");
        assertThat(acquirersList.get(0)).isVisible();
        assertThat(acquirersList.get(acquirersList.size() - 1)).isVisible();
    }

    @Test
    @TmsLink("168")
    @Epic("System/Acquirers")
    @Feature("Select acquirer")
    @Description("Selecting the 'Select acquirer' field opens a dropdown with Acquirers list.")
    public void testSelectAcquirerDropdownFunctionality() {
        Locator dropdownAcquirerList = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().clickSelectAcquirerField()
                .getSelectAcquirer().getSelectAcquirersDropdownItems();

        Allure.step("Verify: Dropdown list is not empty");
        assertThat(dropdownAcquirerList).not().hasCount(0);
    }

    @Test
    @TmsLink("187")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("The 'Status' dropdown toggles and contains options All, Active, Inactive.")
    public void testOpenStatusDropdown() {
        Locator actualOptions = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectStatus().clickSelector()
                .getSelectStatus().getStatusOptions();

        Allure.step("Verify: The 'Status' dropdown toggles and contains options");
        assertThat(actualOptions).hasText(new String[]{"All", "Active", "Inactive"});
    }

    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("243")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("Filter acquirers by status.")
    public void testFilterAcquirersByStatus(String status) {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectStatus().select(status);

        Allure.step(String.format("Verify: The 'Acquirers' list shows only '%s' items after filtering.", status));
        assertTrue(acquirersPage.getTable().getColumnValuesFromAllPages("Status", Function.identity())
                .stream().allMatch(value -> value.equals(status)));
    }

    @Test
    @TmsLink("268")
    @Epic("System/Acquirers")
    @Feature("Status")
    @Description("Verify that re-selecting an already selected status keeps the selection unchanged.")
    public void testRetainStatusWhenReSelectingSameOption() {
        List<String> expectedOptions = List.of("All", "Active", "Inactive");

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        Locator actualStatus = acquirersPage.getSelectStatus().getStatusValue();

        for (String status : expectedOptions) {
            acquirersPage.getSelectStatus().select(status);

            Allure.step("Verify placeholder matches expected value: " + status);
            assertThat(actualStatus).hasText(status);

            acquirersPage.getSelectStatus().select(status);

            Allure.step("Verify again placeholder matches expected value: " + status);
            assertThat(actualStatus).hasText(status);
        }
    }

    @Test
    @TmsLink("380")
    @Epic("System/Acquirers")
    @Feature("Rows Per Page")
    @Description("Verify the default 'Rows Per Page' value is 25 and the dropdown contains the correct options.")
    public void testRowsPerPageDropdownOptions() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        Allure.step("Verify: The default 'Rows Per Page' value is set to 25");
        assertThat(acquirersPage.getTable().getRowsPerPage()).hasText("25");

        acquirersPage.getTable().clickRowsPerPageChevron();

        Allure.step("Verify: Dropdown is visible");
        assertThat(acquirersPage.getTable().getRowsPerPageDropdown()).isVisible();

        Allure.step("Verify: The dropdown contains all four options: 10, 25, 50, 100");
        assertThat(acquirersPage.getTable().getRowsPerPageOptions()).hasText(rowsPerPageOptions);
    }

    @Test
    @TmsLink("382")
    @Epic("System/Acquirers")
    @Feature("Rows Per Page")
    @Description("Verify Selecting 'Rows Per Page' Option Updates the Field Value.")
    public void testSelectingRowsPerPageOptionUpdatesFieldValue() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        for (String option : rowsPerPageOptions) {
            acquirersPage.getTable().selectRowsPerPageOption(option);

            Allure.step(String.format("Verify: The Rows Per Page' value is set to '%s'", option));
            assertThat(acquirersPage.getTable().getRowsPerPage()).hasText(option);
        }
    }

    @Test
    @TmsLink("385")
    @Epic("System/Acquirers")
    @Feature("Rows Per Page")
    @Description("Verify that selecting a 'Rows Per Page' option displays the correct number of rows in the table.")
    public void testRowsPerPageSelectionDisplaysCorrectNumberOfRows() {
        List<Integer> totalRowsForDifferentPaginations = new ArrayList<>();

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        for (String option : rowsPerPageOptions) {
            acquirersPage.getTable().selectRowsPerPageOption(option);

            List<Integer> rowsCountPerPage = acquirersPage.getTable().getRowCountsPerPage();
            int rowsSum = acquirersPage.getTable().countAllRows(rowsCountPerPage);
            boolean allValid = rowsCountPerPage.stream().allMatch(count -> count <= Integer.parseInt(option));

            Allure.step(String.format("Verify: The table contains rows less than or equal to '%s' per page", option));
            assertTrue(allValid, "Not all row counts are less than or equal to " + option);

            totalRowsForDifferentPaginations.add(rowsSum);
        }

        Assert.assertEquals(totalRowsForDifferentPaginations.stream().distinct().count(), 1,
                "Total rows should be the same for all 'Rows Per Page' options");
    }

    @Ignore
    @Test
    @TmsLink("432")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify Acquirers table contains correct column headers")
    public void testDisplayCorrectColumnHeadersInAcquirersTable() {
        List<String> acquirerTableHeaders = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getTable().getColumnHeadersText();

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
        Map<String, String> expectedColumnValues = Map.of(
                COLUMNS_HEADERS.get(0), ACQUIRER.acquirerName(),
                COLUMNS_HEADERS.get(1), ACQUIRER.acquirerDisplayName(),
                COLUMNS_HEADERS.get(2), ACQUIRER.acquirerCode(),
                COLUMNS_HEADERS.get(3), ACQUIRER.acquirerMid(),
                COLUMNS_HEADERS.get(4), ACQUIRER.acquirerMidMcc(),

                COLUMNS_HEADERS.get(5), String.join(", ", Arrays.stream(ACQUIRER.currencyList())
                        .map(Enum::name)
                        .toList()),
                COLUMNS_HEADERS.get(6), ACQUIRER.acquirerConfig(),
                COLUMNS_HEADERS.get(7), String.join("\n",
                        "Challenge URL\n" + ACQUIRER.systemConfig().challengeUrl(),
                        "Fingerprint URL\n" + ACQUIRER.systemConfig().fingerprintUrl(),
                        "Resource URL\n" + ACQUIRER.systemConfig().resourceUrl(),
                        "Notification queue\n" + ACQUIRER.systemConfig().notificationQueue()),
                COLUMNS_HEADERS.get(8), ACQUIRER.isActive() ? "Active" : "Inactive"
        );

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().typeName(ACQUIRER.acquirerName())
                .getSelectAcquirer().clickAcquirerInDropdown(ACQUIRER.acquirerName());

        Locator row = acquirersPage.getTable().getRows();

        Allure.step("Verify: List of acquirers has only 1 row in the table");
        assertThat(row).hasCount(1);

//        assertThat(acquirersPage.getTable().getFirstRowCell("Acquirer name"))
//                .hasText(ACQUIRER.acquirerName());
//
//        assertThat(acquirersPage.getTable().getFirstRowCell("Acquirer display name"))
//                .hasText(ACQUIRER.acquirerDisplayName());

        for (int i = 0; i < COLUMNS_HEADERS.size() - 1; i++) {
            String header = COLUMNS_HEADERS.get(i);
            String expected = expectedColumnValues.get(header);
            String actual = acquirersPage.getTable().getColumnValues(header).get(0);

            Allure.step(String.format("Verify that displayed '%s' is: %s", header, expected));
            Assert.assertEquals(
                    actual,
                    expected,
                    String.format("%s in the table does not match the expected value", header)
            );
        }

        Allure.step("Verify: Edit button is visible");
        assertThat(acquirersPage.getTable().getEditAcquirerButton(row)).isVisible();

        Allure.step("Verify: Activate/Deactivate acquirer button is visible");
        assertThat(acquirersPage.getTable().getChangeActivityButton(row)).isVisible();

        Allure.step("Verify: Pagination shows only one page labeled '1'");
        assertThat(acquirersPage.getTable().getPaginationItems()).isVisible();
        assertThat(acquirersPage.getTable().getPaginationItems()).hasText("1");
    }

    @Ignore
    @Test
    @TmsLink("487")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("Verifies that table column headers are displayed correctly on each page when navigating"
            + " through paginated results.")
    public void testColumnHeadersDisplayCorrectlyOnAllPages() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        AcquirersTableComponent table = acquirersPage.getTable();
        double pageWidth = getPage().viewportSize().width;

        for (String option : rowsPerPageOptions) {
            table.forEachPage(option, activePage -> {
                BoundingBox box = table.getHeadersRow().boundingBox();

                Allure.step(String.format("Verify headers on page '%s' with '%s' pagination", activePage, option));
                Assert.assertEquals(table.getColumnHeader().allTextContents(), COLUMNS_HEADERS,
                        String.format("Headers mismatch on page %s with '%s' rows per page", activePage, option));

                assertTrue(box.x >= 0, String.format(
                        "Headers x-position must be within viewport on page '%s' with '%s'/page", activePage, option));

                assertTrue((box.x + box.width) <= pageWidth, String.format(
                        "Headers right edge must be within viewport on page '%s' with '%s'/page", activePage, option));

            });
        }
    }

    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("557")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify Acquirer status 'Active/Inactive' is displayed in column 'Status'")
    public void testVerifyAcquirerStatus(String status) {
        String acquirerName = status.equals("Active") ? ACTIVE_ACQUIRER_NAME : INACTIVE_ACQUIRER_NAME;
        SystemConfig systemConfig = new SystemConfig();

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerNameField(acquirerName)
                .fillChallengeUrlField(systemConfig.challengeUrl())
                .fillFingerprintUrlField(systemConfig.fingerprintUrl())
                .fillResourceUrlField(systemConfig.resourceUrl())
                .clickStatusRadiobutton(status)
                .clickCheckboxCurrency("USD")
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify: Acquirer status");
        assertThat(acquirersPage.getTable().getCell(acquirerName, "Status")).hasText(status);
    }

    @Test
    @TmsLink("588")
    @Epic("System/Acquirers")
    @Feature("Acquirers list")
    @Description("Verify Acquirer can be activated and deactivated from the table")
    public void testAcquirerCanBeActivatedAndDeactivated() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().typeName(CHANGE_STATE_ACQUIRER.acquirerName())
                .getSelectAcquirer().clickAcquirerInDropdown(CHANGE_STATE_ACQUIRER.acquirerName());

        Locator row = acquirersPage.getTable().getRows();

        acquirersPage
                .getTable().clickChangeActivityButton(row)
                .clickDeactivateButton();

        Allure.step("Verify: Successful message");
        assertThat(acquirersPage.getAlert().getMessage())
                .hasText("SUCCESSAcquirer was deactivated successfully");

        acquirersPage
                .getAlert().clickCloseButton();

        Allure.step("Verify: Acquirer status changed to Inactive");
        assertThat(acquirersPage.getTable().getCell(CHANGE_STATE_ACQUIRER.acquirerName(), "Status"))
                .hasText("Inactive");

        acquirersPage
                .getTable().clickChangeActivityButton(row)
                .clickActivateButton();

        Allure.step("Verify: Successful message");
        assertThat(acquirersPage.getAlert().getMessage())
                .hasText("SUCCESSAcquirer was activated successfully");

        acquirersPage
                .getAlert().clickCloseButton();

        Allure.step("Verify: Acquirer status changed back to Active");
        assertThat(acquirersPage.getTable().getCell(CHANGE_STATE_ACQUIRER.acquirerName(), "Status"))
                .hasText("Active");
    }

    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("708")
    @Epic("System/Acquirers")
    @Feature("Reset")
    @Description("'Reset' button clears selected filter values and resets them to default.")
    public void testResetFilter(String status) {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().selectAcquirer(ACQUIRER.acquirerName())
                .getSelectStatus().select(status)
                .clickResetFilterButton();

        Allure.step("Verify: the selected acquirer filter is cleared");
        assertThat(acquirersPage.getSelectAcquirer().getSelectAcquirerField()).isEmpty();

        Allure.step("Verify: the status filter is reset to 'All'");
        assertThat(acquirersPage.getSelectStatus().getStatusValue()).hasText("All");
    }

    @Ignore
    @Test(priority = 1)
    @TmsLink("726")
    @Epic("System/Acquirers")
    @Feature("Delete acquirer")
    @Description("Verify that an acquirer can be deleted")
    public void testDeleteAcquirer() {
        AcquirersPage acquirersPage = new AcquirersPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().selectAcquirer(ACQUIRER.acquirerName())
                .clickDeleteAcquirer()
                .clickDeleteButton();

        Allure.step("Verify: a success message appears after deleting the acquirer");
        assertThat(acquirersPage.getAlert().getMessage())
                .hasText("SUCCESSAcquirer was deleted successfully");

        Allure.step("Verify: the deleted acquirer is no longer present in the dropdown list");
        assertTrue(acquirersPage.getSelectAcquirer().isAcquirerAbsent(ACQUIRER.acquirerName()));
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER.acquirerName());
        TestUtils.deleteAcquirer(getApiRequestContext(), ACTIVE_ACQUIRER_NAME);
        TestUtils.deleteAcquirer(getApiRequestContext(), INACTIVE_ACQUIRER_NAME);
        TestUtils.deleteAcquirer(getApiRequestContext(), CHANGE_STATE_ACQUIRER.acquirerName());
        super.afterClass();
    }
}
