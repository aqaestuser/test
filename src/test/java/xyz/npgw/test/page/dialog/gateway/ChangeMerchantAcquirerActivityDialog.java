package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class ChangeMerchantAcquirerActivityDialog extends BaseDialog<GatewayPage, ChangeMerchantAcquirerActivityDialog>
        implements AlertTrait<ChangeMerchantAcquirerActivityDialog> {

    private final Locator submitButton = getByRole(AriaRole.BUTTON, "Activate")
            .or(getByRole(AriaRole.BUTTON, "Deactivate"));
    private final Locator cancelButton = getByRole(AriaRole.BUTTON, "Cancel");
    private final Locator closeButton = getByRole(AriaRole.BUTTON, "Close");

    public ChangeMerchantAcquirerActivityDialog(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getReturnPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click on submit deactivate button ")
    public GatewayPage clickSubmitButton() {
        submitButton.click();

        return new GatewayPage(getPage());
    }

    @Step("Click on 'Cancel' button ")
    public GatewayPage clickCancelButton() {
        cancelButton.click();

        return new GatewayPage(getPage());
    }

    @Step("Click on 'Close' button ")
    public GatewayPage clickCloseButton() {
        closeButton.click();

        return new GatewayPage(getPage());
    }
}
