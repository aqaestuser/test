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
    @Description(
            "Verify that the 'Add Acquirer' form opens with the correct header and input fields, and closes correctly.")
    public void testAddAcquirerFormOpensWithCorrectHeaderAndFieldsAndClosesCorrectly() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        AddAcquirerDialog addAcquirerDialog = acquirersPage
                .clickAddAcquirer();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(addAcquirerDialog.getDialogHeader()).hasText("Add acquirer");

        Allure.step("Verify: all placeholders are correct for each field");
        assertEquals(addAcquirerDialog.getAllPlaceholders(), List.of(
                "Enter acquirer name",
                "Enter acquirer title",
                "Enter acquirer code",
                "Enter challenge URL",
                "Enter fingerprint URL",
                "Enter resource URL",
                "Enter notification queue",
                "Enter acquirer config"
        ));

        Allure.step("Verify: the Status Switch visible and contains switch Active&Inactive");
        assertThat(addAcquirerDialog.getStatusSwitch()).hasText("StatusActiveInactive");

        Allure.step("Verify: the 'Allowed Currencies' Checkboxes visible");
        assertThat(addAcquirerDialog.getAllowedCurrenciesCheckboxes()).hasText("Allowed currenciesEURUSDGBP");

        addAcquirerDialog
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
    @Description("Verify that a new Acquirer can be successfully created and appears in the Acquirers dropdown.")
    public void testAcquirerSuccessfullyCreatedAndAppearsInDropdown() {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerName(ACQUIRER_NAME)
                .fillChallengeUrl(defaultConfig.challengeUrl())
                .fillFingerprintUrl(defaultConfig.fingerprintUrl())
                .fillResourceUrl(defaultConfig.resourceUrl())
                .clickCheckboxCurrency("USD")
                .clickCreateButton();

        Allure.step("Verify: Acquirer creation success message is displayed");
        assertThat(acquirersPage.getAlert().getMessage())
                .containsText("SUCCESSAcquirer was created successfully");

        Allure.step("Verify: The 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getAddAcquirerDialog()).isHidden();

        acquirersPage
                .getSelectAcquirer().typeName(ACQUIRER_NAME);

        Allure.step("Verify: Dropdown contains acquirer name");
        assertThat(acquirersPage.getSelectAcquirer().getSelectAcquirersDropdownItems()).hasText(ACQUIRER_NAME);
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
                .fillAcquirerName(EXISTING_ACQUIRER_NAME)
                .fillChallengeUrl(defaultConfig.challengeUrl())
                .fillFingerprintUrl(defaultConfig.fingerprintUrl())
                .fillResourceUrl(defaultConfig.resourceUrl())
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
    @Description("Verify that 'Create' button is disabled when Acquirer name is empty.")
    public void testDisableCreateButtonWhenAcquirerNameIsEmpty() {
        AddAcquirerDialog addAcquirerDialog = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerName("Acquirer name");

        Allure.step("Verify: 'Create' button is enabled.");
        assertThat(addAcquirerDialog.getCreateButton()).isEnabled();

        addAcquirerDialog
                .getAcquirerNamePlaceholder().clear();

        Allure.step("Verify: 'Create' button is disabled.");
        assertThat(addAcquirerDialog.getCreateButton()).isDisabled();
    }

    @Test(dataProvider = "acquirerNegativeData", dataProviderClass = TestDataProvider.class)
    @TmsLink("547")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify validation messages when creating Acquirer with invalid input.")
    public void testDisplayValidationErrorsForInvalidAcquirerInput(Acquirer acquirer, String expectedError) {
        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerName(acquirer.acquirerName())
                .fillAcquirerForm(acquirer)
                .clickCreateButton();

        Allure.step("Verify: error message presence and text");
        assertThat(acquirersPage.getAlert().getMessage()).hasText(expectedError);
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteAcquirer(getApiRequestContext(), EXISTING_ACQUIRER_NAME);
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER_NAME);
        super.afterClass();
    }
}
