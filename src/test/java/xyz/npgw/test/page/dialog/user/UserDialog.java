package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TeamPage;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public abstract class UserDialog<CurrentDialogT extends UserDialog<CurrentDialogT>>
        extends BaseDialog<TeamPage, CurrentDialogT> {

    @Getter
    private final Locator companyNameField = getByTextExact("Company name");

    public UserDialog(Page page) {
        super(page);
    }

    @Override
    protected TeamPage getReturnPage() {
        return new TeamPage(getPage());
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

        return (CurrentDialogT) this;
    }

    @Step("Check 'Company admin' user role radiobutton")
    public CurrentDialogT checkCompanyAdminRadiobutton() {
        getByRole(AriaRole.RADIO, "Company admin").check();

        return (CurrentDialogT) this;
    }

    @Step("Check 'Company analyst' user role radiobutton")
    public CurrentDialogT checkCompanyAnalystRadiobutton() {
        getByRole(AriaRole.RADIO, "Company analyst").check();

        return (CurrentDialogT) this;
    }

    @Step("Set checked 'Allowed business units' checkboxes by business units names")
    public CurrentDialogT setAllowedBusinessUnits(String[] businessUnits) {
        Arrays.stream(businessUnits).forEach(this::setAllowedBusinessUnit);

        return (CurrentDialogT) this;
    }

    @Step("Set checked all '{businessUnit}' checkboxes")
    public CurrentDialogT setAllowedBusinessUnit(String businessUnit) {
        getByTextExact("Allowed business units").waitFor();
        getByRole(AriaRole.CHECKBOX, businessUnit).all()
                .forEach(item -> {
                    item.waitFor();
                    item.setChecked(true);
                });

        return (CurrentDialogT) this;
    }

    @Step("Unset checked 'Allowed business units' checkboxes by business units names")
    public CurrentDialogT unsetAllowedBusinessUnits(String[] businessUnits) {
        for (String businessUnit : businessUnits) {
            Locator businessUnitLocator = getByRole(AriaRole.CHECKBOX, businessUnit);

            businessUnitLocator.last().waitFor();
            businessUnitLocator.all().forEach(item -> item.setChecked(false));
        }

        return (CurrentDialogT) this;
    }
}
