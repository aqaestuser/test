package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.FraudControlPage;

public class ConnectFraudControlToBusinessUnitDialog extends BaseDialog<FraudControlPage,
        ConnectFraudControlToBusinessUnitDialog> {

    public ConnectFraudControlToBusinessUnitDialog(Page page) {
        super(page);
    }

    @Override
    protected FraudControlPage getReturnPage() {
        return new FraudControlPage(getPage());
    }

    @Step("Click 'Connect' button")
    public FraudControlPage clickConnectButton() {
        getByRole(AriaRole.BUTTON, "Connect").click();

        return getReturnPage();
    }

    @Step("Click 'Cancel' button")
    public FraudControlPage clickCancelButton() {
        getByRole(AriaRole.BUTTON, "Cancel").click();

        return getReturnPage();
    }
}
