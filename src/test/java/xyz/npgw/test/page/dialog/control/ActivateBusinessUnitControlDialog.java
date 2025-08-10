package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public class ActivateBusinessUnitControlDialog
        extends BaseDialog<SuperFraudControlPage, ActivateBusinessUnitControlDialog> {

    public ActivateBusinessUnitControlDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperFraudControlPage getReturnPage() {
        return new SuperFraudControlPage(getPage());
    }

    @Step("Click 'Activate' button")
    public SuperFraudControlPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return getReturnPage();
    }
}
