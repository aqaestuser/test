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
import xyz.npgw.test.common.entity.ControlType;
import xyz.npgw.test.common.entity.FraudControl;
import xyz.npgw.test.common.util.TestUtils;
import xyz.npgw.test.page.dashboard.SuperDashboardPage;
import xyz.npgw.test.page.dialog.control.ActivateBusinessUnitControlDialog;
import xyz.npgw.test.page.dialog.control.AddControlDialog;
import xyz.npgw.test.page.dialog.control.DeactivateBusinessUnitControlDialog;
import xyz.npgw.test.page.dialog.control.DeactivateControlDialog;
import xyz.npgw.test.page.dialog.control.EditControlDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static xyz.npgw.test.common.Constants.BUSINESS_UNIT_FOR_TEST_RUN;
import static xyz.npgw.test.common.Constants.COMPANY_NAME_FOR_TEST_RUN;

public class FraudControlTest extends BaseTest {

    private static final FraudControl FRAUD_CONTROL = FraudControl.builder()
            .controlName("%s ControlEverything".formatted(RUN_ID))
            .controlCode("Neutrino")
            .controlDisplayName("ControlDisplay")
            .controlConfig("notDefault")
            .build();
    private static final FraudControl FRAUD_CONTROL_FRAUD_SCREEN = FraudControl.builder()
            .controlName("%s ControlScreen".formatted(RUN_ID))
            .controlType(String.valueOf(ControlType.FRAUD_SCREEN))
            .controlCode("Neutrino")
            .controlDisplayName("ControlFSC")
            .controlConfig("type")
            .build();
    private static final FraudControl FRAUD_CONTROL_INACTIVE = FraudControl.builder()
            .controlName("%s ControlNothing".formatted(RUN_ID))
            .controlCode("Neutrino")
            .controlDisplayName("DisplayNotAvailable")
            .controlConfig("suspicious")
            .isActive(false)
            .build();
    private static final FraudControl FRAUD_CONTROL_ADD_ONE = FraudControl.builder()
            .controlName("%s ControlOne".formatted(RUN_ID))
            .controlCode("Neutrino")
            .controlDisplayName("ControlDisplayFirst")
    //        .controlCode("1234")
            .controlConfig("firstQueue")
            .build();
    private static final FraudControl FRAUD_CONTROL_ADD_TWO = FraudControl.builder()
            .controlName("%s ControlTwo".formatted(RUN_ID))
            .controlCode("Neutrino")
            .controlDisplayName("ControlDisplaySecond")
    //        .controlCode("2345")
            .controlConfig("secondQueue")
            .build();
    private static final FraudControl FRAUD_CONTROL_ADD_INACTIVE = FraudControl.builder()
            .controlName("%s Inactive control".formatted(RUN_ID))
            .controlCode("Neutrino")
            .controlDisplayName("Inactive control")
            .isActive(false)
            .controlConfig("firstQueue")
            .build();
    private static final FraudControl FRAUD_CONTROL_THREE = FraudControl.builder()
            .controlName("%s ControlThree".formatted(RUN_ID))
            .controlCode("Neutrino")
            .controlType(String.valueOf(ControlType.FRAUD_SCREEN))
            .controlDisplayName("ControlDisplayThird")
            .build();
    private static final FraudControl FRAUD_CONTROL_ACTIVE_TO_INACTIVE = FraudControl.builder()
            .controlName("%s ControlActiveToInactive".formatted(RUN_ID))
            .controlCode("Neutrino")
            .controlType(String.valueOf(ControlType.FRAUD_SCREEN))
            .controlDisplayName("ControlDisplayActiveToInactive")
            .build();
    private static final String FRAUD_CONTROL_NAME = "%S Test fraudControl name".formatted(RUN_ID);

    private static final String COMPANY_NAME = "%s company to bend Fraud Control".formatted(RUN_ID);
    private static final String BUSINESS_UNIT_NAME = "Business unit %s".formatted(RUN_ID);
    private static final String BUSINESS_UNIT_SORT = "Business unit sort %s".formatted(RUN_ID);

