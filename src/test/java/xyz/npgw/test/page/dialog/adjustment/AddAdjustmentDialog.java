package xyz.npgw.test.page.dialog.adjustment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.AddAdjustmentTableTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TransactionManagementPage;

@Getter
public class AddAdjustmentDialog extends BaseDialog<TransactionManagementPage, AddAdjustmentDialog> implements
        AddAdjustmentTableTrait {

    private final Locator npgwReferenceInput = locator("input[aria-label='NPGW reference']");
    private final Locator npgwReferenceFieldLabel = locator("label[data-slot='label']").first();
    private final Locator buReferenceFieldLabel = locator("label[data-slot='label']").last();
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddAdjustmentDialog(Page page) {
        super(page);
    }

    @Override
    protected TransactionManagementPage getReturnPage() {
        return new TransactionManagementPage(getPage());
    }

    @Step("Fill NPGW reference input field")
    public AddAdjustmentDialog fillNpgwReferenceInput(String text) {
        npgwReferenceInput.fill(text);

        return this;
    }

    public TransactionManagementPage clickCreateButton() {
        createButton.click();

        return new TransactionManagementPage(getPage());
    }
}
