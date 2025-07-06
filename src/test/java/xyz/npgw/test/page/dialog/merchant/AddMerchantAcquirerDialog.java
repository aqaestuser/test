package xyz.npgw.test.page.dialog.merchant;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import xyz.npgw.test.page.common.trait.SelectAcquirerTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class AddMerchantAcquirerDialog extends BaseDialog<GatewayPage, AddMerchantAcquirerDialog>
        implements SelectAcquirerTrait<AddMerchantAcquirerDialog> {

    private final Locator inactiveStatusRadio = locator("(//input[@value='INACTIVE'])[1]");
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");


    public AddMerchantAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getReturnPage() {

        return new GatewayPage(getPage());
    }

    public AddMerchantAcquirerDialog selectInactiveStatus() {
        inactiveStatusRadio.click();

        return this;
    }

    public GatewayPage clickCreateButton() {
        createButton.click();

        return new GatewayPage(getPage());
    }
}
