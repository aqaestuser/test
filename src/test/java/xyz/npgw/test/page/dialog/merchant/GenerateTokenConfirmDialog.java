package xyz.npgw.test.page.dialog.merchant;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;

@Getter
public final class GenerateTokenConfirmDialog extends BaseDialog<AdminBusinessUnitsPage, GenerateTokenConfirmDialog> {

    private final Locator content = getDialog().locator("p.text-danger");

    public GenerateTokenConfirmDialog(Page page) {
        super(page);
    }

    @Override
    protected AdminBusinessUnitsPage getReturnPage() {
        return new AdminBusinessUnitsPage(getPage());
    }

    @Step("Click 'Generate' button")
    public SecretTokenDialog clickGenerateButton() {
        getByRole(AriaRole.BUTTON, "Generate").click();

        return new SecretTokenDialog(getPage());
    }
}
