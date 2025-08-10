package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public class ConnectControlToBusinessUnitDialog
        extends BaseDialog<SuperFraudControlPage, ConnectControlToBusinessUnitDialog> {

    public ConnectControlToBusinessUnitDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperFraudControlPage getReturnPage() {
        return new SuperFraudControlPage(getPage());
    }

    @Step("Click 'Connect' button")
    public SuperFraudControlPage clickConnectButton() {
        getByRole(AriaRole.BUTTON, "Connect").click();

        return getReturnPage();
    }

    @Step("Click 'Cancel' button")
    public SuperFraudControlPage clickCancelButton() {
        getByRole(AriaRole.BUTTON, "Cancel").click();

        return getReturnPage();
    }
}
