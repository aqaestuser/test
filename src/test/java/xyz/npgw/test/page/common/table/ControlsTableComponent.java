package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.control.ActivateControlDialog;
import xyz.npgw.test.page.dialog.control.ConnectControlToBusinessUnitDialog;
import xyz.npgw.test.page.dialog.control.DeactivateControlDialog;
import xyz.npgw.test.page.dialog.control.DeleteControlDialog;
import xyz.npgw.test.page.dialog.control.EditControlDialog;
import xyz.npgw.test.page.system.FraudControlPage;

public class ControlsTableComponent extends BaseTableComponent<FraudControlPage> {

    private final Locator tooltip = locator("//div[@data-slot='content']").last();

    public ControlsTableComponent(Page page) {
        super(page, page.getByText("Controls", new Page.GetByTextOptions().setExact(true)).locator("../.."));
    }

    @Override
    protected FraudControlPage getCurrentPage() {
        return new FraudControlPage(getPage());
    }

    @Step("Hover over Edit Control icon to get Tooltip")
    public Locator hoverOverEditIcon(String controlName) {
        getRow(controlName).hover();
        getRow(controlName).getByTestId("EditControlButton").hover();

        tooltip.waitFor();

        return tooltip;
    }

    @Step("Hover over Deactivate Control icon to get Tooltip")
    public Locator hoverOverDeactivateControlIcon(String controlName) {
        getRow(controlName).hover();
        getRow(controlName).locator("//*[@data-icon='ban']/..").hover();

        tooltip.waitFor();

        return tooltip;
    }

    @Step("Hover over Activate Control icon to get Tooltip")
    public Locator hoverOverActivateControlIcon(String controlName) {
        getRow(controlName).hover();
        getRow(controlName).locator("//*[@data-icon='check']/..").hover();

        tooltip.waitFor();

        return tooltip;
    }

    @Step("Hover over Activate Control icon to get Tooltip")
    public Locator hoverOverDeleteIcon(String controlName) {
        getRow(controlName).hover();
        getRow(controlName).getByTestId("DeleteControlButton").hover();

        tooltip.waitFor();

        return tooltip;
    }

    @Step("Hover over Activate Control icon to get Tooltip")
    public Locator hoverOverConnectControlIcon(String controlName) {
        getRow(controlName).hover();
        getRow(controlName).getByTestId("ConnectControlButton").hover();

        tooltip.waitFor();

        return tooltip;
    }

    @Step("Click 'Edit control' button")
    public EditControlDialog clickEditControlButton(String controlName) {
        getRow(controlName).getByTestId("EditControlButton").click();

        return new EditControlDialog(getPage());
    }

    @Step("Click 'Activate control' button")
    public ActivateControlDialog clickActivateControlButton(String controlName) {
        getRow(controlName).hover();
        getRow(controlName).locator("//*[@data-icon='check']/..").click();

        return new ActivateControlDialog(getPage());
    }

    @Step("Click 'Deactivate control' button")
    public DeactivateControlDialog clickDeactivateControlButton(String controlName) {
        getRow(controlName).hover();
        getRow(controlName).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateControlDialog(getPage());
    }

    @Step("Click 'Delete control' button")
    public DeleteControlDialog clickDeleteControlButton(String controlName) {
        getRow(controlName).getByTestId("DeleteControlButton").click();

        return new DeleteControlDialog(getPage());
    }

    @Step("Click 'Connect control' button")
    public ConnectControlToBusinessUnitDialog clickConnectControlButton(String controlName) {
        getRow(controlName).getByTestId("ConnectControlButton").click();

        return new ConnectControlToBusinessUnitDialog(getPage());
    }
}