    @BeforeClass
    @Override
    protected void beforeClass() {
        super.beforeClass();
        TestUtils.createCompany(getApiRequestContext(), COMPANY_NAME);
        TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_NAME, BUSINESS_UNIT_NAME);
        TestUtils.createBusinessUnit(getApiRequestContext(), COMPANY_NAME, BUSINESS_UNIT_SORT);

        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_ONE);
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_TWO);
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ADD_INACTIVE);
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_THREE);
        TestUtils.createFraudControl(getApiRequestContext(), FRAUD_CONTROL_ACTIVE_TO_INACTIVE);
    }

    @Test
    @TmsLink("891")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Active Fraud Control")
    public void testAddActiveFraudControl() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName())
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig())
                .checkActiveRadiobutton()
                .clickSetupButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify that all the data are presented in the row");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName()))
                .hasText(FRAUD_CONTROL.toString());
    }

    @Test
    @TmsLink("1154")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Active Fraud Control with Fraud Screen type")
    public void testAddActiveFraudControlWithFraudScreenType() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_FRAUD_SCREEN.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL_FRAUD_SCREEN.getControlCode())
                .selectFraudControlTypeField(ControlType.FRAUD_SCREEN)
                .fillFraudControlDisplayNameField(FRAUD_CONTROL_FRAUD_SCREEN.getControlDisplayName())
                .fillFraudControlConfigField(FRAUD_CONTROL_FRAUD_SCREEN.getControlConfig())
                .checkActiveRadiobutton()
                .clickSetupButton();

        Allure.step("Verify that all the data are presented in the row");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL_FRAUD_SCREEN.getControlName()))
                .hasText(FRAUD_CONTROL_FRAUD_SCREEN.toString());
    }

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("920")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Fraud Control to Business Unit with Cancel button"
            + "Add Fraud Control to Business Unit with 'Cross'"
            + "Add Fraud Control to Business Unit with ESC")
    public void testCancelAddingFraudControlToBusinessUnit() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL.getControlName())
                .clickCancelButton();

        Allure.step("Verify that due to click Cancel button Fraud Control hasn't been added");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getNoRowsToDisplayMessage()).isAttached();

        superFraudControlPage
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL.getControlName())
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control hasn't been added");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getNoRowsToDisplayMessage()).isAttached();

        superFraudControlPage
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL.getControlName())
                .pressEscapeKey();

        Allure.step("Verify that due to press ESC keyboard button Fraud Control hasn't been added");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getNoRowsToDisplayMessage()).isAttached();
    }

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("972")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Delete Fraud Control with Cancel button"
            + "Delete Fraud Control with 'Cross'"
            + "Delete Fraud Control with ESC")
    public void testCancelDeletingFraudControl() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .clickCancelButton();

        Allure.step("Verify that due to click Cancel button Fraud Control hasn't been deleted");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName())).isAttached();

        superFraudControlPage
                .getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control hasn't been deleted");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName())).isAttached();

        superFraudControlPage
                .getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .pressEscapeKey();

        Allure.step("Verify that due to press ESC keyboard button Fraud Control hasn't been deleted");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName())).isAttached();
    }

    @Test(dependsOnMethods = "testAddActiveFraudControl")
    @TmsLink("987")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Deactivate Fraud Control with Cancel button"
            + "Deactivate Fraud Control with 'Cross'"
            + "Deactivate Fraud Control with ESC")
    public void testCancelDeactivationFraudControl() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName())
                .clickCancelButton();

        Locator statusCell = superFraudControlPage
                .getTableControls().getCell(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName()), "Status");

        Allure.step("Verify that due to click Cancel button Fraud Control is still active");
        assertThat(statusCell).hasText("Active");

        superFraudControlPage
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName())
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control is still active");
        assertThat(statusCell).hasText("Active");

        superFraudControlPage
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName())
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
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickEditControlButton(FRAUD_CONTROL.getControlName())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName() + " Edited")
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode() + RUN_ID)
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig() + "Not applicable")
                .checkInactiveRadiobutton()
                .clickCloseButton();

        Allure.step("Verify that due to click Close button Fraud Control hasn't been changed");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName()))
                .hasText(FRAUD_CONTROL.toString());

        superFraudControlPage
                .getTableControls().clickEditControlButton(FRAUD_CONTROL.getControlName())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName() + " Edited")
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode() + RUN_ID)
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig() + "Not applicable")
                .checkInactiveRadiobutton()
                .clickCloseIcon();

        Allure.step("Verify that due to click Cross icon Fraud Control hasn't been changed");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName()))
                .hasText(FRAUD_CONTROL.toString());

        superFraudControlPage
                .getTableControls().clickEditControlButton(FRAUD_CONTROL.getControlName())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL.getControlDisplayName() + " Edited")
                .fillFraudControlCodeField(FRAUD_CONTROL.getControlCode() + RUN_ID)
                .fillFraudControlConfigField(FRAUD_CONTROL.getControlConfig() + "Not applicable")
                .checkInactiveRadiobutton()
                .pressEscapeKey();

        Allure.step("Verify that due to press ESC keyboard button Fraud Control hasn't been changed");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName()))
                .hasText(FRAUD_CONTROL.toString());
    }

    @Ignore("no tooltips atm")
    @Test(dependsOnMethods = {"testAddActiveFraudControl", "testAddInactiveFraudControl"})
    @TmsLink("1001")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Tooltips for available actions check")
    public void testTooltipsForActionsControlTable() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME);

        Locator editIconTooltip = superFraudControlPage
                .getTableControls().hoverOverEditIcon(FRAUD_CONTROL.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Edit icon Tooltip is presented on Control table");
        assertThat(editIconTooltip).isVisible();

        assertThat(editIconTooltip).hasText("Edit control");

        Locator activateIconTooltip = superFraudControlPage
                .getTableControls().hoverOverActivateControlIcon(FRAUD_CONTROL_INACTIVE.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Activate icon Tooltip is presented on Control table");
        assertThat(activateIconTooltip).isVisible();
        assertThat(activateIconTooltip).hasText("Activate control");

        Locator deactivateIconTooltip = superFraudControlPage
                .getTableControls().hoverOverDeactivateControlIcon(FRAUD_CONTROL.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Deactivate icon Tooltip is presented on Control table");
        assertThat(deactivateIconTooltip).isVisible();
        assertThat(deactivateIconTooltip).hasText("Deactivate control");

        Locator deleteIconTooltip = superFraudControlPage
                .getTableControls().hoverOverDeleteIcon(FRAUD_CONTROL.getControlName())
                .getTableControls().getTooltip();

        Allure.step("Verify that Delete icon Tooltip is presented on Control table");
        assertThat(deleteIconTooltip).isVisible();
        assertThat(deleteIconTooltip).hasText("Delete control");

        Locator connectControlIconTooltip = superFraudControlPage
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
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_SORT);

        Locator moveControlDownIcon = superFraudControlPage
                .getTableBusinessUnitControls().hoverOverMoveControlDownIcon("1")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Move Business Control Down icon Tooltip is presented on Business Control table");
        assertThat(moveControlDownIcon).isVisible();
        assertThat(moveControlDownIcon).hasText("Move business unit control down");

        Locator moveControlUpIcon = superFraudControlPage
                .getTableBusinessUnitControls().hoverOverMoveControlUpIcon("1")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Move Business Control Up icon Tooltip is presented on Business Control table");
        assertThat(moveControlUpIcon).isVisible();
        assertThat(moveControlUpIcon).hasText("Move business unit control up");

        Locator activateIconTooltip = superFraudControlPage
                .getTableBusinessUnitControls().hoverOverActivateControlIcon("1")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Activate icon Tooltip is presented on Business Unit Control table");
        assertThat(activateIconTooltip).isVisible();
        assertThat(activateIconTooltip).hasText("Аctivate business unit control"); //TODO BUG ru letter

        Locator deactivateIconTooltip = superFraudControlPage
                .getTableBusinessUnitControls().hoverOverDeactivateControlIcon("0")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Deactivate icon Tooltip is presented on Business Unit Control table");
        assertThat(deactivateIconTooltip).isVisible();
        assertThat(deactivateIconTooltip).hasText("Deactivate business unit control");

        Locator deleteIconTooltip = superFraudControlPage
                .getTableBusinessUnitControls().hoverOverDeleteIcon("0")
                .getTableBusinessUnitControls().getTooltip();

        Allure.step("Verify that Delete icon Tooltip is presented on Business Unit Control table");
        assertThat(deleteIconTooltip).isVisible();
        assertThat(deleteIconTooltip).hasText("Delete business unit control");
    }

    @Test(dependsOnMethods = {"testCancelAddingFraudControlToBusinessUnit", "testCancelDeletingFraudControl",
            "testCancelDeactivationFraudControl", "testCancelEditingFraudControl",
            /*"testTooltipsForActionsControlTable", "testBusinessUnitControlTableEntriesSorting",*/
            "testVerifyWarningModalWindowChangeActivityForControlTable"})
    @TmsLink("949")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Delete Active Fraud Control not added to Business Unit")
    public void testDeleteActiveFraudControlNotAddedToBusinessUnit() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeleteControlButton(FRAUD_CONTROL.getControlName())
                .clickDeleteButton();

        Allure.step("Check if just deleted Fraud Control still presented in the table");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL.getControlName())).not().isAttached();
    }

    @Test
    @TmsLink("904")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Inactive Fraud Control")
    public void testAddInactiveFraudControl() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_INACTIVE.getControlName())
                .fillFraudControlCodeField(FRAUD_CONTROL_INACTIVE.getControlCode())
                .fillFraudControlConfigField(FRAUD_CONTROL_INACTIVE.getControlConfig())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL_INACTIVE.getControlDisplayName())
                .checkInactiveRadiobutton()
                .clickSetupButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify that all the data are presented in the row");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL_INACTIVE.getControlName()))
                .hasText(FRAUD_CONTROL_INACTIVE.toString());
    }

    @Test(dependsOnMethods = "testAddInactiveFraudControl")
    @TmsLink("955")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Activate Fraud Control not added to Business Unit"
            + "Deactivate Fraud Control not added to Business Unit")
    public void testChangeControlActivityForFraudControlNotAddedToBusinessUnit() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickActivateControlButton(FRAUD_CONTROL_INACTIVE.getControlName())
                .clickActivateButton();

        // TODO refactor this
        Locator row = superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL_INACTIVE.getControlName());
        Locator cell = superFraudControlPage.getTableControls().getCell(row, "Status");

        Allure.step("Verify that Fraud Control state now is Active");
        assertThat(cell).hasText("Active");

        superFraudControlPage
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL_INACTIVE.getControlName())
                .clickDeactivateButton();

        Allure.step("Verify that Fraud Control state now is Inactive");
        assertThat(cell).hasText("Inactive");
    }

    @Test
    @TmsLink("910")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Add Fraud Control to Business Unit (No Fraud Control)"
            + "Add Fraud Control to Business Unit (Business unit has Fraud Control)")
    public void testAddFraudControlToBusinessUnit() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify that Fraud Control one are presented in Business Unit table");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL_ADD_ONE.getControlName()))
                .hasText(FRAUD_CONTROL_ADD_ONE.toString());

        Allure.step("Verify that Fraud Control two are presented in Business Unit table");
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlName()))
                .hasText(FRAUD_CONTROL_ADD_TWO.toString());

        superFraudControlPage.getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("967")
    @Epic("System/Fraud control")
    @Feature("Control table")
    @Description("Activate Fraud Control added to Business Unit and Deactivate Fraud Control added to Business Unit")
    public void testChangeControlActivityForFraudControlAddedToBusinessUnit() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton();

        Locator controlStatusCell = superFraudControlPage
                .getTableControls().getCell(FRAUD_CONTROL_ADD_ONE.getControlName(), "Status");

        // TODO refactor this
        Locator businessControlRow = superFraudControlPage
                .getTableBusinessUnitControls().getRowByDataKey("0");
        Locator businessControlStatusCell = superFraudControlPage
                .getTableBusinessUnitControls().getCell(businessControlRow, "Status");

        Allure.step("Verify Fraud Control's current status in Business Unit Control Table");
        assertThat(businessControlStatusCell).hasText("Active");

        Allure.step("Verify Fraud Control's current status in Control Table");
        assertThat(controlStatusCell).hasText("Active");

        superFraudControlPage
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickDeactivateButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify that Fraud Control status has been changed to 'Inactive' in Control Table");
        assertThat(controlStatusCell).hasText("Inactive");

        Allure.step("Verify that Fraud Control status hasn't been changed in Business Unit Control Table");
        assertThat(businessControlStatusCell).hasText("Active");

        superFraudControlPage
                .getTableControls().clickActivateControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickActivateButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify that Fraud Control status is Active in Control table again");
        assertThat(controlStatusCell).hasText("Active");
        assertThat(businessControlStatusCell).hasText("Active");

        superFraudControlPage.getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("1009")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Activate Business unit control" + "Deactivate Business unit control")
    public void testChangeBusinessUnitFraudControlActivity() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify Fraud Control's current status in Business Unit Control Table");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getCell(0, "Status"))
                .hasText("Active");

        Allure.step("Verify Fraud Control's current status in Control Table");
        assertThat(superFraudControlPage.getTableControls().getCell(FRAUD_CONTROL_ADD_ONE.getControlName(), "Status"))
                .hasText("Active");

        superFraudControlPage
                .getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("0")
                .clickDeactivateButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify that Fraud Control state hasn't been changed in Control Table");
        assertThat(superFraudControlPage.getTableControls().getCell(FRAUD_CONTROL_ADD_ONE.getControlName(), "Status"))
                .hasText("Active");

        Allure.step("Verify that Fraud Control state is Inactive now in Business Unit Control Table");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getCell(0, "Status"))
                .hasText("Inactive");

        superFraudControlPage
                .getTableBusinessUnitControls().clickActivateBusinessUnitControlButton("0")
                .clickActivateButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify that Fraud Control state is Active in Business Unit Control Table again");
        assertThat(superFraudControlPage.getTableControls().getCell(FRAUD_CONTROL_ADD_ONE.getControlName(), "Status"))
                .hasText("Active");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getCell(0, "Status"))
                .hasText("Active");

        superFraudControlPage.getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();
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
        // TODO hide this from test method
        Assert.assertTrue(((String) addControlDialog.getControlNameLabel()
                        .evaluate("el => getComputedStyle(el, '::after').content")).contains("*"),
                "The '*' symbol is not displayed in the 'Control name' label");

        Allure.step("Verify that the 'Create' button is disabled if the 'Control name field is empty");
        assertThat(addControlDialog.getSetupButton()).isDisabled();
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
                .fillFraudControlCodeField("Neutrino")
                .clickSetupButton()
                .getAlert().clickCloseButton()
                .clickAddFraudControl()
                .fillFraudControlNameField(FRAUD_CONTROL_NAME)
                .fillFraudControlCodeField("Neutrino")
                .clickSetupButton();

        Allure.step("Verify that the error message ‘ERROR Entity with name … already exists.’ is displayed.");
        assertThat(fraudControlPage.getAlert().getMessage())
                .hasText("ERROREntity with name {%s} already exists.".formatted(FRAUD_CONTROL_NAME));
    }

    @Test
    @TmsLink("950")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Delete Active Fraud Control added to Business Unit")
    public void testDeleteActiveFraudControlAddedToBusinessUnit() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("1")
                .clickDeleteButton();

        Allure.step("Check if just deleted Fraud Control still presented in both tables");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlDisplayName()))
                .not().isAttached();
        assertThat(superFraudControlPage.getTableControls().getRow(FRAUD_CONTROL_ADD_TWO.getControlName()))
                .isAttached();

        superFraudControlPage.getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("960")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Move Business unit control up" + "Move Business unit control down")
    public void testChangeFraudControlPriority() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableBusinessUnitControls().clickMoveBusinessUnitControlUpButton("1")
                .getAlert().clickCloseButton();

        Allure.step("Check that the second Fraud Control is '0' priority now");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getRowByDataKey("0"))
                .containsText(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());

        superFraudControlPage
                .getTableBusinessUnitControls().clickMoveBusinessUnitControlDownButton("0")
                .getAlert().clickCloseButton();

        Allure.step("Check that the second Fraud Control is '1' priority again");
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getRowByDataKey("0"))
                .containsText(FRAUD_CONTROL_ADD_ONE.getControlDisplayName());
        assertThat(superFraudControlPage.getTableBusinessUnitControls().getRowByDataKey("1"))
                .containsText(FRAUD_CONTROL_ADD_TWO.getControlDisplayName());

        superFraudControlPage.getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("927")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Remove inactive Fraud control added to Business unit")
    public void testDeleteInactiveFraudControlAddedToBusinessUnit() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ACTIVE_TO_INACTIVE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("0")
                .clickDeactivateButton()
                .getAlert().clickCloseButton()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();

        Allure.step("Verify the success message ‘SUCCESSBusiness unit control was deleted successfully'"
                + " is displayed");
        assertThat(superFraudControlPage.getAlert().getMessage())
                .hasText("SUCCESSBusiness unit control was deleted successfully");

        superFraudControlPage
                .getAlert().waitUntilSuccessAlertIsGone();

        List<String> actualFraudControlBusinessUnitList = superFraudControlPage
                .getTableBusinessUnitControls().getColumnValues("Display name");

        Allure.step("Verify that the business unit control table doesn't include the deleted control");
        Assert.assertFalse(actualFraudControlBusinessUnitList.contains(
                FRAUD_CONTROL_ACTIVE_TO_INACTIVE.getControlDisplayName()));
    }

    @Test(dependsOnMethods = {"testDeleteActiveFraudControlAddedToBusinessUnit",
            "testChangeBusinessUnitFraudControlActivity"})
    @TmsLink("986")
    @Epic("System/Fraud control")
    @Feature("Add/Edit/Delete Fraud Control")
    @Description("Edit Fraud Control")
    public void testEditFraudControl() {
        SuperFraudControlPage superFraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickEditControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
        //        .fillFraudControlCodeField(FRAUD_CONTROL_ADD_TWO.getControlCode())
                .fillFraudControlConfigField(FRAUD_CONTROL_ADD_TWO.getControlConfig())
                .fillFraudControlDisplayNameField(FRAUD_CONTROL_ADD_TWO.getControlDisplayName())
                .checkInactiveRadiobutton()
                .clickSaveChangesButton();

        Allure.step("Verify that 'Control was update successfully' alert is appeared");
        assertThat(superFraudControlPage.getAlert().getSuccessMessage())
                .hasText("SUCCESSControl was updated successfully");

        Locator row = superFraudControlPage
                .getAlert().clickCloseButton()
                .getTableControls().getRow(FRAUD_CONTROL_ADD_ONE.getControlName());

        Allure.step("Verify that all the data are changed in the row" + FRAUD_CONTROL_ADD_ONE.getControlName());

        assertThat(row).containsText(FRAUD_CONTROL_ADD_TWO.getControlCode());
    //    assertThat(row).not().containsText(FRAUD_CONTROL_ADD_ONE.getControlCode());

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
    @Feature("Table sort")
    @Description("Verify that 'Integrated third party controls' table can be sorted")
    public void testControlTableEntriesSorting() {
        SuperFraudControlPage fraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab();

        List<String> defaultColumnValues = fraudControlPage
                .getTableControls().getColumnValuesFromAllPages("Name");

        Allure.step("Entries are by default sorted ascending by Name");
        Assert.assertEquals(defaultColumnValues, defaultColumnValues.stream().sorted().toList());

        List<String> columns = List.of(/*"Type", */"Display name", "Name", "Code", "Config", "Status");
        //TODO sort by "Type" broken --- atm not sorting - [Fraud Screen, BIN Check, Fraud Screen] on first click

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

    @Test(expectedExceptions = AssertionError.class)
    @TmsLink("1013")
    @Epic("System/Fraud control")
    @Feature("Table sort")
    @Description("Verify that 'Connected business unit controls' table can be sorted")
    public void testBusinessUnitControlTableEntriesSorting() {
        SuperFraudControlPage fraudControlPage = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_SORT)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_THREE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("1")
                .clickDeactivateButton()
                .getAlert().clickCloseButton();

        List<String> defaultColumnValues = fraudControlPage
                .getTableBusinessUnitControls().getColumnValuesFromAllPages("Priority");

        Allure.step("Entries are by default sorted ascending by Priority");
        Assert.assertEquals(defaultColumnValues, defaultColumnValues.stream().sorted().toList());

        List<String> columns = List.of(/*"Type", */"Display name", "Code", "Config", "Priority"/*, "Status"*/);
        //TODO BUG sort for 'Type' column works only once
        //TODO BUG wrong(reverse) sort for 'Status' column

        columns.forEach(columnName -> {
            List<String> columnValues = fraudControlPage
                    .getTableBusinessUnitControls().clickColumnHeader(columnName)
                    .getTableBusinessUnitControls().getColumnValuesFromAllPages(columnName);

            Allure.step("Verify that entries are now sorted ascending by %s".formatted(columnName));
            Assert.assertEquals(columnValues, columnValues.stream().sorted().toList());

            columnValues = fraudControlPage
                    .getTableBusinessUnitControls().clickColumnHeader(columnName)
                    .getTableBusinessUnitControls().getColumnValuesFromAllPages(columnName);

            Allure.step("Verify that entries are now sorted descending by %s".formatted(columnName));
            Assert.assertEquals(columnValues, columnValues.stream().sorted(Comparator.reverseOrder()).toList());
        });
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

    @Test
    @TmsLink("1018")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("First 'Move business unit control up' and last 'Move business unit control down' are disabled")
    public void testChangeBusinessControlPriorityRestrictions() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_ONE.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton()
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton();

        Allure.step("Check that the first item move up icon is disabled");
        assertThat(page.getTableBusinessUnitControls().getMoveBusinessUnitControlUpButton("0")).isDisabled();

        Allure.step("Check that the last item move down icon is disabled");
        assertThat(page.getTableBusinessUnitControls().getMoveBusinessUnitControlDownButton("1")).isDisabled();

        page.getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton()
                .getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();
    }

    @Test
    @TmsLink("1064")
    @Epic("System/Fraud control")
    @Feature("Business Unit Control table")
    @Description("Priority icons for only one Business unit control")
    public void testPriorityIconsDisableForOnlyOneBusinessUnitControl() {
        SuperFraudControlPage page = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_NAME)
                .getTableControls().clickConnectControlButton(FRAUD_CONTROL_ADD_TWO.getControlName())
                .clickConnectButton()
                .getAlert().clickCloseButton();

        Allure.step("Verify Up button is disabled on Business Unit Control table because of one control");
        assertThat(page.getTableBusinessUnitControls().getMoveBusinessUnitControlUpButton("0")).isDisabled();

        Allure.step("Verify Down button is disabled on Business Unit Control table because of one control");
        assertThat(page.getTableBusinessUnitControls().getMoveBusinessUnitControlDownButton("0")).isDisabled();

        page.getTableBusinessUnitControls().clickDeleteBusinessUnitControlButton("0")
                .clickDeleteButton();
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
                    .fillFraudControlCodeField("Neutrino")
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
        AddControlDialog addControlDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .clickAddFraudControl()
                .fillFraudControlNameField("a".repeat(3));

        Allure.step("Verify that the 'Control Name' field is has attribute aria-invalid set");
        assertThat(addControlDialog.getControlNameInput()).hasAttribute("aria-invalid", "true");

        Allure.step("Verify that the Setup button is disabled if 'Control name' contains 3 characters");
        assertThat(addControlDialog.getSetupButton()).isDisabled();

        addControlDialog
                .fillFraudControlNameField("a".repeat(4));

        Allure.step("Verify that the Setup button is enabled if 'Control name' contains 4 characters");
        assertThat(addControlDialog.getSetupButton()).isEnabled();

        addControlDialog
                .fillFraudControlNameField("a".repeat(100));

        Allure.step("Verify that the Setup button is enabled if 'Control name' contains 100 characters");
        assertThat(addControlDialog.getSetupButton()).isEnabled();

        addControlDialog
                .fillFraudControlNameField("a".repeat(101));

        Allure.step("Verify that a 'Control name' field has limit of 100 characters");
        Assert.assertEquals(addControlDialog.getControlNameInput().inputValue().length(), 100);
    }

    @Test(dependsOnMethods = {"testAddActiveFraudControl", "testAddInactiveFraudControl"})
    @TmsLink("1143")
    @Epic("System/Fraud control")
    @Feature("Change control activity dialog Control table")
    @Description("Warning message text")
    public void testVerifyWarningModalWindowChangeActivityForControlTable() {
        DeactivateControlDialog deactivateControlDialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getTableControls().clickDeactivateControlButton(FRAUD_CONTROL.getControlName());

        Allure.step("Verify Deactivate control modal window text");
        assertThat(deactivateControlDialog.getModalWindowHeaderTitle()).hasText("Change control activity");

        Allure.step("Verify Deactivate control modal window main body text");
        assertThat(deactivateControlDialog.getModalWindowsMainTextBody())
                .hasText("Are you sure you want to deactivate control %s?".formatted(FRAUD_CONTROL.getControlDisplayName()));

        deactivateControlDialog
                .clickCancelButton()
                .getTableControls().clickActivateControlButton(FRAUD_CONTROL_INACTIVE.getControlName());

        Allure.step("Verify Activate control modal window text");
        assertThat(deactivateControlDialog.getModalWindowHeaderTitle()).hasText("Change control activity");

        Allure.step("Verify Activate control modal window main body text");
        assertThat(deactivateControlDialog.getModalWindowsMainTextBody())
                .hasText("Are you sure you want to activate control %s?".formatted(FRAUD_CONTROL_INACTIVE.getControlDisplayName()));
    }

    @Test(dependsOnMethods = {"testBusinessUnitControlTableEntriesSorting"})
    @TmsLink("1157")
    @Epic("System/Fraud control")
    @Feature("'Change business unit control activity' dialog text content")
    @Description("Verify text content for Header and Main body on 'Change business unit control activity' dialog")
    public void testVerifyWarningModalWindowChangeActivityForBusinessUnitControlTable() {
        DeactivateBusinessUnitControlDialog dialog = new SuperDashboardPage(getPage())
                .getHeader().clickSystemAdministrationLink()
                .getSystemMenu().clickFraudControlTab()
                .getSelectCompany().selectCompany(COMPANY_NAME)
                .getSelectBusinessUnit().selectBusinessUnit(BUSINESS_UNIT_SORT)
                .getTableBusinessUnitControls().clickDeactivateBusinessUnitControlButton("0");

        Allure.step("Verify Deactivate Business unit control modal window text");
        assertThat(dialog.getDialogHeader()).hasText("Change business unit control activity");

        Allure.step("Verify Deactivate Business unit control modal window main body text");
        assertThat(dialog.getModalWindowsMainTextBody())
                .hasText("Are you sure you want to deactivate business unit control "
                        + FRAUD_CONTROL_ADD_ONE.getControlDisplayName() + " with priority " + "0?");

        ActivateBusinessUnitControlDialog activateDialog = dialog.clickCloseIcon()
                .getTableBusinessUnitControls().clickActivateBusinessUnitControlButton("1");

        Allure.step("Verify Activate control modal window text");
        assertThat(activateDialog.getDialogHeader()).hasText("Change business unit control activity");

        Allure.step("Verify Activate control modal window main body text");
        assertThat(activateDialog.getModalWindowsMainTextBody())
                .hasText("Are you sure you want to activate business unit control "
                        + FRAUD_CONTROL_ADD_TWO.getControlDisplayName() + " with priority " + "1?");
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
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_FRAUD_SCREEN.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_THREE.getControlName());
        TestUtils.deleteFraudControl(getApiRequestContext(), FRAUD_CONTROL_ACTIVE_TO_INACTIVE.getControlName());

        TestUtils.deleteCompany(getApiRequestContext(), COMPANY_NAME);
        super.afterClass();
    }
}
