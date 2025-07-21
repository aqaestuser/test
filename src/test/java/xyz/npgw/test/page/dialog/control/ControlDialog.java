package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.FraudControlPage;

@SuppressWarnings("unchecked")
public abstract class ControlDialog<CurrentDialogT extends ControlDialog<CurrentDialogT>>
        extends BaseDialog<FraudControlPage, CurrentDialogT> {

    public ControlDialog(Page page) {
        super(page);
    }

    @Override
    protected FraudControlPage getReturnPage() {

        return new FraudControlPage(getPage());
    }

    @Step("Fill in fraud control name: {controlName}")
    public CurrentDialogT fillFraudControlNameField(String controlName) {
        getByPlaceholder("Enter control name").fill(controlName);

        return (CurrentDialogT) this;
    }

    @Step("Fill in fraud control code: {controlCode}")
    public CurrentDialogT fillFraudControlCodeField(String controlCode) {
        getByPlaceholder("Enter control code").fill(controlCode);

        return (CurrentDialogT) this;
    }

    @Step("Fill in fraud control display name: {displayName}")
    public CurrentDialogT fillFraudControlDisplayNameField(String displayName) {
        getByPlaceholder("Enter display name").fill(displayName);

        return (CurrentDialogT) this;
    }

    @Step("Check 'Active' status radiobutton")
    public CurrentDialogT checkActiveRadiobutton() {
        getByRoleExact(AriaRole.RADIO, "Active").check();

        return (CurrentDialogT) this;
    }

    @Step("Check 'Inactive' status radiobutton")
    public CurrentDialogT checkInactiveRadiobutton() {
        getByRole(AriaRole.RADIO, "Inactive").check();

        return (CurrentDialogT) this;
    }

    @Step("Fill in fraud control config: {controlConfig}")
    public CurrentDialogT fillFraudControlConfigField(String controlConfig) {
        getByPlaceholder("Enter control config").fill(controlConfig);

        return (CurrentDialogT) this;
    }
}
