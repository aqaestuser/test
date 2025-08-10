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
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.acquirer.SetupAcquirerMidDialog;
import xyz.npgw.test.page.system.SuperAcquirersPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AddEditAcquirerTest extends BaseTest {

    private static final String EXISTING_ACQUIRER = "%s existing acquirer".formatted(RUN_ID);
    private static final SystemConfig DEFAULT_CONFIG = new SystemConfig();
    private static final Acquirer ACQUIRER = Acquirer.builder()
            .acquirerMid("123456")
            .acquirerCode("ACQ001")
            .acquirerDisplayName("%s display acquirer".formatted(TestUtils.now()))
            .currencyList(new Currency[]{Currency.USD})
            .acquirerName("%s my-acquirer".formatted(TestUtils.now()))
            .acquirerMidMcc("5411")
            .build();

    private static final Acquirer ACQUIRER_FOR_EDIT = Acquirer.builder()
            .acquirerName("%s acquirer for edit form".formatted(RUN_ID))
            .acquirerDisplayName("%s display acquirer for edit form".formatted(RUN_ID))
            .acquirerMidMcc("5411")
            .build();

    private static final Acquirer ACQUIRER_EDITED = Acquirer.builder()
            .acquirerName(ACQUIRER_FOR_EDIT.getAcquirerDisplayName())
            .acquirerDisplayName("%s display acquirer edited".formatted(RUN_ID))
            .acquirerCode("NGenius")
            .acquirerMid("new mid name")
            .acquirerMidMcc("2222")
            .currencyList(new Currency[]{Currency.GBP})
            .acquirerConfig("new config")
            .systemConfig(new SystemConfig("https://test.npgw.xyz/challenge/new/url",
                    "https://test.npgw.xyz/fingerprint/new/url",
                    "https://test.npgw.xyz/resource/new/url",
                    "new notificationQueue"))
            .isActive(false)
            .build();

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createAcquirer(getApiRequestContext(), Acquirer.builder().acquirerName(EXISTING_ACQUIRER).build());
        TestUtils.createAcquirer(getApiRequestContext(), ACQUIRER_FOR_EDIT);
    }

    @Test
    @TmsLink("249")
    @Epic("System/Acquirers")
    @Feature("Setup acquirer MID")
    @Description("Validate correct layout and behavior of the 'Setup acquirer MID' dialog.")
    public void testSetupAcquirerMidDialogDisplaysCorrectlyAndCloses() {
        SetupAcquirerMidDialog setupAcquirerMidDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickSetupAcquirerMidButton();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(setupAcquirerMidDialog.getDialogHeader()).hasText("Setup acquirer MID");

        Allure.step("Verify: all placeholders are correct for each field");
        assertEquals(setupAcquirerMidDialog.getAllPlaceholders(), List.of(
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
        assertThat(setupAcquirerMidDialog.getStatusSwitch()).hasText("StatusActiveInactive");

        Allure.step("Verify: the 'Allowed Currencies' Checkboxes visible");
        assertThat(setupAcquirerMidDialog.getAllowedCurrenciesCheckboxes()).hasText("Allowed currencyEURUSDGBP");

        SuperAcquirersPage acquirersPage = setupAcquirerMidDialog
                .clickCloseButton();

        Allure.step("Verify: the 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getSetupAcquirerMidDialog()).isHidden();
    }

    @Test(dataProvider = "getAcquirersStatus", dataProviderClass = TestDataProvider.class)
    @TmsLink("255")
    @Epic("System/Acquirers")
    @Feature("Setup acquirer MID")
    @Description("Verifies that the status radio buttons ('Active' and 'Inactive') toggle correctly.")
    public void testToggleStatusRadioButtonsCorrectly(String status) {
        Locator statusRadiobutton = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickSetupAcquirerMidButton()
                .clickStatusRadiobutton(status)
                .getStatusRadiobutton(status);

        Allure.step("Verify: The radiobutton is selected");
        assertThat(statusRadiobutton).hasAttribute("data-selected", "true");
    }

    @Test
    @TmsLink("412")
    @Epic("System/Acquirers")
    @Feature("Setup acquirer MID")
    @Description("New acquirer MID can be successfully set up and displayed correctly in the acquirers table.")
    public void testSetupAcquirerMid() {
        SuperAcquirersPage acquirersPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickSetupAcquirerMidButton()
                .fillAcquirerNameField(ACQUIRER.getAcquirerName())
                .fillAcquirerDisplayNameField(ACQUIRER.getAcquirerDisplayName())
                .fillAcquirerMidField(ACQUIRER.getAcquirerMid())
                .fillAcquirerMccField(ACQUIRER.getAcquirerMidMcc())
                .fillChallengeUrlField(ACQUIRER.getSystemConfig().challengeUrl())
                .fillFingerprintUrlField(ACQUIRER.getSystemConfig().fingerprintUrl())
                .fillResourceUrlField(ACQUIRER.getSystemConfig().resourceUrl())
                .fillNotificationQueueField(ACQUIRER.getSystemConfig().notificationQueue())
                .clickCheckboxCurrency(ACQUIRER.getCurrency())
                .fillAcquirerConfigField(ACQUIRER.getAcquirerConfig())
                .clickCreateButton();

        Allure.step("Verify: The 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getSetupAcquirerMidDialog()).isHidden();

        Allure.step("Verify: Acquirer creation success message is displayed");
        assertThat(acquirersPage.getAlert().getMessage())
                .containsText("SUCCESSAcquirer was created successfully");

        acquirersPage
                .getSelectAcquirerMid().selectAcquirerMid(ACQUIRER.getAcquirerDisplayName());

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
                .hasText(ACQUIRER.getCurrency());

        Allure.step("Verify: Acquirer config matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Acquirer config"))
                .hasText(ACQUIRER.getAcquirerConfig());

        Allure.step("Verify: 'System config' cell contains all values in correct order");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "System config"))
                .hasText(ACQUIRER.getSystemConfig().toString());

        Allure.step("Verify: Status matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER.getAcquirerName(), "Status"))
                .hasText("Active");
    }

    @Test
    @TmsLink("427")
    @Epic("System/Acquirers")
    @Feature("Setup acquirer MID")
    @Description("Verify error appears when creating an Acquirer with a duplicate name.")
    public void testCreateAcquirerWithDuplicateNameShowsError() {
        SetupAcquirerMidDialog setupAcquirerMidDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickSetupAcquirerMidButton()
                .fillAcquirerNameField(EXISTING_ACQUIRER)
                .fillAcquirerMidField("1234")
                .fillAcquirerMccField("1234")
                .fillChallengeUrlField(DEFAULT_CONFIG.challengeUrl())
                .fillFingerprintUrlField(DEFAULT_CONFIG.fingerprintUrl())
                .fillResourceUrlField(DEFAULT_CONFIG.resourceUrl())
                .clickCheckboxCurrency("USD")
                .clickCreateButtonAndTriggerError();

        Allure.step("Verify: Acquirer Error message is displayed");
        assertThat(setupAcquirerMidDialog.getAlert().getMessage())
                .containsText("Acquirer with name {" + EXISTING_ACQUIRER + "} already exists.");
    }

    @Test
    @TmsLink("526")
    @Epic("System/Acquirers")
    @Feature("Setup acquirer MID")
    @Description("Verify default state of the 'Setup acquirer MID' dialog")
    public void testDefaultStateOfSetupAcquirerMidDialog() {
        SetupAcquirerMidDialog setupAcquirerMidDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickSetupAcquirerMidButton();

        Allure.step("Verify: Acquirer name field is marked as invalid");
        assertThat(setupAcquirerMidDialog.getAcquirerNameField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: Acquirer code field is read-only");
        assertThat(setupAcquirerMidDialog.getAcquirerCodeField()).hasAttribute("aria-readonly", "true");

        Allure.step("Verify: Challenge URL field is marked as invalid");
        assertThat(setupAcquirerMidDialog.getChallengeURLField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: Fingerprint URL field is marked as invalid");
        assertThat(setupAcquirerMidDialog.getFingerprintUrlField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: Resource URL field is marked as invalid");
        assertThat(setupAcquirerMidDialog.getResourceUrlField()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify: 'Active' status is selected by default");
        assertThat(setupAcquirerMidDialog.getStatusRadiobutton("Active")).isChecked();

        Allure.step("Verify: 'EUR' is selected as the default allowed currency");
        assertThat(setupAcquirerMidDialog.getAllowedCurrencyRadio("EUR")).isChecked();

        Allure.step("Verify: 'Create' button is disabled when required fields are not filled");
        assertThat(setupAcquirerMidDialog.getCreateButton()).isDisabled();
    }

    @Test(dependsOnMethods = "testSetupAcquirerMid")
    @TmsLink("239")
    @Epic("System/Acquirers")
    @Feature("Edit acquirer MID")
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

        List<String> actualPlaceholders = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirerMid().selectAcquirerMid(ACQUIRER.getAcquirerDisplayName())
                .getTable().clickEditAcquirerMidButton(ACQUIRER.getAcquirerName())
                .getAllPlaceholders();

        Allure.step("Verify placeholders match expected values for all fields");
        assertEquals(actualPlaceholders, expectedPlaceholders);
    }

    @Test
    @TmsLink("450")
    @Epic("System/Acquirers")
    @Feature("Edit acquirer MID")
    @Description("Edit Acquirer Mid and Verify Updated Data in the Table")
    public void testEditAcquirerVerifyUpdatedData() {
        SuperAcquirersPage acquirersPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .getSelectAcquirerMid().selectAcquirerMid(ACQUIRER_FOR_EDIT.getAcquirerDisplayName())
                .getTable().clickEditAcquirerMidButton(ACQUIRER_FOR_EDIT.getAcquirerDisplayName())
                .fillAcquirerDisplayNameField(ACQUIRER_EDITED.getAcquirerDisplayName())
                .fillAcquirerMidField(ACQUIRER_EDITED.getAcquirerMid())
                .fillAcquirerMccField(ACQUIRER_EDITED.getAcquirerMidMcc())
                .fillChallengeUrlField(ACQUIRER_EDITED.getSystemConfig().challengeUrl())
                .fillFingerprintUrlField(ACQUIRER_EDITED.getSystemConfig().fingerprintUrl())
                .fillResourceUrlField(ACQUIRER_EDITED.getSystemConfig().resourceUrl())
                .fillNotificationQueueField(ACQUIRER_EDITED.getSystemConfig().notificationQueue())
                .fillAcquirerConfigField(ACQUIRER_EDITED.getAcquirerConfig())
                .clickStatusRadiobutton(ACQUIRER_EDITED.getStatus())
                .clickCheckboxCurrency(ACQUIRER_EDITED.getCurrency())
                .clickSaveChangesButton();

        Allure.step("Verify: Successful message");
        assertThat(acquirersPage.getAlert().getMessage())
                .hasText("SUCCESSAcquirer was updated successfully");

        Allure.step("Verify: The 'Edit acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getEditAcquirerMidDialog()).isHidden();


        Allure.step("Verify: Acquirer display name matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "Display name"))
                .hasText(ACQUIRER_EDITED.getAcquirerDisplayName());

        Allure.step("Verify: Acquirer code is 'NGenius' by default");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "Acquirer code"))
                .hasText("NGenius");

        Allure.step("Verify: Acquirer MID matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "MID"))
                .hasText(ACQUIRER_EDITED.getAcquirerMid());

        Allure.step("Verify: Acquirer MID MCC matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "MCC"))
                .hasText(ACQUIRER_EDITED.getAcquirerMidMcc());

        Allure.step("Verify: Currencies column contains expected currency");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "Currencies"))
                .hasText(ACQUIRER_EDITED.getCurrency());

        Allure.step("Verify: Acquirer config matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "Acquirer config"))
                .hasText(ACQUIRER_EDITED.getAcquirerConfig());

        Allure.step("Verify: 'System config' cell contains all values in correct order");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "System config"))
                .hasText(ACQUIRER_EDITED.getSystemConfig().toString());

        Allure.step("Verify: Status matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT.getAcquirerName(), "Status"))
                .hasText(ACQUIRER_EDITED.getStatus());
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteAcquirer(getApiRequestContext(), EXISTING_ACQUIRER);
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER.getAcquirerName());
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER_FOR_EDIT.getAcquirerName());
        super.afterClass();
    }
}
