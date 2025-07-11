package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.FraudControlPage;

public class DeactivateControlActivityDialog extends BaseDialog<FraudControlPage, DeactivateControlActivityDialog> {


    public DeactivateControlActivityDialog(Page page) {
        super(page);
    }

    @Override
    protected FraudControlPage getReturnPage() {
        return new FraudControlPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public FraudControlPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }
}
