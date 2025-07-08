package xyz.npgw.test.page.dialog.adjustment;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TransactionManagementPage;

public class AddAdjustmentDialog extends BaseDialog<TransactionManagementPage, AddAdjustmentDialog> {

    private final Locator transactionRow = locator("[aria-label='transactions table'] tr[data-first]");
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");
    private final Locator closeButton = getByRole(AriaRole.BUTTON)
            .filter(new Locator.FilterOptions().setHasText("Close"));

    public AddAdjustmentDialog(Page page) {
        super(page);
    }

    @Override
    protected TransactionManagementPage getReturnPage() {
        return new TransactionManagementPage(getPage());
    }

    public AddAdjustmentDialog clickOnTheTransaction() {
        transactionRow.click();

        return this;
    }

    public TransactionManagementPage clickOnCreateButton() {
        createButton.click();

        return new TransactionManagementPage(getPage());
    }

    public TransactionManagementPage clickOnCloseButton() {
        closeButton.click();
        getDialog().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return new TransactionManagementPage(getPage());
    }
}
