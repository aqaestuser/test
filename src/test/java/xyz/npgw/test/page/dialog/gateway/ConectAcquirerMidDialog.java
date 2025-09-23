package xyz.npgw.test.page.dialog.gateway;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.component.AlertTrait;
import xyz.npgw.test.page.component.select.SelectAcquirerMidTrait;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperGatewayPage;

@Getter
public class ConectAcquirerMidDialog extends BaseDialog<SuperGatewayPage, ConectAcquirerMidDialog>
        implements SelectAcquirerMidTrait<ConectAcquirerMidDialog>, AlertTrait<ConectAcquirerMidDialog> {

    private final Locator acquirerNameField = getByPlaceholder("Enter acquirer name");
    private final Locator connectButton = getByRole(AriaRole.BUTTON, "Connect");

    public ConectAcquirerMidDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperGatewayPage getReturnPage() {
        return new SuperGatewayPage(getPage());
    }

    @Step("Click 'Create' button")
    public SuperGatewayPage clickConnectButton() {
        connectButton.click();

        return getReturnPage();
    }

    @Step("Check 'Active' status radiobutton")
    public ConectAcquirerMidDialog checkActiveRadiobutton() {
        getByRole(AriaRole.RADIO, "Active").setChecked(true);

        return this;
    }

    @Step("Check 'Inactive' status radiobutton")
    public ConectAcquirerMidDialog checkInactiveRadiobutton() {
        getByRole(AriaRole.RADIO, "Inactive").setChecked(true);

        return this;
    }
}
