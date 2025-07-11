package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.Getter;
import xyz.npgw.test.page.common.SelectAcquirerComponent;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectAcquirerTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.GatewayPage;

@Getter
public class AddMerchantAcquirerDialog extends BaseDialog<GatewayPage, AddMerchantAcquirerDialog>
        implements SelectAcquirerTrait<AddMerchantAcquirerDialog>, AlertTrait<AddMerchantAcquirerDialog> {

    private final Locator acquirerNameField = getByPlaceholder("Enter acquirer name");
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");
    private final Locator inactiveStatusRadioButton = locator("(//input[@value='INACTIVE'])[1]");
    private final SelectAcquirerComponent<AddMerchantAcquirerDialog> selectAcquirerComponent;

    public AddMerchantAcquirerDialog(Page page) {
        super(page);
        this.selectAcquirerComponent = new SelectAcquirerComponent<>(page, this);
    }

    @Override
    protected GatewayPage getReturnPage() {
        return new GatewayPage(getPage());
    }

    public GatewayPage clickCreateButton() {
        createButton.click();
        getAlert().clickCloseButton();
        getDialog().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return new GatewayPage(getPage());
    }

    public AddMerchantAcquirerDialog selectInactiveStatus() {
        inactiveStatusRadioButton.click();

        return this;
    }
}
