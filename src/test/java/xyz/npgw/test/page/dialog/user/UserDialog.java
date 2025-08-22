package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.dialog.BaseDialog;

@Getter
public abstract class UserDialog<
        ReturnPageT extends BasePage,
        CurrentDialogT extends UserDialog<ReturnPageT, CurrentDialogT>>
        extends BaseDialog<ReturnPageT, CurrentDialogT> {

    private final Locator companyNameField = getByTextExact("Company name");

    public UserDialog(Page page) {
        super(page);
    }

    @Step("Check 'Active' status radiobutton")
    public CurrentDialogT checkActiveRadiobutton() {
        getByRoleExact(AriaRole.RADIO, "Active").check();

        return getCurrentDialog();
    }

    @Step("Check 'Inactive' status radiobutton")
    public CurrentDialogT checkInactiveRadiobutton() {
        getByRole(AriaRole.RADIO, "Inactive").check();

        return getCurrentDialog();
    }

    @Step("Check 'Company admin' user role radiobutton")
    public CurrentDialogT checkCompanyAdminRadiobutton() {
        getByRole(AriaRole.RADIO, "Company admin").check();

        return getCurrentDialog();
    }

    @Step("Check 'Company analyst' user role radiobutton")
    public CurrentDialogT checkCompanyAnalystRadiobutton() {
        getByRole(AriaRole.RADIO, "Company analyst").check();

        return getCurrentDialog();
    }

    @Step("Check '{businessUnitName}' checkbox")
    public CurrentDialogT checkAllowedBusinessUnitCheckbox(String businessUnitName) {
//        getByTextExact("Allowed business units").waitFor();
        getByRole(AriaRole.CHECKBOX, businessUnitName).setChecked(true);

        return getCurrentDialog();
    }

    @Step("Uncheck '{businessUnitName}' checkbox")
    public CurrentDialogT uncheckAllowedBusinessUnitCheckbox(String businessUnitName) {
        getByRole(AriaRole.CHECKBOX, businessUnitName).setChecked(false);

        return getCurrentDialog();
    }
}
