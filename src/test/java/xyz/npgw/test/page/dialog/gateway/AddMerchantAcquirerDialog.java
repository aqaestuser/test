package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectAcquirerTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.GatewayPage;

@Getter
public class AddMerchantAcquirerDialog extends BaseDialog<GatewayPage, AddMerchantAcquirerDialog>
        implements SelectAcquirerTrait<AddMerchantAcquirerDialog>, AlertTrait<AddMerchantAcquirerDialog> {

    private final Locator acquirerNameField = getByPlaceholder("Enter acquirer name");
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddMerchantAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getReturnPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click 'Create' button")
    public GatewayPage clickCreateButton() {
        createButton.click();

        return new GatewayPage(getPage());
    }

    @Step("Check 'Inactive' status radiobutton")
    public AddMerchantAcquirerDialog checkInactiveRadiobutton() {
        getByRole(AriaRole.RADIO, "Inactive").setChecked(true);

        return this;
    }
}
