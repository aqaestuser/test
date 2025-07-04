package xyz.npgw.test.run;

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
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.AcquirersPage;

import java.util.List;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class EditAcquirerDialogTest extends BaseTest {

    private static final String ACQUIRER_NAME = "%s acquirer for edit form".formatted(RUN_ID);
    private static final Map<String, String> ACQUIRER_EDITED = Map.ofEntries(
            Map.entry("Acquirer name", ACQUIRER_NAME),
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
        TestUtils.createAcquirer(getApiRequestContext(), Acquirer.builder().acquirerName(ACQUIRER_NAME).build());
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
                .getSelectAcquirer().typeName(ACQUIRER_NAME)
                .getSelectAcquirer().clickAcquirerInDropdown(ACQUIRER_NAME)
                .getTable().clickEditAcquirerButton(ACQUIRER_NAME)
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
                .getSelectAcquirer().typeName(ACQUIRER_NAME)
                .getSelectAcquirer().clickAcquirerInDropdown(ACQUIRER_NAME)
                .getTable().clickEditAcquirerButton(ACQUIRER_NAME)
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
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "Display name"))
                .hasText(ACQUIRER_EDITED.get("Acquirer display name"));

        Allure.step("Verify: Acquirer code is 'NGenius' by default");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "Acquirer code"))
                .hasText("NGenius");

        Allure.step("Verify: Acquirer MID matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "MID"))
                .hasText(ACQUIRER_EDITED.get("Acquirer MID"));

        Allure.step("Verify: Acquirer MID MCC matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "MCC"))
                .hasText(ACQUIRER_EDITED.get("Acquirer MID MCC"));

        Allure.step("Verify: Currencies column contains expected currency");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "Currencies"))
                .hasText(ACQUIRER_EDITED.get("Currencies"));

        Allure.step("Verify: Acquirer config matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "Acquirer config"))
                .hasText(ACQUIRER_EDITED.get("Acquirer config"));

        Allure.step("Verify: 'System config' cell contains all values in correct order");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "System config"))
                .hasText(
                        "Challenge URL" + ACQUIRER_EDITED.get("Challenge URL")
                                + "Fingerprint URL" + ACQUIRER_EDITED.get("Fingerprint URL")
                                + "Resource URL" + ACQUIRER_EDITED.get("Resource URL")
                                + "Notification queue" + ACQUIRER_EDITED.get("Notification queue")
                );

        Allure.step("Verify: Status matches expected");
        assertThat(acquirersPage.getTable().getCell(ACQUIRER_NAME, "Status"))
                .hasText(ACQUIRER_EDITED.get("Status"));
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteAcquirer(getApiRequestContext(), ACQUIRER_NAME);
        super.afterClass();
    }
}
