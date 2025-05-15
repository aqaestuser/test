package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.common.entity.UserRole;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TeamPage;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public abstract class UserDialog<CurrentDialogT extends UserDialog<CurrentDialogT>>
        extends BaseDialog<TeamPage, CurrentDialogT> {

    private final Locator activeRadioButton = getByRoleExact(AriaRole.RADIO, "Active");
    private final Locator inactiveRadioButton = getByRoleExact(AriaRole.RADIO, "Inactive");
    private final Locator allowedBusinessUnitsTitle = getByTextExact("Allowed business units");

    public UserDialog(Page page) {
        super(page);
    }

    @Override
    protected TeamPage getReturnPage() {
        return new TeamPage(getPage());
    }

    @Step("Set user status to {isActive}")
    public CurrentDialogT setStatusRadiobutton(boolean isActive) {
        if (isActive) {
            activeRadioButton.check();
        } else {
            inactiveRadioButton.check();
        }

        return (CurrentDialogT) this;
    }

    @Step("Check 'Active' status radiobutton")
    public CurrentDialogT checkActiveRadiobutton() {
        getPage().getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setExact(true).setName("Active")).check();

        return (CurrentDialogT) this;
    }

    @Step("Check 'Inactive' status radiobutton")
    public CurrentDialogT checkInactiveRadiobutton() {
        getPage().getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Inactive")).check();

        return (CurrentDialogT) this;
    }

    @Step("Set 'User role' radiobutton checked for '{userRole}'")
    public CurrentDialogT setUserRoleRadiobutton(UserRole userRole) {
        getByRoleExact(AriaRole.RADIO, userRole.getName()).check();

        return (CurrentDialogT) this;
    }

    @Step("Check 'System admin' user role radiobutton")
    public CurrentDialogT checkSystemAdminRadiobutton() {
        getPage().getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("System admin")).check();

        return (CurrentDialogT) this;
    }

    @Step("Check 'Company admin' user role radiobutton")
    public CurrentDialogT checkCompanyAdminRadiobutton() {
        getPage().getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Company admin")).check();

        return (CurrentDialogT) this;
    }

    @Step("Check 'Company analyst' user role radiobutton")
    public CurrentDialogT checkCompanyAnalystRadiobutton() {
        getPage().getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Company analyst")).check();

        return (CurrentDialogT) this;
    }

    @Step("Set checked 'Allowed business units' checkboxes by business units names")
    public CurrentDialogT setAllowedBusinessUnits(String[] businessUnits) {
        Arrays.stream(businessUnits).forEach(this::setAllowedBusinessUnit);

        return (CurrentDialogT) this;
    }

    @Step("Set checked all '{businessUnit}' checkboxes")
    public CurrentDialogT setAllowedBusinessUnit(String businessUnit) {
        allowedBusinessUnitsTitle.waitFor();
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
            Locator businessUnitLocator = getPage()
                    .getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(businessUnit));

            businessUnitLocator.last().waitFor();
            businessUnitLocator.all().forEach(item -> item.setChecked(false));
        }

        return (CurrentDialogT) this;
    }
}
