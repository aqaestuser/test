package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.Acquirer;
import xyz.npgw.test.common.entity.SystemConfig;
import xyz.npgw.test.common.provider.TestDataProvider;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.dialog.acquirer.AddAcquirerDialog;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static xyz.npgw.test.common.util.TestUtils.createAcquirer;
import static xyz.npgw.test.common.util.TestUtils.deleteAcquirer;
import static xyz.npgw.test.common.util.TestUtils.getAcquirer;

public class AddAcquirerDialogTest extends BaseTest {

    private final SystemConfig defaultConfig = new SystemConfig();

    @Test
    @TmsLink("249")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description(
            "Verify that the 'Add Acquirer' form opens with the correct header and input fields, and closes correctly.")
    public void testAddAcquirerFormOpensWithCorrectHeaderAndFieldsAndClosesCorrectly() {
        List<String> expectedPlaceholders = List.of(
                "Enter acquirer name",
                "Enter acquirer code",
                "Enter challenge URL",
                "Enter fingerprint URL",
                "Enter resource URL",
                "Enter notification queue",
                "Enter acquirer config"
        );

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        AddAcquirerDialog addAcquirerDialog = acquirersPage.clickAddAcquirer();

        Allure.step("Verify: the header contains the expected title text");
        assertThat(addAcquirerDialog.getDialogHeader()).hasText("Add acquirer");

        Allure.step("Verify: all placeholders are correct for each field");
        assertEquals(addAcquirerDialog.getAllFieldPlaceholders(), expectedPlaceholders);

        Allure.step("Verify: the Status Switch visible and contains switch Active&Inactive");
        assertThat(addAcquirerDialog.getStatusSwitch()).isVisible();
        assertThat(addAcquirerDialog.getStatusSwitch()).hasText("StatusActiveInactive");

        Allure.step("Verify: the 'Allowed Currencies' Checkboxes visible and contains USD&EUR");
        assertThat(addAcquirerDialog.getAllowedCurrenciesCheckboxes()).isVisible();
        assertThat(addAcquirerDialog.getAllowedCurrenciesCheckboxes()).hasText("Allowed currenciesUSDEUR");

        addAcquirerDialog.clickCloseButton();

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
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .clickStatusRadiobutton(status)
                .getStatusRadiobutton(status);

        Allure.step(String.format("Verify: The radiobutton %s clicked.", status));
        assertThat(statusRadiobutton).hasAttribute("data-selected", "true");
    }

    @Test
    @TmsLink("412")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify that a new Acquirer can be successfully created and appears in the Acquirers dropdown.")
    public void testAcquirerSuccessfullyCreatedAndAppearsInDropdown() {
        String acquirerName = "Awesome acquirer";
        deleteAcquirer(getApiRequestContext(), acquirerName);

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerName(acquirerName)
                .fillChallengeUrl(defaultConfig.challengeUrl())
                .fillFingerprintUrl(defaultConfig.fingerprintUrl())
                .fillResourceUrl(defaultConfig.resourceUrl())
                .clickCheckboxCurrency("USD")
                .clickCreateButton();

        Allure.step("Verify: Acquirer creation success message is displayed");
        assertThat(acquirersPage.getAlert().getAlertMessage()).containsText(
                "SUCCESSAcquirer was created successfully");

        Allure.step("Verify: the 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getAddAcquirerDialog()).isHidden();

        acquirersPage
                .getSelectAcquirer().typeAcquirerNameToSelectAcquirerInputField(acquirerName);

        Allure.step(String.format("Verify: Dropdown contain '%s' acquirer", acquirerName));
        assertThat(acquirersPage.getAddAcquirerDialog()).hasText(acquirerName);
    }

    @Test
    @TmsLink("427")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify error appears when creating an Acquirer with a duplicate name.")
    public void testCreateAcquirerWithDuplicateNameShowsError() {
        String acquirerName = "Awesome acquirer";
        if (!getAcquirer(getApiRequestContext(), acquirerName)) {
            createAcquirer(getApiRequestContext(), new Acquirer(acquirerName));
        }

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab();

        AddAcquirerDialog acquirerDialog = acquirersPage
                .clickAddAcquirer()
                .fillAcquirerName(acquirerName)
                .fillChallengeUrl(defaultConfig.challengeUrl())
                .fillFingerprintUrl(defaultConfig.fingerprintUrl())
                .fillResourceUrl(defaultConfig.resourceUrl())
                .clickCheckboxCurrency("USD");

        acquirerDialog.clickCreateButton();

        Allure.step("Verify: Acquirer Error message is displayed");
        assertThat(acquirerDialog
                .getAlert().getAlertMessage())
                .containsText("Acquirer with name {" + acquirerName + "} already exists.");

        Allure.step("Verify: the 'Add acquirer' dialog is not closed");
        assertThat(acquirersPage.getAddAcquirerDialog()).not().isHidden();
    }

    @Test
    @TmsLink("526")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify that 'Create' button is disabled when Acquirer name is empty.")
    public void testDisableCreateButtonWhenAcquirerNameIsEmpty() {
        AddAcquirerDialog addAcquirerDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerName("Acquirer name");

        Allure.step("Verify: 'Create' button is not disabled.");
        assertThat(addAcquirerDialog.getCreateButton()).not().isDisabled();

        addAcquirerDialog.getAcquirerNamePlaceholder().clear();

        Allure.step("Verify: 'Create' button is disabled.");
        assertThat(addAcquirerDialog.getCreateButton()).isDisabled();
    }

    @Test(dataProvider = "acquirerNegativeData", dataProviderClass = TestDataProvider.class)
    @TmsLink("547")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verify validation messages when creating Acquirer with invalid input.")
    public void testDisplayValidationErrorsForInvalidAcquirerInput(Acquirer acquirer, String expectedError) {
        deleteAcquirer(getApiRequestContext(), acquirer.acquirerName());

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAddAcquirer()
                .fillAcquirerName(acquirer.acquirerName())
                .fillAcquirerForm(acquirer)
                .clickCreateButton();

        assertThat(acquirersPage.getAlert().getAlertMessage()).isVisible();

        Allure.step(String.format("Verify error message is: %s", expectedError));
        assertThat(acquirersPage.getAlert().getAlertMessage()).hasText(expectedError);
    }
}
