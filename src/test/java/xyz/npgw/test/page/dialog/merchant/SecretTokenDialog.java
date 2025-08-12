package xyz.npgw.test.page.dialog.merchant;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;

@Getter
public final class SecretTokenDialog extends BaseDialog<AdminBusinessUnitsPage, SecretTokenDialog> {

    private final Locator secretTokenInputField = getDialog().locator("//input[@type='text']");

    public SecretTokenDialog(Page page) {
        super(page);
    }

    @Override
    protected AdminBusinessUnitsPage getReturnPage() {
        return new AdminBusinessUnitsPage(getPage());
    }

    @Step("Click 'Copy secret token' icon in input")
    public SecretTokenDialog clickCopySecretToken() {
        getByLabelExact("Secret token").locator("..").getByRole(AriaRole.BUTTON).click();

        return this;
    }
}
