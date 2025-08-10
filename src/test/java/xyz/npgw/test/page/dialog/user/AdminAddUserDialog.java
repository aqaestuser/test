package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.AdminTeamPage;

public class AdminAddUserDialog extends AddUserDialog<AdminTeamPage, AdminAddUserDialog> {

    public AdminAddUserDialog(Page page) {
        super(page);
    }

    @Override
    protected AdminTeamPage getReturnPage() {
        return new AdminTeamPage(getPage());
    }

    @Step("Check 'System admin' user role radiobutton")
    public AdminAddUserDialog checkSystemAdminRadiobutton() {
        getByRole(AriaRole.RADIO, "System admin").check();
//        assertThat(getByRole(AriaRole.RADIO, "System admin")).isChecked();

        return this;
    }
}
