package xyz.npgw.test.page.dialog.adjustment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.component.table.AddAdjustmentTableTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperTransactionManagementPage;

@Getter
public class AddAdjustmentDialog extends BaseDialog<SuperTransactionManagementPage, AddAdjustmentDialog> implements
        AddAdjustmentTableTrait {

    private final Locator npgwReferenceInput = getByLabelExact("NPGW reference").locator("../input");
    private final Locator npgwReferenceFieldLabel = locator("label[data-slot='label']").first();
    private final Locator buReferenceFieldLabel = locator("label[data-slot='label']").last();
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddAdjustmentDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTransactionManagementPage getReturnPage() {
        return new SuperTransactionManagementPage(getPage());
    }

    @Step("Fill NPGW reference input field")
    public AddAdjustmentDialog fillNpgwReferenceInput(String text) {
        npgwReferenceInput.fill(text);

        return this;
    }

    public SuperTransactionManagementPage clickCreateButton() {
        createButton.click();

        return new SuperTransactionManagementPage(getPage());
    }
}
