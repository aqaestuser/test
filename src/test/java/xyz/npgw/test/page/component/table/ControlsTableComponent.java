package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.control.ActivateControlDialog;
import xyz.npgw.test.page.dialog.control.ConnectControlToBusinessUnitDialog;
import xyz.npgw.test.page.dialog.control.DeactivateControlDialog;
import xyz.npgw.test.page.dialog.control.DeleteControlDialog;
import xyz.npgw.test.page.dialog.control.EditControlDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public class ControlsTableComponent extends BaseTableComponent<SuperFraudControlPage> {

    public ControlsTableComponent(Page page, SuperFraudControlPage currentPage) {
        super(page, currentPage,
                page.getByText("Integrated third party controls", new Page.GetByTextOptions().setExact(true))
                        .locator("../.."));
    }

    @Override
    protected SuperFraudControlPage getCurrentPage() {
        return new SuperFraudControlPage(getPage());
    }

    @Step("Hover over Edit Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverEditIcon(String controlName) {
        Locator row = getRow(controlName);
        row.hover();
        row.getByTestId("EditControlButton").hover();

        return getCurrentPage();
    }

    @Step("Hover over Deactivate Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverDeactivateControlIcon(String controlName) {
        Locator row = getRow(controlName);
        row.hover();
        row.locator("//*[@data-icon='ban']/..").hover();

        return getCurrentPage();
    }

    @Step("Hover over Activate Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverActivateControlIcon(String controlName) {
        Locator row = getRow(controlName);
        row.hover();
        row.locator("//*[@data-icon='check']/..").hover();

        return getCurrentPage();
    }

    @Step("Hover over Delete Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverDeleteIcon(String controlName) {
        Locator row = getRow(controlName);
        row.hover();
        row.getByTestId("DeleteControlButton").hover();

        return getCurrentPage();
    }

    @Step("Hover over Connect Control icon to get Tooltip")
    public SuperFraudControlPage hoverOverConnectControlIcon(String controlName) {
        Locator row = getRow(controlName);
        row.hover();
        row.getByTestId("ConnectControlButton").hover();

        return getCurrentPage();
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

    @Step("Get 'Connect control' button locator")
    public Locator getConnectControlButton(String controlName) {
        return getRow(controlName).getByTestId("ConnectControlButton");
    }
}
