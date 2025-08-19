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
import org.testng.annotations.Test;
import xyz.npgw.test.common.base.BaseTest;
import xyz.npgw.test.common.entity.FraudControl;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.control.AddControlDialog;
import xyz.npgw.test.page.dialog.control.DeactivateControlDialog;
import xyz.npgw.test.page.dialog.control.EditControlDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;

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
    private static final FraudControl FRAUD_CONTROL_ADD_EMPTY_FIELDS = FraudControl.builder()
            .controlName("Control with empty display name")
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
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_EMPTY_FIELDS);
    }

    @Test
    @TmsLink("891")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Active Fraud Control")
    public void testAddActiveFraudControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName())
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig())
                .checkActiveRadiobutton()
                .clickSetupButton();

        Locator row = page.getTableControls().getRow(FRAUD_CONTROL.getControlName());

        Allure.step("Verify that all the data are presented in the row");
        assertThat(row).containsText(FRAUD_CONTROL.getControlCode());
        assertThat(row).containsText(FRAUD_CONTROL.getControlConfig());
        assertThat(row).containsText(FRAUD_CONTROL.getControlDisplayName());
        assertThat(row).containsText("Active");
    }

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("920")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Fraud Control to Business Unit with Cancel button"
            + "Add Fraud Control to Business Unit with 'Cross'"
            + "Add Fraud Control to Business Unit with ESC")
    public void testCancelAddingFraudControlToBusinessUnit() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Delete Fraud Control with Cancel button"
            + "Delete Fraud Control with 'Cross'"
            + "Delete Fraud Control with ESC")
    public void testCancelDeletingFraudControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Deactivate Fraud Control with Cancel button"
            + "Deactivate Fraud Control with 'Cross'"
            + "Deactivate Fraud Control with ESC")
    public void testCancelDeactivationFraudControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("999")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Edit Fraud Control with Cancel button"
            + "Edit Fraud Control with 'Cross'"
            + "Edit Fraud Control with ESC")
    public void testCancelEditingFraudControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickEditControlButton(FRAUD_CONTROL.getControlName())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName() + " Edited")
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode() + RUN_ID)
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig() + "Not applicable")
                .checkInactiveRadiobutton()
                .clickCloseButton();

        Locator controlRow = page.getTableControls().getRow(FRAUD_CONTROL.getControlName());

        Locator displayNameCell = page.getTableControls().getCell(controlRow, "Display name");
        Locator codeCell = page.getTableControls().getCell(controlRow, "Code");
        Locator configCell = page.getTableControls().getCell(controlRow, "Config");
        Locator statusCell = page.getTableControls().getCell(controlRow, "Status");

        Allure.step("Verify that due to click Close button Fraud Control hasn't been changed");
        assertThat(codeCell).hasText(FRAUD_CONTROL.getControlCode());
        assertThat(configCell).hasText(FRAUD_CONTROL.getControlConfig());
        assertThat(displayNameCell).hasText(FRAUD_CONTROL.getControlDisplayName());
        assertThat(statusCell).hasText("Active");

        page.getTableControls().clickEditControlButton(FRAUD_CONTROL.getControlName())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName() + " Edited")
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode() + RUN_ID)
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig() + "Not applicable")
                .checkInactiveRadiobutton()
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control hasn't been changed");
        assertThat(codeCell).hasText(FRAUD_CONTROL.getControlCode());
        assertThat(configCell).hasText(FRAUD_CONTROL.getControlConfig());
        assertThat(displayNameCell).hasText(FRAUD_CONTROL.getControlDisplayName());
        assertThat(statusCell).hasText("Active");

        page.getTableControls().clickEditControlButton(FRAUD_CONTROL.getControlName())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName() + " Edited")
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode() + RUN_ID)
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig() + "Not applicable")
                .checkInactiveRadiobutton()
                .pressEscapeKey();

        Allure.step("Verify that due to press ESC keyboard button Fraud Control hasn't been changed");
        assertThat(codeCell).hasText(FRAUD_CONTROL.getControlCode());
        assertThat(configCell).hasText(FRAUD_CONTROL.getControlConfig());
        assertThat(displayNameCell).hasText(FRAUD_CONTROL.getControlDisplayName());
        assertThat(statusCell).hasText("Active");
    }

    @Test(dependsOnMethods = {"testAddActiveFraudControl", "testAddInactiveFraudControl"})
    @TmsLink("1001")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Tooltips for available actions check")
    public void testTooltipsForActionsControlTable() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME);

        Locator editIconTooltip = page
                .getTableControls().hoverOverEditIcon(FRAUD_CONTROL.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Edit icon Tooltip is presented on Control table");
        assertThat(editIconTooltip).isVisible();
        assertThat(editIconTooltip).hasText("Edit control");

        Locator activateIconTooltip = page
                .getTableControls().hoverOverActivateControlIcon(FRAUD_CONTROL_INACTIVE.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Activate icon Tooltip is presented on Control table");
        assertThat(activateIconTooltip).isVisible();
        assertThat(activateIconTooltip).hasText("Activate control");

        Locator deactivateIconTooltip = page
                .getTableControls().hoverOverDeactivateControlIcon(FRAUD_CONTROL.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Deactivate icon Tooltip is presented on Control table");
        assertThat(deactivateIconTooltip).isVisible();
        assertThat(deactivateIconTooltip).hasText("Deactivate control");

        Locator deleteIconTooltip = page
                .getTableControls().hoverOverDeleteIcon(FRAUD_CONTROL.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Delete icon Tooltip is presented on Control table");
        assertThat(deleteIconTooltip).isVisible();
        assertThat(deleteIconTooltip).hasText("Delete control");

        Locator connectControlIconTooltip = page
                .getTableControls().hoverOverConnectControlIcon(FRAUD_CONTROL.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Connect control icon Tooltip is presented on Control table");
        assertThat(connectControlIconTooltip).isVisible();
        assertThat(connectControlIconTooltip).hasText("Connect control to business unit");
    }

    @Test(dependsOnMethods = {"testBusinessUnitControlTableEntriesSorting"})
    @TmsLink("1123")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Tooltips for available actions check")
    public void testTooltipsForActionsBusinessUnitControlTable() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME);

        Locator moveControlDownIcon = page
                .getTableBusinessUnitControls().hoverOverMoveControlDownIcon("0")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Move Business Control Down icon Tooltip is presented on Business Control table");
        assertThat(moveControlDownIcon).isVisible();
        assertThat(moveControlDownIcon).hasText("Move business unit control down");

        Locator moveControlUpIcon = page
                .getTableBusinessUnitControls().hoverOverMoveControlUpIcon("0")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Move Business Control Up icon Tooltip is presented on Business Control table");
        assertThat(moveControlUpIcon).isVisible();
        assertThat(moveControlUpIcon).hasText("Move business unit control up");

        Locator activateIconTooltip = page
                .getTableBusinessUnitControls().hoverOverActivateControlIcon("0")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Activate icon Tooltip is presented on Business Unit Control table");
        assertThat(activateIconTooltip).isVisible();
        assertThat(activateIconTooltip).hasText("Аctivate business unit control");

        Locator deactivateIconTooltip = page
                .getTableBusinessUnitControls().hoverOverDeactivateControlIcon("1")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Deactivate icon Tooltip is presented on Business Unit Control table");
        assertThat(deactivateIconTooltip).isVisible();
        assertThat(deactivateIconTooltip).hasText("Deactivate business unit control");

        Locator deleteIconTooltip = page
                .getTableBusinessUnitControls().hoverOverDeleteIcon("0")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Delete icon Tooltip is presented on Business Unit Control table");
        assertThat(deleteIconTooltip).isVisible();
        assertThat(deleteIconTooltip).hasText("Delete business unit control");
    }

    @Test(dependsOnMethods = {"testCancelAddingFraudControlToBusinessUnit", "testCancelDeletingFraudControl",
            "testCancelDeactivationFraudControl", "testCancelEditingFraudControl",
            "testTooltipsForActionsControlTable", "testBusinessUnitControlTableEntriesSorting",
            "testVerifyWarningModalWindowChangeActivityForControlTable"})
    @TmsLink("949")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Delete Active Fraud Control not added to Business Unit")
    public void testDeleteActiveFraudControlNotAddedToBusinessUnit() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .clickDeleteButton();

        Allure.step("Check if just deleted Fraud Control still presented in the table");
        assertThat(page.getTableControls().getRow(FRAUD_CONTROL.getControlName())).not().isAttached();
    }

    @Test
    @TmsLink("904")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Inactive Fraud Control")
    public void testAddInactiveFraudControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_INACTIVE.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL_INACTIVE.getControlCode())
                .fillFraudControlConfigField(FRAUD_CONTROL_INACTIVE.getControlConfig())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL_INACTIVE.getControlDisplayName())
                .checkInactiveRadiobutton()
                .clickSetupButton();

        Locator row = page.getTableControls().getRow(FRAUD_CONTROL_INACTIVE.getControlName());

        Allure.step("Verify that all the data are presented in the row");
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlCode());
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlConfig());
        assertThat(row).containsText(FRAUD_CONTROL_INACTIVE.getControlDisplayName());
        assertThat(row).containsText("Inactive");
    }

    @Test(dependsOnMethods = "testAddInactiveFraudControl")
    @TmsLink("955")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Activate Fraud Control not added to Business Unit"
            + "Deactivate Fraud Control not added to Business Unit")
    public void testChangeControlActivityForFraudControlNotAddedToBusinessUnit() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Fraud Control to Business Unit (No Fraud Control)"
            + "Add Fraud Control to Business Unit (Business unit has Fraud Control)")
    public void testAddFraudControlToBusinessUnit() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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

    @Test(dependsOnMethods = "testAddFraudControlToBusinessUnit")
    @TmsLink("967")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Activate Fraud Control added to Business Unit"
            + "Deactivate Fraud Control added to Business Unit")
    public void testChangeControlActivityForFraudControlAddedToBusinessUnit() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME);

        Locator controlStatusCell = page.getTableControls().getCell(FRAUD_CONTROL_ADD_ONE.getControlName(), "Status");
        Locator businessControlRow = page.getTableBusinessUnitControls().getRowByDataKey("0");
        Locator businessControlStatusCell = page.getTableBusinessUnitControls().getCell(businessControlRow, "Status");

        Allure.step("Verify Fraud Control's current status in Business Unit Control Table");
        assertThat(businessControlStatusCell).hasText("Active");

        Allure.step("Verify Fraud Control's current status in Control Table");
        assertThat(controlStatusCell).hasText("Active");

        page.getTableControls().clickDeactivateControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickDeactivateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify that Fraud Control status has been changed to 'Inactive' in Control Table");
        assertThat(controlStatusCell).hasText("Inactive");

        Allure.step("Verify that Fraud Control status hasn't been changed in Business Unit Control Table");
        assertThat(businessControlStatusCell).hasText("Active");

        page.getTableControls().clickActivateControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickActivateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify that Fraud Control status is Active in Control table again");
        assertThat(controlStatusCell).hasText("Active");
        assertThat(businessControlStatusCell).hasText("Active");
    }

    @Test(dependsOnMethods = "testAddFraudControlToBusinessUnit")
    @TmsLink("1009")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Activate Business unit control" + "Deactivate Business unit control")
    public void testChangeBusinessUnitFraudControlActivity() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME);

        Locator controlStatusCell = page.getTableControls().getCell(FRAUD_CONTROL_ADD_ONE.getControlName(), "Status");
        Locator businessControlRow = page.getTableBusinessUnitControls().getRowByDataKey("0");
        Locator businessControlStatusCell = page.getTableBusinessUnitControls().getCell(businessControlRow, "Status");

        Allure.step("Verify Fraud Control's current status in Business Unit Control Table");
        assertThat(businessControlStatusCell).hasText("Active");

        Allure.step("Verify Fraud Control's current status in Control Table");
        assertThat(controlStatusCell).hasText("Active");

        page.getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("0")
                .clickDeactivateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify that Fraud Control state hasn't been changed in Control Table");
        assertThat(controlStatusCell).hasText("Active");

        Allure.step("Verify that Fraud Control state is Inactive now in Business Unit Control Table");
        assertThat(businessControlStatusCell).hasText("Inactive");

        page.getTableBusinessUnitControls().clickActivateBusinessUnitControlButton("0")
                .clickActivateButton()
                .getAlert().waitUntilSuccessAlertIsGone();

        Allure.step("Verify that Fraud Control state is Active in Business Unit Control Table again");
        assertThat(controlStatusCell).hasText("Active");
        assertThat(businessControlStatusCell).hasText("Active");
    }

    @Test
    @TmsLink("1024")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Verify the 'Control name' field is mandatory and marked with '*'")
    public void testControlNameIsMandatory() {
        AddControlDialog addControlDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl();

        Allure.step("Verify that the 'Control name' field is marked with '*'");
        Assert.assertTrue(((String) addControlDialog.getControlNameLabel()
                        .evaluate("el => getComputedStyle(el, '::after').content")).contains("*"),
                "The '*' symbol is not displayed in the 'Control name' label");

        Allure.step("Verify that the 'Create' button is disabled if the 'Control name field is empty");
        assertThat(addControlDialog.getSetupButton()).hasAttribute("data-disabled", "true");
    }

    @Test
    @TmsLink("895")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Verify the error message when attempting to create a Fraud Control with the existing name")
    public void testErrorMessageForExistingControlName() {
        SuperFraudControlPage fraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_NAME)
                .clickSetupButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_NAME)
                .clickSetupButton();

        Allure.step("Verify that the error message ‘ERROR Entity with name … already exists.’ is displayed.");

        assertThat(fraudControlPage.getAlert().getMessage())
                .hasText("ERROREntity with name {" + FRAUD_CONTROL_NAME + "} already exists.");
    }

    @Test(dependsOnMethods = {"testAddFraudControlToBusinessUnit", "testChangeFraudControlPriority",
            "testChangeBusinessUnitFraudControlActivity", "testChangeBusinessControlPriorityRestrictions"})
    @TmsLink("950")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Delete Active Fraud Control added to Business Unit")
    public void testDeleteActiveFraudControlAddedToBusinessUnit() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("1")
                .clickDeleteButton();

        Allure.step("Check if just deleted Fraud Control still presented in both tables");
        assertThat(page.getTableBusinessUnitControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlDisplayName()))
                .not().isAttached();
        assertThat(page.getTableControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlName())).isAttached();
    }

    @Test(dependsOnMethods = "testAddFraudControlToBusinessUnit")
    @TmsLink("960")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Move Business unit control up" + "Move Business unit control down")
    public void testChangeFraudControlPriority() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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
        SuperFraudControlPage fraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickActivateControlButton(FRAUD_CONTROL_ADD_INACTIVE.getControlName())
                .clickActivateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_INACTIVE.getControlName())
                .clickConnectButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("0")
                .clickDeactivateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
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

        fraudControlPage.getTableControls().clickDeactivateControlButton(FRAUD_CONTROL_ADD_INACTIVE.getControlName())
                .clickDeactivateButton();
    }

    @Test(dependsOnMethods = {"testDeleteActiveFraudControlAddedToBusinessUnit",
            "testChangeBusinessUnitFraudControlActivity"})
    @TmsLink("986")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Edit Fraud Control")
    public void testEditFraudControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
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

    @Test(dependsOnMethods = {"testDeleteActiveFraudControlAddedToBusinessUnit",
            "testChangeBusinessUnitFraudControlActivity"})
    @TmsLink("993")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Verify,Control name is immutable")
    public void testNotEditControlName() {
        EditControlDialog editControlDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls()
                .clickEditControlButton(FRAUD_CONTROL_ADD_ONE.getControlName());

        Allure.step("Verify that 'Control Name' input field is immutable");
        assertThat(editControlDialog.getControlNameInput()).not().isEditable();
    }

    @Test
    @TmsLink("969")
    @Epic("System/Fraud control")
    @Feature("Control table entries sorting")
    @Description("Verify that entries can be sorted by Name, DisplayName, Code, Config, Status  in Asc and Desc order")
    public void testControlTableEntriesSorting() {
        SuperFraudControlPage fraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab();

        List<String> defaultColumnValues = fraudControlPage
                .getTableControls().getColumnValuesFromAllPages("Name");

        Allure.step("Entries are by default sorted ascending by Name");
        Assert.assertEquals(defaultColumnValues, defaultColumnValues.stream().sorted().toList());

        List<String> columns = List.of(/*"Type", */"Display name", "Name", "Code", "Config" /*, "Status"*/);
        //TODO sort by "Type" broken --- atm not sorting - [Fraud Screen, BIN Check, Fraud Screen] on first click
        //TODO sort by "Status" broken --- atm wrong order - [Inactive, Active] on first click

        columns.forEach(columnName -> {
            List<String> columnValues = fraudControlPage
                    .getTableControls().clickColumnHeader(columnName)
                    .getTableControls().getColumnValuesFromAllPages(columnName);

            Allure.step("Verify that entries are now sorted ascending by %s".formatted(columnName));
            Assert.assertEquals(columnValues, columnValues.stream().sorted().toList());

            columnValues = fraudControlPage
                    .getTableControls().clickColumnHeader(columnName)
                    .getTableControls().getColumnValuesFromAllPages(columnName);

            Allure.step("Verify that entries are now sorted descending by %s".formatted(columnName));
            Assert.assertEquals(columnValues, columnValues.stream().sorted(Comparator.reverseOrder()).toList());
        });
    }

    @Test(dependsOnMethods = {"testAddActiveFraudControl", "testEditFraudControl",
            "testPriorityIconsDisableForOnlyOneBusinessUnitControl"})
    @TmsLink("1013")
    @Epic("System/Fraud control")
    @Feature("Control business unit controls table entries sorting")
    @Description("Verify that entries can be sorted by Priority, DisplayName, Code, Config, Status"
            + "in Asc and Desc order")
    public void testBusinessUnitControlTableEntriesSorting() {
        SuperFraudControlPage fraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickActivateControlButton(FRAUD_CONTROL_ADD_INACTIVE.getControlName())
                .clickActivateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_INACTIVE.getControlName())
                .clickConnectButton()
                .getTableControls().clickConnectControlButton(
                        FRAUD_CONTROL_ADD_EMPTY_FIELDS.getControlName())
                .clickConnectButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("0")
                .clickDeactivateButton()
                .getAlert().waitUntilSuccessAlertIsGone()
                .getTableBusinessUnitControls().clickColumnHeader("Priority");

        List<String> actualPriorityList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Priority");
        List<String> sortedPriorityListDesc = new ArrayList<>(actualPriorityList);
        sortedPriorityListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Priority in Desc order ");
        Assert.assertEquals(actualPriorityList, sortedPriorityListDesc,
                "Priority column is not sorted in descending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Priority");
        actualPriorityList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Priority");

        List<String> sortedPriorityListAsc = new ArrayList<>(actualPriorityList);
        Collections.sort(sortedPriorityListAsc);

        Allure.step("Verify that entries are sorted by Priority in Asc order ");
        Assert.assertEquals(actualPriorityList, sortedPriorityListAsc,
                "Priority column is not sorted in ascending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Display name");
        List<String> actualDisplayNameList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Display name");
        List<String> sortedDisplayNameListAsc = new ArrayList<>(actualDisplayNameList);
        Collections.sort(sortedDisplayNameListAsc);

        Allure.step("Verify that entries are sorted by Display name in Asc order ");
        Assert.assertEquals(actualDisplayNameList, sortedDisplayNameListAsc,
                "Display name column is not sorted in ascending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Display name");
        actualDisplayNameList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Display name");
        List<String> sortedDisplayNameListDesc = new ArrayList<>(actualDisplayNameList);
        sortedDisplayNameListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Display name in Desc order ");
        Assert.assertEquals(actualDisplayNameList, sortedDisplayNameListDesc,
                "Display name column is not sorted in descending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Code");
        List<String> actualCodeList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Code");
        List<String> sortedCodeListAsc = new ArrayList<>(actualCodeList);
        Collections.sort(sortedCodeListAsc);

        Allure.step("Verify that entries are sorted by Code in Asc order ");
        Assert.assertEquals(actualCodeList, sortedCodeListAsc,
                "Code column is not sorted in ascending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Code");
        actualCodeList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Code");
        List<String> sortedCodeListDesc = new ArrayList<>(actualCodeList);
        sortedCodeListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Code in Desc order ");
        Assert.assertEquals(actualCodeList, sortedCodeListDesc,
                "Code column is not sorted in descending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Config");
        List<String> actualConfigList = fraudControlPage.getTableBusinessUnitControls().getColumnValues("Config");
        List<String> sortedConfigListAsc = new ArrayList<>(actualConfigList);
        Collections.sort(sortedConfigListAsc);

        Allure.step("Verify that entries are sorted by Config in Asc order ");
        Assert.assertEquals(actualConfigList, sortedConfigListAsc,
                "Config column is not sorted in ascending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Config");
        actualConfigList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Config");
        List<String> sortedConfigListDesc = new ArrayList<>(actualConfigList);
        sortedConfigListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Config in Desc order ");
        Assert.assertEquals(actualConfigList, sortedConfigListDesc,
                "Config column is not sorted in descending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Status");
        List<String> actualStatusList = fraudControlPage.getTableBusinessUnitControls().getColumnValues("Status");
        List<String> sortedStatusListDesc = new ArrayList<>(actualStatusList);
        sortedStatusListDesc.sort(Collections.reverseOrder());

        Allure.step("Verify that entries are sorted by Status in Desc order ");
        Assert.assertEquals(actualStatusList, sortedStatusListDesc,
                "Status column is not sorted in descending order");

        fraudControlPage
                .getTableBusinessUnitControls().clickColumnHeader("Status");
        actualStatusList = fraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Status");
        List<String> sortedStatusListAsc = new ArrayList<>(actualStatusList);
        Collections.sort(sortedStatusListAsc);

        Allure.step("Verify that entries are sorted by Status in Asc order ");
        Assert.assertEquals(actualStatusList, sortedStatusListAsc,
                "Status column is not sorted in ascending order");

        fraudControlPage.getTableControls().clickDeactivateControlButton(FRAUD_CONTROL_ADD_INACTIVE.getControlName())
                .clickDeactivateButton();
    }

    @Test
    @TmsLink("1005")
    @Epic("System/Fraud control")
    @Feature("Reset filter")
    @Description("'Reset filter' clears selected options")
    public void testResetFilter() {
        SuperFraudControlPage fraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME_FOR_TEST_RUN)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_FOR_TEST_RUN)
                .clickResetFilterButton();

        Allure.step("Verify: the selected company field is empty after reset");
        assertThat(fraudControlPage.getSelectCompany().getSelectCompanyField()).isEmpty();

        Allure.step("Verify: the selected business unit field is empty after reset");
        assertThat(fraudControlPage.getSelectBusinessUnit().getSelectBusinessUnitField()).isEmpty();
    }

    @Test(dependsOnMethods = "testAddFraudControlToBusinessUnit")
    @TmsLink("1018")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Move the first Business unit control up" + "Move the last Business unit control down")
    public void testChangeBusinessControlPriorityRestrictions() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableBusinessUnitControls().clickMoveBusinessUnitControlUpButton("0");

        Allure.step("Check that the warning message is presented due to not moving up");
        assertThat(page.getAlert().getMessage()).containsText("Error changing business unit control priority");

        Allure.step("Check that no changes has been performed at this stage");
        assertThat(page.getTableBusinessUnitControls().getCell(0, "Display name"))
                .hasText(FRAUD_CONTROL_ADD_ONE.getControlDisplayName());
        assertThat(page.getTableBusinessUnitControls().getCell(1, "Display name"))
                .hasText(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());

        page.getTableBusinessUnitControls().clickMoveBusinessUnitControlDownButton("1");

        Allure.step("Check that the warning message is presented due to not moving down");
        assertThat(page.getAlert().getMessage()).containsText("Error changing business unit control priority");

        Allure.step("Check that no changes has been performed at the end");
        assertThat(page.getTableBusinessUnitControls().getCell(0, "Display name"))
                .hasText(FRAUD_CONTROL_ADD_ONE.getControlDisplayName());
        assertThat(page.getTableBusinessUnitControls().getCell(1, "Display name"))
                .hasText(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());
    }

    @Test(dependsOnMethods = {"testDeleteActiveFraudControlAddedToBusinessUnit"})
    @TmsLink("1064")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Priority icons for only one Business unit control")
    public void testPriorityIconsDisableForOnlyOneBusinessUnitControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME);

        Allure.step("Verify Up button is disabled on Business Unit Control table because of one control");
        assertThat(page.getTableBusinessUnitControls().getMoveBusinessUnitControlUpButton("0")).isDisabled();

        Allure.step("Verify Down button is disabled on Business Unit Control table because of one control");
        assertThat(page.getTableBusinessUnitControls().getMoveBusinessUnitControlDownButton("0")).isDisabled();
    }

    @Test
    @TmsLink("1079")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Verify that error message is displayed when special symbols are entered in the Control name field"
            + " after clicking create button")
    public void testSpecialSymbolsInControlNameNotAllowed() {
        List<String> invalidSymbols = Arrays.asList(
                "@", "#", "$", "%", "*", "=", "/", "\\", ":", "|", "^", "~", "!", "\"", "<", ">", "[", "]", "{", "}",
                "?", "(", ")", "_", "`", "+", ";"
        );

        AddControlDialog addControlDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl();

        for (String symbol : invalidSymbols) {
            String input = "a" + symbol.repeat(4);
            String expectedMessage = "ERRORInvalid name: '" + input + "'. It may only contain letters, digits,"
                    + " ampersands (&), hyphens (-), commas (,), periods (.), apostrophes ('), and spaces.";

            addControlDialog
                    .fillFraudControlNameField(input)
                    .clickSetupButton();

            Allure.step("Verify error message for symbol: " + symbol);
            assertThat(addControlDialog.getAlert().getMessage()).containsText(expectedMessage);
        }
    }

    @Test
    @TmsLink("1091")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Verify that the Control Name field requires between 4 and 100 characters")
    public void testControlNameLengthRestrictions() {
        String invalidControlName3Chars = "a".repeat(3);
        String validControlName4Chars = "a".repeat(4);
        String validControlName100Chars = "a".repeat(100);
        String invalidControlName101Chars = "a".repeat(101);

        AddControlDialog addControlDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(invalidControlName3Chars);

        String ariaInvalid = addControlDialog.getControlNameInput().getAttribute("aria-invalid");

        Allure.step("Verify that the 'Control Name' field is highlighted in red");
        Assert.assertEquals(ariaInvalid, "true", "The 'Control Name' field should be"
                + " highlighted in red");

        Allure.step("Verify that the Setup button is disabled if Control name contains 3 characters");
        assertThat(addControlDialog.getSetupButton()).isDisabled();

        addControlDialog
                .fillFraudControlNameField(validControlName4Chars);

        Allure.step("Verify that the Setup button is enabled if  Control name contains 4 characters");
        assertThat(addControlDialog.getSetupButton()).isEnabled();

        addControlDialog
                .fillFraudControlNameField(validControlName100Chars);

        Allure.step("Verify that the Setup button is enabled if Control name contains 100 characters");
        assertThat(addControlDialog.getSetupButton()).isEnabled();

        addControlDialog
                .fillFraudControlNameField(invalidControlName101Chars);

        Allure.step("Verify that a Control name field has limit of 100 characters");
        Assert.assertEquals(addControlDialog.getControlNameInput().inputValue().length(), 100);
    }

    @Test(dependsOnMethods = {"testAddActiveFraudControl", "testAddInactiveFraudControl"})
    @TmsLink("1143")
    @Epic("System/Fraud control")
    @Feature("Change control activity dialog Control table")
    @Description("Warning message text")
    public void testVerifyWarningModalWindowChangeActivityForControlTable() {
        DeactivateControlDialog dialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName());

        Locator windowHeader = dialog.getModalWindowHeaderTitle();
        Locator windowMainBody = dialog.getModalWindowsMainTextBody();

        Allure.step("Verify Deactivate control modal window text");
        assertThat(windowHeader).hasText("Change control activity");

        Allure.step("Verify Deactivate control modal window main body text");
        assertThat(windowMainBody).hasText("Are you sure you want to deactivate control "
                + FRAUD_CONTROL.getControlDisplayName() + "?");

        dialog.clickCancelButton()
                .getTableControls().clickActivateControlButton(FRAUD_CONTROL_INACTIVE.getControlName());

        Allure.step("Verify Activate control modal window text");
        assertThat(windowHeader).hasText("Change control activity");

        Allure.step("Verify Activate control modal window main body text");
        assertThat(windowMainBody).hasText("Are you sure you want to activate control "
                + FRAUD_CONTROL_INACTIVE.getControlDisplayName() + "?");

        dialog.clickCancelButton();
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
        TestUtils.deleteFraudControl(getApiRequestContext(),
                FRAUD_CONTROL_ADD_EMPTY_FIELDS.getControlName());
        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
