package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.Currency;
import xyz.npgw.test.common.entity.SystemConfig;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.dialog.acquirer.AddAcquirerDialog;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.List;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AddEditAcquirerTest extends BaseTest {

    private static final String EXISTING_ACQUIRER = "%s existing acquirer".formatted(RUN_ID);
    private static final SystemConfig DEFAULT_CONFIG = new SystemConfig();
    private static final Acquirer ACQUIRER = Acquirer.builder()
            .acquirerMid("123456")
            .acquirerCode("ACQ001")
            .currencyList(new Currency[]{Currency.USD})
            .acquirerName("%s my-acquirer".formatted(TestUtils.now()))
            .acquirerMidMcc("5411")
            .build();

    private static final String ACQUIRER_FOR_EDIT = "%s acquirer for edit form".formatted(RUN_ID);
    private static final Map<String, String> ACQUIRER_EDITED = Map.ofEntries(
            Map.entry("Acquirer name", ACQUIRER_FOR_EDIT),
            Map.entry("Acquirer display name", "new display name"),
            Map.entry("Acquirer code", "NGenius"),
            Map.entry("Acquirer MID", "new mid name"),
            Map.entry("Acquirer MID MCC", "2222"),
            Map.entry("Currencies", "GBP"),
            Map.entry("Acquirer config", "new config"),
            Map.entry("Challenge URL", "https://test.npgw.xyz/challenge/new/url"),
            Map.entry("Fingerprint URL", "https://test.npgw.xyz/fingerprint/new/url"),
            Map.entry("Resource URL", "https://test.npgw.xyz/resource/new/url"),
            Map.entry("Notification queue", "new notificationQueue"),
            Map.entry("Status", "Inactive")
    );

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createAcquirer(getApiRequestContext(), Acquirer.builder().acquirerName(EXISTING_ACQUIRER).build());
        TestUtils.createAcquirer(getApiRequestContext(), Acquirer.builder().acquirerName(ACQUIRER_FOR_EDIT).build());
    }

    @Test
    @TmsLink("249")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify 'Add Acquirer' form opens with the correct header and input fields, and closes correctly.")
    public void testAddAcquirerFormOpensWithCorrectHeaderAndFieldsAndClosesCorrectly() {
        AddAcquirerDialog addAcquirerDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(addAcquirerDialog.getDialogHeader()).hasText("Add acquirer");

        Allure.step("Verify: all placeholders are correct for each field");
        assertEquals(addAcquirerDialog.getAllPlaceholders(), List.of(
                "Enter entity name",
                "Enter acquirer code",
                "Enter display name",
                "Enter MID",
                "Enter MCC",
                "Enter challenge URL",
                "Enter fingerprint URL",
                "Enter resource URL",
                "Enter notification queue",
                "Enter acquirer config"
        ));

        Allure.step("Verify: the Status Switch visible and contains switch Active&Inactive");
        assertThat(addAcquirerDialog.getStatusSwitch()).hasText("StatusActiveInactive");

        Allure.step("Verify: the 'Allowed Currencies' Checkboxes visible");
        assertThat(addAcquirerDialog.getAllowedCurrenciesCheckboxes()).hasText("Allowed currencyEURUSDGBP");

        AcquirersPage acquirersPage = addAcquirerDialog
                .clickCloseButton();

        Allure.step("Verify: the 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getAddAcquirerDialog()).isHidden();
    }

    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("255")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verifies that the status radio buttons ('Active' and 'Inactive') toggle correctly.")
    public void testToggleStatusRadioButtonsCorrectly(String status) {
        Locator statusRadiobutton = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .clickStatusRadiobutton(status)
                .getStatusRadiobutton(status);

        Allure.step("Verify: The radiobutton is selected");
        assertThat(statusRadiobutton).hasAttribute("data-selected", "true");
    }

    @Test
    @TmsLink("412")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("New Acquirer can be successfully created and its details appear correctly in the acquirers table")
    public void testAddAcquirer() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerNameField(ACQUIRER.getAcquirerName())
                .fillAcquirerDisplayNameField(ACQUIRER.getAcquirerDisplayName())
                .fillAcquirerMidField(ACQUIRER.getAcquirerMid())
                .fillAcquirerMidMccField(ACQUIRER.getAcquirerMidMcc())
                .fillChallengeUrlField(DEFAULT_CONFIG.challengeUrl())
                .fillFingerprintUrlField(DEFAULT_CONFIG.fingerprintUrl())
                .fillResourceUrlField(DEFAULT_CONFIG.resourceUrl())
                .fillNotificationQueueField(DEFAULT_CONFIG.notificationQueue())
                .clickCheckboxCurrency(ACQUIRER.getCurrencyList()[0].name())
                .fillAcquirerConfigField(ACQUIRER.getAcquirerConfig())
                .clickCreateButton();

        Allure.step("Verify: The 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getAddAcquirerDialog()).isHidden();

        Allure.step("Verify: Acquirer creation success message is displayed");
        assertThat(acquirersPage.getAlert().getMessage())
                .containsText("SUCCESSAcquirer was created successfully");

        acquirersPage
                .getSelectAcquirer().selectAcquirer(ACQUIRER.getAcquirerName());

        Allure.step("Verify: Entity name matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Entity name"))
                .hasText(ACQUIRER.getAcquirerName());

        Allure.step("Verify: Display name matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Display name"))
                .hasText(ACQUIRER.getAcquirerDisplayName());

        Allure.step("Verify: Acquirer code is 'NGenius' by default");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Acquirer code"))
                .hasText("NGenius");

        Allure.step("Verify: Acquirer MID matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "MID"))
                .hasText(ACQUIRER.getAcquirerMid());

        Allure.step("Verify: Acquirer MID MCC matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "MCC"))
                .hasText(ACQUIRER.getAcquirerMidMcc());

        Allure.step("Verify: Currencies column contains expected currency");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Currencies"))
                .hasText(ACQUIRER.getCurrencyList()[0].name());

        Allure.step("Verify: Acquirer config matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Acquirer config"))
                .hasText(ACQUIRER.getAcquirerConfig());

        Allure.step("Verify: 'System config' cell contains all values in correct order");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "System config"))
                .hasText(
                        "Challenge URL" + ACQUIRER.getSystemConfig().challengeUrl()
                                + "Fingerprint URL" + ACQUIRER.getSystemConfig().fingerprintUrl()
                                + "Resource URL" + ACQUIRER.getSystemConfig().resourceUrl()
                                + "Notification queue" + ACQUIRER.getSystemConfig().notificationQueue()
                );

        Allure.step("Verify: Status matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Status"))
                .hasText("Active");
    }

    @Test
    @TmsLink("427")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify error appears when creating an Acquirer with a duplicate name.")
    public void testCreateAcquirerWithDuplicateNameShowsError() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        AddAcquirerDialog acquirerDialog = acquirersPage
                .clickAddAcquirer()
                .fillAcquirerNameField(EXISTING_ACQUIRER)
                .fillAcquirerMidField("1234")
                .fillAcquirerMidMccField("1234")
                .fillChallengeUrlField(DEFAULT_CONFIG.challengeUrl())
                .fillFingerprintUrlField(DEFAULT_CONFIG.fingerprintUrl())
                .fillResourceUrlField(DEFAULT_CONFIG.resourceUrl())
                .clickCheckboxCurrency("USD");

        acquirerDialog
                .clickCreateButton();

        Allure.step("Verify: Acquirer Error message is displayed");
        assertThat(acquirerDialog.getAlert().getMessage())
                .containsText("Acquirer with name {" + EXISTING_ACQUIRER + "} already exists.");

        Allure.step("Verify: the 'Add acquirer' dialog is not closed");
        assertThat(acquirersPage.getAddAcquirerDialog()).isVisible();
    }

    @Test
    @TmsLink("526")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify default state of the 'Add Acquirer' dialog")
    public void testDefaultStateOfAddAcquirerDialog() {
        AddAcquirerDialog addAcquirerDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer();

        Allure.step("Verify: Acquirer name field is marked as invalid");
        assertThat(addAcquirerDialog.getAcquirerNameField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: Acquirer code field is read-only");
        assertThat(addAcquirerDialog.getAcquirerCodeField()).hasAttribute("aria-readonly", "true");

        Allure.step("Verify: Challenge URL field is marked as invalid");
        assertThat(addAcquirerDialog.getChallengeURLField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: Fingerprint URL field is marked as invalid");
        assertThat(addAcquirerDialog.getFingerprintUrlField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: Resource URL field is marked as invalid");
        assertThat(addAcquirerDialog.getResourceUrlField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: 'Active' status is selected by default");
        assertThat(addAcquirerDialog.getStatusRadiobutton("Active")).isChecked();

        Allure.step("Verify: 'EUR' is selected as the default allowed currency");
        assertThat(addAcquirerDialog.getAllowedCurrencyRadio("EUR")).isChecked();

        Allure.step("Verify: 'Create' button is disabled when required fields are not filled");
        assertThat(addAcquirerDialog.getCreateButton()).isDisabled();
    }

    @Test
    @TmsLink("239")
    @Epic("System/Acquirers")
    @Feature("Edit acquirers")
    @Description("Verifies that all form field placeholders are set correctly")
    public void testVerifyPlaceholdersEditForm() {
        List<String> expectedPlaceholders = List.of(
                "Enter entity name",
                "Enter acquirer code",
                "Enter display name",
                "Enter MID",
                "Enter MCC",
                "Enter challenge URL",
                "Enter fingerprint URL",
                "Enter resource URL",
                "Enter notification queue",
                "Enter acquirer config"
        );

        List<String> actualPlaceholders = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().typeName(ACQUIRER_FOR_EDIT)
                .getSelectAcquirer().clickAcquirerInDropdown(ACQUIRER_FOR_EDIT)
                .getTable().clickEditAcquirerButton(ACQUIRER_FOR_EDIT)
                .getAllPlaceholders();

        Allure.step("Verify placeholders match expected values for all fields");
        assertEquals(actualPlaceholders, expectedPlaceholders);
    }

    @Test
    @TmsLink("450")
    @Epic("System/Acquirers")
    @Feature("Edit acquirers")
    @Description("Edit Acquirer and Verify Updated Data in the Table")
    public void testEditAcquirerVerifyUpdatedData() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirer().typeName(ACQUIRER_FOR_EDIT)
                .getSelectAcquirer().clickAcquirerInDropdown(ACQUIRER_FOR_EDIT)
                .getTable().clickEditAcquirerButton(ACQUIRER_FOR_EDIT)
                .fillAcquirerDisplayNameField(ACQUIRER_EDITED.get("Acquirer display name"))
                .fillAcquirerMidField(ACQUIRER_EDITED.get("Acquirer MID"))
                .fillAcquirerMidMccField(ACQUIRER_EDITED.get("Acquirer MID MCC"))
                .fillChallengeUrlField(ACQUIRER_EDITED.get("Challenge URL"))
                .fillFingerprintUrlField(ACQUIRER_EDITED.get("Fingerprint URL"))
                .fillResourceUrlField(ACQUIRER_EDITED.get("Resource URL"))
                .fillNotificationQueueField(ACQUIRER_EDITED.get("Notification queue"))
                .fillAcquirerConfigField(ACQUIRER_EDITED.get("Acquirer config"))
                .clickStatusRadiobutton(ACQUIRER_EDITED.get("Status"))
                .clickCheckboxCurrency(ACQUIRER_EDITED.get("Currencies"))
                .clickSaveChangesButton();

        Allure.step("Verify: Successful message");
        assertThat(acquirersPage.getAlert().getMessage())
                .hasText("SUCCESSAcquirer was updated successfully");

        Allure.step("Verify: The 'Edit acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getEditAcquirerDialog()).isHidden();

        Allure.step("Verify: Acquirer display name matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Display name"))
                .hasText(ACQUIRER_EDITED.get("Acquirer display name"));

        Allure.step("Verify: Acquirer code is 'NGenius' by default");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Acquirer code"))
                .hasText("NGenius");

        Allure.step("Verify: Acquirer MID matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "MID"))
                .hasText(ACQUIRER_EDITED.get("Acquirer MID"));

        Allure.step("Verify: Acquirer MID MCC matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "MCC"))
                .hasText(ACQUIRER_EDITED.get("Acquirer MID MCC"));

        Allure.step("Verify: Currencies column contains expected currency");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Currencies"))
                .hasText(ACQUIRER_EDITED.get("Currencies"));

        Allure.step("Verify: Acquirer config matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Acquirer config"))
                .hasText(ACQUIRER_EDITED.get("Acquirer config"));

        Allure.step("Verify: 'System config' cell contains all values in correct order");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "System config"))
                .hasText(
                        "Challenge URL" + ACQUIRER_EDITED.get("Challenge URL")
                                + "Fingerprint URL" + ACQUIRER_EDITED.get("Fingerprint URL")
                                + "Resource URL" + ACQUIRER_EDITED.get("Resource URL")
                                + "Notification queue" + ACQUIRER_EDITED.get("Notification queue")
                );

        Allure.step("Verify: Status matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Status"))
                .hasText(ACQUIRER_EDITED.get("Status"));
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteAcquirer(getApiRequestContext(), EXISTING_ACQUIRER);
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER.getAcquirerName());
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER_FOR_EDIT);
        super.afterClass();
    }
}
