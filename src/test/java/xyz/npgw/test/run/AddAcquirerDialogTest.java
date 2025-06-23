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

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class AddAcquirerDialogTest extends BaseTest {

    private static final String EXISTING_ACQUIRER_NAME = "%s existing acquirer".formatted(RUN_ID);
    private static final String ACQUIRER_NAME = "%s awesome acquirer".formatted(RUN_ID);
    private final SystemConfig defaultConfig = new SystemConfig();

    Acquirer acquirer = new Acquirer(ACQUIRER_NAME,
            "123456",
            "ACQ001",
            "default",
            new Currency[]{Currency.USD},
            new SystemConfig(),
            true,
            "my-acquirer",
            "5411");

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createAcquirer(getApiRequestContext(), new Acquirer(EXISTING_ACQUIRER_NAME));
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
                "Enter acquirer name",
                "Enter acquirer code",
                "Enter acquirer display name",
                "Enter acquirer MID",
                "Enter acquirer MID MCC",
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
                .fillAcquirerNameField(acquirer.acquirerName())
                .fillAcquirerDisplayNameField(acquirer.acquirerDisplayName())
                .fillAcquirerMidField(acquirer.acquirerMid())
                .fillAcquirerMidMccField(acquirer.acquirerMidMcc())
                .fillChallengeUrlField(defaultConfig.challengeUrl())
                .fillFingerprintUrlField(defaultConfig.fingerprintUrl())
                .fillResourceUrlField(defaultConfig.resourceUrl())
                .fillNotificationQueueField(defaultConfig.notificationQueue())
                .clickCheckboxCurrency(acquirer.currencyList()[0].name())
                .fillAcquirerConfigField(acquirer.acquirerConfig())
                .clickCreateButton();

        Allure.step("Verify: The 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getAddAcquirerDialog()).isHidden();

        Allure.step("Verify: Acquirer creation success message is displayed");
        assertThat(acquirersPage.getAlert().getMessage())
                .containsText("SUCCESSAcquirer was created successfully");

        acquirersPage
                .getSelectAcquirer().selectAcquirer(acquirer.acquirerName());

        Allure.step("Verify: Acquirer display name matches expected");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "Acquirer display name"))
                .hasText(acquirer.acquirerDisplayName());

        Allure.step("Verify: Acquirer code is 'NGenius' by default");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "Acquirer code"))
                .hasText("NGenius");

        Allure.step("Verify: Acquirer MID matches expected");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "Acquirer MID"))
                .hasText(acquirer.acquirerMid());

        Allure.step("Verify: Acquirer MID MCC matches expected");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "Acquirer MID MCC"))
                .hasText(acquirer.acquirerMidMcc());

        Allure.step("Verify: Currencies column contains expected currency");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "Currencies"))
                .hasText(acquirer.currencyList()[0].name());

        Allure.step("Verify: Acquirer config matches expected");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "Acquirer config"))
                .hasText(acquirer.acquirerConfig());

        Allure.step("Verify: 'System config' cell contains all values in correct order");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "System config"))
                .hasText(
                        "Challenge URL" + acquirer.systemConfig().challengeUrl()
                                + "Fingerprint URL" + acquirer.systemConfig().fingerprintUrl()
                                + "Resource URL" + acquirer.systemConfig().resourceUrl()
                                + "Notification queue" + acquirer.systemConfig().notificationQueue()
                );

        Allure.step("Verify: Status matches expected");
        assertThat(acquirersPage.getTable().getCell(acquirer.acquirerName(), "Status"))
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
                .fillAcquirerNameField(EXISTING_ACQUIRER_NAME)
                .fillChallengeUrlField(defaultConfig.challengeUrl())
                .fillFingerprintUrlField(defaultConfig.fingerprintUrl())
                .fillResourceUrlField(defaultConfig.resourceUrl())
                .clickCheckboxCurrency("USD");

        acquirerDialog
                .clickCreateButton();

        Allure.step("Verify: Acquirer Error message is displayed");
        assertThat(acquirerDialog.getAlert().getMessage())
                .containsText("Acquirer with name {" + EXISTING_ACQUIRER_NAME + "} already exists.");

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

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteAcquirer(getApiRequestContext(), EXISTING_ACQUIRER_NAME);
        TestUtils.deleteAcquirer(getApiRequestContext(), acquirer.acquirerName());
        super.afterClass();
    }
}
