package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.SuperTeamPage;

@SuppressWarnings("unchecked")
public abstract class UserDialog<CurrentDialogT extends UserDialog<CurrentDialogT>>
        extends BaseDialog<SuperTeamPage, CurrentDialogT> {

    @Getter
    private final Locator companyNameField = getByTextExact("Company name");

    public UserDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTeamPage getReturnPage() {
        return new SuperTeamPage(getPage());
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

    @Step("Check 'System admin' user role radiobutton")
    public CurrentDialogT checkSystemAdminRadiobutton() {
        getByRole(AriaRole.RADIO, "System admin").check();
//        assertThat(getByRole(AriaRole.RADIO, "System admin")).isChecked();

        return (CurrentDialogT) this;
    }

    @Step("Check 'Company admin' user role radiobutton")
    public CurrentDialogT checkCompanyAdminRadiobutton() {
        getByRole(AriaRole.RADIO, "Company admin").check();
//        assertThat(getByRole(AriaRole.RADIO, "Company admin")).isChecked();

        return (CurrentDialogT) this;
    }

    @Step("Check 'Company analyst' user role radiobutton")
    public CurrentDialogT checkCompanyAnalystRadiobutton() {
        getByRole(AriaRole.RADIO, "Company analyst").check();
//        assertThat(getByRole(AriaRole.RADIO, "Company analyst")).isChecked();

        return (CurrentDialogT) this;
    }

    @Step("Check '{businessUnitName}' checkbox")
    public CurrentDialogT checkAllowedBusinessUnitCheckbox(String businessUnitName) {
//        getByTextExact("Allowed business units").waitFor();
        getByRole(AriaRole.CHECKBOX, businessUnitName).setChecked(true);

        return (CurrentDialogT) this;
    }

    @Step("Uncheck '{businessUnitName}' checkbox")
    public CurrentDialogT uncheckAllowedBusinessUnitCheckbox(String businessUnitName) {
        getByRole(AriaRole.CHECKBOX, businessUnitName).setChecked(false);

        return (CurrentDialogT) this;
    }
}
