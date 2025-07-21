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
public class AddBusinessUnitAcquirerDialog extends BaseDialog<GatewayPage, AddBusinessUnitAcquirerDialog>
        implements SelectAcquirerTrait<AddBusinessUnitAcquirerDialog>, AlertTrait<AddBusinessUnitAcquirerDialog> {

    private final Locator acquirerNameField = getByPlaceholder("Enter acquirer name");
    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddBusinessUnitAcquirerDialog(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getReturnPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click 'Create' button")
    public GatewayPage clickCreateButton() {
        createButton.click();

        return getReturnPage();
    }

    @Step("Check 'Active' status radiobutton")
    public AddBusinessUnitAcquirerDialog checkActiveRadiobutton() {
        getByRole(AriaRole.RADIO, "Active").setChecked(true);

        return this;
    }

    @Step("Check 'Inactive' status radiobutton")
    public AddBusinessUnitAcquirerDialog checkInactiveRadiobutton() {
        getByRole(AriaRole.RADIO, "Inactive").setChecked(true);

        return this;
    }
}
