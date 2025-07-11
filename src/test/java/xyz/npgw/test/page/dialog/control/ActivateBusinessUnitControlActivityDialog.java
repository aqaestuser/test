package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.FraudControlPage;

public class ActivateBusinessUnitControlActivityDialog
        extends BaseDialog<FraudControlPage, ActivateBusinessUnitControlActivityDialog> {

    public ActivateBusinessUnitControlActivityDialog(Page page) {
        super(page);
    }

    @Override
    protected FraudControlPage getReturnPage() {
        return new FraudControlPage(getPage());
    }

    @Step("Click 'Activate' button")
    public FraudControlPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return getReturnPage();
    }
}
