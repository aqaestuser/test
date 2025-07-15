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
import xyz.npgw.test.common.entity.FraudControl;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.FraudControlPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FraudControlTest extends BaseTest {

    private static final FraudControl FRAUD_CONTROL = FraudControl.builder()
            .controlName("ControlEverything")
            .controlCode("8848")
            .controlDisplayName("ControlDisplay")
            .controlConfig("notDefault")
            .build();
    private static final FraudControl FRAUD_CONTROL_INACTIVE = FraudControl.builder()
            .controlName("ControlNothing")
            .controlCode("9905")
            .controlDisplayName("DisplayNotAvailable")
            .controlConfig("suspicious")
            .build();
    private static final FraudControl FRAUD_CONTROL_ADD_ONE = FraudControl.builder()
            .controlName("ControlOne")
            .controlCode("0001")
            .controlDisplayName("ControlDisplayFirst")
            .controlConfig("firstQueue")
            .build();
    private static final FraudControl FRAUD_CONTROL_ADD_TWO = FraudControl.builder()
            .controlName("ControlTwo")
            .controlCode("0002")
            .controlDisplayName("ControlDisplaySecond")
            .controlConfig("secondQueue")
            .build();
    private final String fraudControlName = "Test fraudControl name";

    private static final String COMPANY_NAME = "%s company to bend Fraud Control".formatted(RUN_ID);
    private static final String BUSINESS_UNIT_NAME = "Business unit %s".formatted(RUN_ID);

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_NAME, BUSINESS_UNIT_NAME);
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_ONE);
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_TWO);
    }

    @Test
    @TmsLink("891")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Active Fraud Control")
    public void testAddActiveFraudControl() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode())
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName())
                .checkActiveRadiobutton()
                .clickCreateButton();

        Locator row = page.getTable().getRow(FRAUD_CONTROL.getControlName(), 0);

        Allure.step("Verify that all the data are presented in the row");
        assertThat(row).containsText(FRAUD_CONTROL.getControlCode());
        assertThat(row).containsText(FRAUD_CONTROL.getControlConfig());
        assertThat(row).containsText(FRAUD_CONTROL.getControlDisplayName());
        assertThat(row).containsText("Active");
    }

    @Test
    @TmsLink("904")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Inactive Fraud Control")
    public void testAddInactiveFraudControl() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_INACTIVE.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL_INACTIVE.getControlCode())
                .fillFraudControlConfigField(FRAUD_CONTROL_INACTIVE.getControlConfig())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL_INACTIVE.getControlDisplayName())
                .checkInactiveRadiobutton()
                .clickCreateButton();

        Locator row = page.getTable().getRow(FRAUD_CONTROL_INACTIVE.getControlName(), 0);

        Allure.step("Verify that all the data are presented in the row");
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlCode());
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlConfig());
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlDisplayName());
        assertThat(row).containsText("Inactive");
    }

    @Test
    @TmsLink("910")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Fraud Control to Business Unit (No Fraud Control)"
            + "Add Fraud Control to Business Unit (Business unit has Fraud Control)")
    public void testAddFraudControlToBusinessUnit() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTable().clickConnectControlIcon(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTable().clickConnectControlIcon(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Locator rowFraudOne = page.getTable().getRow(FRAUD_CONTROL_ADD_ONE.getControlDisplayName(), 1);
        Locator rowFraudTwo = page.getTable().getRow(FRAUD_CONTROL_ADD_TWO.getControlDisplayName(), 1);

        Allure.step("Verify that all the Fraud Controls are presented in Business Unit table");
        assertThat(rowFraudOne).containsText(FRAUD_CONTROL_ADD_ONE.getControlCode());
        assertThat(rowFraudOne).containsText(FRAUD_CONTROL_ADD_ONE.getControlConfig());
        assertThat(rowFraudOne).containsText("Active");
        assertThat(rowFraudTwo).containsText(FRAUD_CONTROL_ADD_TWO.getControlCode());
        assertThat(rowFraudTwo).containsText(FRAUD_CONTROL_ADD_TWO.getControlConfig());
        assertThat(rowFraudTwo).containsText("Active");
    }

    @Test
    @TmsLink("895")
    @Epic("System/Fraud control")
    @Feature("Fraud control")
    @Description("Verify the error message when attempting to create a Fraud Control with the existing name")
    public void testErrorMessageForExistedName() {
        FraudControlPage fraudControlPage = new FraudControlPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(fraudControlName)
                .clickCreateButton()
                .clickAddFraudControl()
                .fillFraudControlNameField(fraudControlName)
                .clickCreateButton();

        Allure.step("Verify that the error message ‘ERROR Entity with name … already exists.’ is displayed.");

        assertThat(fraudControlPage.getAlert().getMessage())
                .hasText("ERROREntity with name {" + fraudControlName + "} already exists.");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_INACTIVE.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), fraudControlName);
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_ONE.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_TWO.getControlName());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
