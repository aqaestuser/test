package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
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
import xyz.npgw.test.page.dialog.acquirer.AddAcquirerDialog;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.List;

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
    private static final Acquirer ACQUIRER_EDITED = Acquirer.builder()
            .acquirerName(ACQUIRER_FOR_EDIT)
            .acquirerDisplayName("new display name")
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

    @Ignore("Possible bug: aria-readonly='true' for AcquirerDisplayName field")
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
                .fillChallengeUrlField(ACQUIRER.getSystemConfig().challengeUrl())
                .fillFingerprintUrlField(ACQUIRER.getSystemConfig().fingerprintUrl())
                .fillResourceUrlField(ACQUIRER.getSystemConfig().resourceUrl())
                .fillNotificationQueueField(ACQUIRER.getSystemConfig().notificationQueue())
                .clickCheckboxCurrency(ACQUIRER.getCurrency())
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

    @Ignore("waiting for getByPlaceholder('Enter display name')")
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
                .fillAcquirerDisplayNameField(ACQUIRER_EDITED.getAcquirerDisplayName())
                .fillAcquirerMidField(ACQUIRER_EDITED.getAcquirerMid())
                .fillAcquirerMidMccField(ACQUIRER_EDITED.getAcquirerMidMcc())
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
        assertThat(acquirersPage.getEditAcquirerDialog()).isHidden();

        Allure.step("Verify: Acquirer display name matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Display name"))
                .hasText(ACQUIRER_EDITED.getAcquirerDisplayName());

        Allure.step("Verify: Acquirer code is 'NGenius' by default");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Acquirer code"))
                .hasText("NGenius");

        Allure.step("Verify: Acquirer MID matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "MID"))
                .hasText(ACQUIRER_EDITED.getAcquirerMid());

        Allure.step("Verify: Acquirer MID MCC matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "MCC"))
                .hasText(ACQUIRER_EDITED.getAcquirerMidMcc());

        Allure.step("Verify: Currencies column contains expected currency");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Currencies"))
                .hasText(ACQUIRER_EDITED.getCurrency());

        Allure.step("Verify: Acquirer config matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Acquirer config"))
                .hasText(ACQUIRER_EDITED.getAcquirerConfig());

        Allure.step("Verify: 'System config' cell contains all values in correct order");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "System config"))
                .hasText(ACQUIRER_EDITED.getSystemConfig().toString());

        Allure.step("Verify: Status matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_FOR_EDIT, "Status"))
                .hasText(ACQUIRER_EDITED.getStatus());
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
