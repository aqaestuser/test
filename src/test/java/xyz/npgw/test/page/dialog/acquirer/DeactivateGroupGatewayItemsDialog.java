package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperAcquirersPage;

@Getter
public class DeactivateGroupGatewayItemsDialog extends BaseDialog<SuperAcquirersPage, ActivateGroupGatewayItemsDialog> {

    private final Locator confirmationQuestion = getDialog().locator("p");

    public DeactivateGroupGatewayItemsDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperAcquirersPage getReturnPage() {
        return new SuperAcquirersPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public SuperAcquirersPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return getReturnPage();
    }
}
