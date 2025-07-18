package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.control.ActivateControlActivityDialog;
import xyz.npgw.test.page.dialog.control.ConnectFraudControlToBusinessUnitDialog;
import xyz.npgw.test.page.dialog.control.DeactivateControlActivityDialog;
import xyz.npgw.test.page.dialog.control.DeleteControlDialog;
import xyz.npgw.test.page.dialog.control.EditFraudControlDialog;
import xyz.npgw.test.page.system.FraudControlPage;

public class FraudControlsTableComponent extends BaseTableComponent<FraudControlPage> {

    public FraudControlsTableComponent(Page page) {
//        super(page, page.locator("div.flex.flex-col.gap-2.flex-1").first());
        super(page, page.getByLabel("transactions table")
                .or(page.getByRole(AriaRole.GROUP)
                        .filter(new Locator.FilterOptions().setHasText("Rows Per Page"))).first());
    }

    @Override
    protected FraudControlPage getCurrentPage() {
        return new FraudControlPage(getPage());
    }

    @Step("Click 'Edit control' icon")
    public EditFraudControlDialog clickEditControlIcon(String controlName) {
        getRow(controlName).getByTestId("EditControlButton").click();

        return new EditFraudControlDialog(getPage());
    }

    @Step("Click 'Deactivate control' icon")
    public DeactivateControlActivityDialog clickDeactivateControlIcon(String controlName) {
        getRow(controlName).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateControlActivityDialog(getPage());
    }

    @Step("Click 'Activate control' icon")
    public ActivateControlActivityDialog clickActivateControlIcon(String controlName) {
        getRow(controlName).locator("//*[@data-icon='check']/..").click();

        return new ActivateControlActivityDialog(getPage());
    }

    @Step("Click 'Delete control' icon")
    public DeleteControlDialog clickDeleteControlIcon(String controlName) {
        getRow(controlName).getByTestId("DeleteControlButton").click();

        return new DeleteControlDialog(getPage());
    }

    @Step("Click 'Connect control' icon")
    public ConnectFraudControlToBusinessUnitDialog clickConnectControlIcon(String controlName) {
        getRow(controlName).getByTestId("ConnectControlButton").click();

        return new ConnectFraudControlToBusinessUnitDialog(getPage());
    }
}
