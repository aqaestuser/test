package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
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
                .getSystemMenu()
                .clickAcquirersTab();

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
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAddAcquirer()
                .clickStatusRadiobutton(status)
                .getStatusRadiobutton(status);

        Allure.step(String.format("Verify: The radiobutton %s clicked.", status));
        assertThat(statusRadiobutton).hasAttribute("data-selected", "true");
    }

    @Ignore("Country and Timezone fields not exists already - fail after latest update (FALU)")
    @Test
    @TmsLink("322")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verifies Country and Timezone field interaction during Acquirer creation.")
    public void testCountryAndTimezoneFieldInteraction() {
        String oneTimezoneCountry = "Iceland";
        String icelandTimezone = "+00:00 Greenwich Mean Time";
        String multipleTimezoneCountry = "United States";
        String unitedStatesTimezone = "-09:00 Alaska Time";

        AddAcquirerDialog addAcquirerDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAddAcquirer();

        Allure.step("Verify: Country fields has default 'Search...' text", () -> {
            Assert.assertEquals(addAcquirerDialog.getSelectCountry().getAttribute("placeholder"), "Search...");
            assertThat(addAcquirerDialog.getSelectCountry()).hasValue("");
        });
        Allure.step("Verify: Timezone fields has default 'Select timezone' text");
        assertThat(addAcquirerDialog.getSelectTimezone()).hasText("Select timezone");

        addAcquirerDialog
                .clickSelectCountry();

        Allure.step("Verify: Dropdown with the country options is visible");
        assertThat(addAcquirerDialog.getSelectDropdown()).isVisible();

        addAcquirerDialog
                .clickCountryInDropdown(oneTimezoneCountry);

        Allure.step(String.format("Verify: Country field is populated with '%s'", oneTimezoneCountry));
        assertThat(addAcquirerDialog.getSelectCountry()).hasValue(oneTimezoneCountry);

        Allure.step(String.format("Verify: Timezone field is auto-filled with the timezone '%s'", icelandTimezone));
        assertThat(addAcquirerDialog.getSelectTimezone()).hasText(icelandTimezone);

        addAcquirerDialog
                .clickSelectCountryClearIcon();

        Allure.step("Verify: Country fields has default 'Search...' text");
        assertThat(addAcquirerDialog.getSelectCountry()).hasValue("");

        Allure.step("Verify: Timezone fields has default 'Select timezone' text");
        assertThat(addAcquirerDialog.getSelectTimezone()).hasText("Select timezone");

        addAcquirerDialog
                .clickSelectCountry()
                .clickCountryInDropdown(multipleTimezoneCountry);

        Allure.step(String.format("Verify: Country field is populated with %s", multipleTimezoneCountry));
        assertThat(addAcquirerDialog.getSelectCountry()).hasValue(multipleTimezoneCountry);

        Allure.step("Verify: Timezone fields has default 'Select timezone' text");
        assertThat(addAcquirerDialog.getSelectTimezone()).hasText("Select timezone");

        addAcquirerDialog
                .clickSelectTimezone()
                .clickTimezoneInDropdown(unitedStatesTimezone);

        Allure.step(String.format("Verify: Timezone field is timezone '%s'", unitedStatesTimezone));
        assertThat(addAcquirerDialog.getSelectTimezone()).hasText(unitedStatesTimezone);

        addAcquirerDialog
                .clickSelectCountryClearIcon()
                .clickSelectCountry()
                .clickSelectTimezone();

        Allure.step("Verify: Country fields has default 'Search...' text");
        assertThat(addAcquirerDialog.getSelectCountry()).hasValue("");

        Allure.step("Verify: Timezone fields has default 'Select timezone' text");
        assertThat(addAcquirerDialog.getSelectTimezone()).hasText("Select timezone");

        Allure.step("Verify: Timezone dropdown has no timezone options, has 'No items.' text");
        assertThat(addAcquirerDialog.getSelectDropdown()).hasText("No items.");
    }

    @Ignore("Timezone field not exists already (FALU)")
    @Test(expectedExceptions = AssertionError.class)
    @TmsLink("326")
    @Epic("System/Acquirers")
    @Feature("Add acquirer")
    @Description("Verifies Re-selecting the Same Timezone Does Not Change the Value")
    public void testReSelectingSameTimezoneKeepsValueUnchanged() {
        Allure.step("This test is temporarily disabled till bug fixed.");

        String oneTimezoneCountry = "Iceland";
        String icelandTimezone = "+00:00 Greenwich Mean Time";

        AddAcquirerDialog addAcquirerDialog = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAddAcquirer()
                .clickSelectCountry()
                .clickCountryInDropdown(oneTimezoneCountry)
                .clickSelectTimezone()
                .clickTimezoneInDropdown(icelandTimezone);

        Allure.step(String.format("Verify: Timezone field is timezone '%s'", icelandTimezone));
        assertThat(addAcquirerDialog.getSelectTimezone()).hasText(icelandTimezone);
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
                .getSystemMenu()
                .clickAcquirersTab()
                .clickAddAcquirer()
                .enterAcquirerName(acquirerName)
                .clickCheckboxCurrency("USD")
                .clickCreateButton();

        Allure.step("Verify: Acquirer creation success message is displayed");
        assertThat(acquirersPage.getAlertMessage()).containsText(
                "SUCCESSAcquirer was created successfully");

        Allure.step("Verify: the 'Add acquirer' dialog is no longer visible");
        assertThat(acquirersPage.getAddAcquirerDialog()).isHidden();

        acquirersPage.typeAcquirerNameToSelectAcquirerInputField(acquirerName);

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
            createAcquirer(getApiRequestContext(), acquirerName);
        }

        AcquirersPage acquirersPage = new DashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu()
                .clickAcquirersTab();

        AddAcquirerDialog acquirerDialog = acquirersPage
                .clickAddAcquirer()
                .enterAcquirerName(acquirerName)
                .clickCheckboxCurrency("USD");

        acquirerDialog.clickCreateButton();

        Allure.step("Verify: Acquirer Error message is displayed");
        assertThat(acquirerDialog.getAlertMessage()).containsText(
                "Acquirer with name {" + acquirerName + "} already exists.");

        Allure.step("Verify: the 'Add acquirer' dialog is not closed");
        assertThat(acquirersPage.getAddAcquirerDialog()).not().isHidden();
    }
}
