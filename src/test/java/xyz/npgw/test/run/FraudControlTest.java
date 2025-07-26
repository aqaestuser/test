package xyz.npgw.test.run;

import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.FraudControl;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.DashboardPage;
import xyz.npgw.test.page.system.FraudControlPage;

import java.util.List;

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
    private static final FraudControl FRAUD_CONTROL_ADD_INACTIVE = FraudControl.builder()
            .controlName("Inactive control")
            .controlCode("0003")
            .controlDisplayName("Inactive control")
            .isActive(false)
            .controlConfig("firstQueue")
            .build();
    private static final String FRAUD_CONTROL_NAME = "%S Test fraudControl name".formatted(RUN_ID);
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
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_INACTIVE);
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

        Locator row = page.getTableControls().getRow(FRAUD_CONTROL.getControlName());

        Allure.step("Verify that all the data are presented in the row");
        assertThat(row).containsText(FRAUD_CONTROL.getControlCode());
        assertThat(row).containsText(FRAUD_CONTROL.getControlConfig());
        assertThat(row).containsText(FRAUD_CONTROL.getControlDisplayName());
        assertThat(row).containsText("Active");
    }

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("920")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Fraud Control to Business Unit with Cancel button"
            + "Add Fraud Control to Business Unit with 'Cross'"
            + "Add Fraud Control to Business Unit with ESC")
    public void testCancelAddingFraudControlToBusinessUnit() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL.getControlName())
                .clickCancelButton();

        Allure.step("Verify that due to click Cancel button Fraud Control hasn't been added");
        Locator attemptOne = page.getTableBusinessUnitControls().getNoRowsToDisplayMessage();
        assertThat(attemptOne).isAttached();

        page.getTableControls().clickConnectControlButton(FRAUD_CONTROL.getControlName())
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control hasn't been added");
        Locator attemptTwo = page.getTableBusinessUnitControls().getNoRowsToDisplayMessage();
        assertThat(attemptTwo).isAttached();

        page.getTableControls().clickConnectControlButton(FRAUD_CONTROL.getControlName())
                .pressEscapeKey();

        Allure.step("Verify that due to press ESC keyboard button Fraud Control hasn't been added");
        Locator attemptThree = page.getTableBusinessUnitControls().getNoRowsToDisplayMessage();
        assertThat(attemptThree).isAttached();
    }

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("972")
    @Epic("System/Fraud Control")
    @Feature("Control table")
    @Description("Delete Fraud Control with Cancel button"
            + "Delete Fraud Control with 'Cross'"
            + "Delete Fraud Control with ESC")
    public void testCancelDeletingFraudControl() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .clickCancelButton();

        Allure.step("Verify that due to click Cancel button Fraud Control hasn't been deleted");
        assertThat(page.getTableControls().getRow(FRAUD_CONTROL.getControlName())).isAttached();

        page.getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control hasn't been deleted");
        assertThat(page.getTableControls().getRow(FRAUD_CONTROL.getControlName())).isAttached();

        page.getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .pressEscapeKey();

        Allure.step("Verify that due to press ESC keyboard button Fraud Control hasn't been deleted");
        assertThat(page.getTableControls().getRow(FRAUD_CONTROL.getControlName())).isAttached();
    }

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("987")
    @Epic("System/Fraud Control")
    @Feature("Control table")
    @Description("Deactivate Fraud Control with Cancel button"
            + "Deactivate Fraud Control with 'Cross'"
            + "Deactivate Fraud Control with ESC")
    public void testCancelDeactivationFraudControl() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName())
                .clickCancelButton();

        Locator statusCell = page.getTableControls().getCell(
                page.getTableControls().getRow(FRAUD_CONTROL.getControlName()),
                "Status");

        Allure.step("Verify that due to click Cancel button Fraud Control is still active");
        assertThat(statusCell).hasText("Active");

        page.getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName())
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control is still active");
        assertThat(statusCell).hasText("Active");

        page.getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName())
                .pressEscapeKey();

        Allure.step("Verify that due to press ESC keyboard button Fraud Control is still active");
        assertThat(statusCell).hasText("Active");
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

        Locator row = page.getTableControls().getRow(FRAUD_CONTROL_INACTIVE.getControlName());

        Allure.step("Verify that all the data are presented in the row");
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlCode());
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlConfig());
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlDisplayName());
        assertThat(row).containsText("Inactive");
    }

    @Ignore("https://github.com/NPGW/npgw-ui-test/issues/913")
    @Test(dependsOnMethods = "testAddInactiveFraudControl")
    @TmsLink("955")
    @Epic("System/Fraud Control")
    @Feature("Control table")
    @Description("Activate Fraud Control not added to Business Unit"
            + "Deactivate Fraud Control not added to Business Unit")
    public void testChangeControlActivityForFraudControlNotAddedToBusinessUnit() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickActivateControlButton(FRAUD_CONTROL_INACTIVE.getControlName())
                .clickActivateButton();

        Locator row = page.getTableControls().getRow(FRAUD_CONTROL_INACTIVE.getControlName());
        Locator cell = page.getTableControls().getCell(row, "Status");

        Allure.step("Verify that Fraud Control state now is Active");
        assertThat(cell).hasText("Active");

        page.getTableControls().clickDeactivateControlButton(FRAUD_CONTROL_INACTIVE.getControlName())
                .clickDeactivateButton();

        Allure.step("Verify that Fraud Control state now is Inactive");
        assertThat(cell).hasText("Inactive");
    }

    @Test(dependsOnMethods = {"testCancelAddingFraudControlToBusinessUnit",
            "testDeleteInactiveFraudControlAddedToBusinessUnit"})
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
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Locator rowFraudOne = page.getTableControls().getRow(FRAUD_CONTROL_ADD_ONE.getControlDisplayName());
        Locator rowFraudTwo = page.getTableControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());

        Allure.step("Verify that all the Fraud Controls are presented in Business Unit table");
        assertThat(rowFraudOne).containsText(FRAUD_CONTROL_ADD_ONE.getControlCode());
        assertThat(rowFraudOne).containsText(FRAUD_CONTROL_ADD_ONE.getControlConfig());
        assertThat(rowFraudOne).containsText("Active");
        assertThat(rowFraudTwo).containsText(FRAUD_CONTROL_ADD_TWO.getControlCode());
        assertThat(rowFraudTwo).containsText(FRAUD_CONTROL_ADD_TWO.getControlConfig());
        assertThat(rowFraudTwo).containsText("Active");
    }

    @Ignore("https://github.com/NPGW/npgw-ui-test/issues/913")
    @Test(dependsOnMethods = "testAddFraudControlToBusinessUnit")
    @TmsLink("967")
    @Epic("System/Fraud Control")
    @Feature("Control table")
    @Description("Activate Fraud Control added to Business Unit"
            + "Deactivate Fraud Control added to Business Unit")
    public void testChangeControlActivityForFraudControlAddedToBusinessUnit() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("0")
                .clickDeactivateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Locator controlCell = page.getTableControls().getCell(FRAUD_CONTROL_ADD_ONE.getControlName(), "Status");
        Locator businessControlRow = page.getTableBusinessUnitControls().getRowByDataKey("0");
        Locator businessControlCell = page.getTableBusinessUnitControls().getCell(businessControlRow, "Status");

        Allure.step("Verify that Fraud Control state hasn't been changed in Control Table");
        assertThat(controlCell).hasText("Active");

        Allure.step("Verify that Fraud Control state is Inactive now in Business Unit Control Table");
        assertThat(businessControlCell).hasText("Inactive");

        page.getTableBusinessUnitControls().clickActivateBusinessUnitControlButton("0")
                .clickActivateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify that Fraud Control state is Active in Business Unit Control Table again");
        assertThat(controlCell).hasText("Active");
        assertThat(businessControlCell).hasText("Active");
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
                .fillFraudControlNameField(FRAUD_CONTROL_NAME)
                .clickCreateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_NAME)
                .clickCreateButton();

        Allure.step("Verify that the error message ‘ERROR Entity with name … already exists.’ is displayed.");

        assertThat(fraudControlPage.getAlert().getMessage())
                .hasText("ERROREntity with name {" + FRAUD_CONTROL_NAME + "} already exists.");
    }

    @Test(dependsOnMethods = {"testCancelAddingFraudControlToBusinessUnit", "testCancelDeletingFraudControl"})
    @TmsLink("949")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Delete Active Fraud Control not added to Business Unit")
    public void testDeleteActiveFraudControlNotAddedToBusinessUnit() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .clickDeleteButton();

        Allure.step("Check if just deleted Fraud Control still presented in the table");
        try {
            page.getTableControls().getRow(FRAUD_CONTROL.getControlName());
        } catch (RuntimeException e) {
            throw new RuntimeException("There no rows with name "
                    + FRAUD_CONTROL.getControlName() + " in the table");
        }
    }

    @Test(dependsOnMethods = {"testAddFraudControlToBusinessUnit", "testChangeFraudControlPriority"})
    @TmsLink("950")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Delete Active Fraud Control added to Business Unit")
    public void testDeleteActiveFraudControlAddedToBusinessUnit() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("1")
                .clickDeleteButton();

        Allure.step("Check if just deleted Fraud Control still presented in both tables");
        try {
            page.getTableBusinessUnitControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());
            page.getTableControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlName());
        } catch (RuntimeException e) {
            throw new RuntimeException("There no rows with name "
                    + FRAUD_CONTROL_ADD_TWO.getControlName() + " in the table");
        }
    }

    @Test(dependsOnMethods = "testAddFraudControlToBusinessUnit")
    @TmsLink("960")
    @Epic("System/Fraud Control")
    @Feature("Business Unit Control table")
    @Description("Move Business unit control up" + "Move Business unit control down")
    public void testChangeFraudControlPriority() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableBusinessUnitControls().clickMoveBusinessUnitControlUpButton("1")
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Check that the second Fraud Control is '0' priority now");
        assertThat(page.getTableBusinessUnitControls().getRowByDataKey("0"))
                .containsText(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());

        page.getTableBusinessUnitControls().clickMoveBusinessUnitControlDownButton("0")
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Check that the second Fraud Control is '1' priority again");
        assertThat(page.getTableBusinessUnitControls().getRowByDataKey("0"))
                .containsText(FRAUD_CONTROL_ADD_ONE.getControlDisplayName());
        assertThat(page.getTableBusinessUnitControls().getRowByDataKey("1"))
                .containsText(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());
    }

    @Test
    @TmsLink("927")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Remove inactive Fraud control added to Business unit")
    public void testDeleteInactiveFraudControlAddedToBusinessUnit() {
        FraudControlPage fraudControlPage = new FraudControlPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_INACTIVE.getControlDisplayName())
                .clickConnectButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton(
                        "0")
                .clickDeleteButton();

        Allure.step("Verify the success message ‘SUCCESSBusiness unit control was deleted successfully'"
                + " is displayed");
        assertThat(fraudControlPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit control was deleted successfully");

        fraudControlPage.getAlert().waitUntilSuccessAlertIsGone();
        List<String> actualFraudControlBusinessUnitList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Display name");

        Allure.step("Verify that the business unit control table doesn't include the deleted control");
        Assert.assertFalse(actualFraudControlBusinessUnitList.contains(FRAUD_CONTROL_ADD_INACTIVE
                .getControlDisplayName()));
    }

    @Test(dependsOnMethods = "testDeleteActiveFraudControlAddedToBusinessUnit")
    @TmsLink("986")
    @Epic("System/Fraud Control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Edit Fraud Control")
    public void testEditFraudControl() {
        FraudControlPage page = new DashboardPage(getPage())
                .clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickEditControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL_ADD_TWO.getControlCode())
                .fillFraudControlConfigField(FRAUD_CONTROL_ADD_TWO.getControlConfig())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL_ADD_TWO.getControlDisplayName())
                .checkInactiveRadiobutton()
                .clickSaveChangesButton();

        Locator row = page.getTableControls().getRow(FRAUD_CONTROL_ADD_ONE.getControlName());
        Locator alertMessage = page.getAlert().getSuccessMessage();

        Allure.step("Verify that 'Control was update successfully' alert is appeared ");
        assertThat(alertMessage).hasText("SUCCESSControl was updated successfully");

        Allure.step("Verify that all the data are changed in the row" + FRAUD_CONTROL_ADD_ONE.getControlName());
        assertThat(row).containsText(FRAUD_CONTROL_ADD_TWO.getControlCode());
        assertThat(row).not().containsText(FRAUD_CONTROL_ADD_ONE.getControlCode());

        assertThat(row).containsText(FRAUD_CONTROL_ADD_TWO.getControlConfig());
        assertThat(row).not().containsText(FRAUD_CONTROL_ADD_ONE.getControlConfig());

        assertThat(row).containsText(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());
        assertThat(row).not().containsText(FRAUD_CONTROL_ADD_ONE.getControlDisplayName());

        assertThat(row).containsText("Inactive");
        assertThat(row).not().containsText("Active");
    }

    @AfterClass
    @Override
    protected void afterClass() {
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_INACTIVE.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_NAME);
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_ONE.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_TWO.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_INACTIVE.getControlName());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
