package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperFraudControlPage;

public class DeactivateBusinessUnitControlDialog
        extends BaseDialog<SuperFraudControlPage, DeactivateBusinessUnitControlDialog> {

    public DeactivateBusinessUnitControlDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperFraudControlPage getReturnPage() {
        return new SuperFraudControlPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public SuperFraudControlPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }
}
