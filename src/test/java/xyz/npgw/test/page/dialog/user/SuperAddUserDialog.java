package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.SuperTeamPage;

public class SuperAddUserDialog extends AddUserDialog<SuperTeamPage, SuperAddUserDialog> {

    public SuperAddUserDialog(Page page) {
        super(page);
    }

    @Override
    protected SuperTeamPage getReturnPage() {
        return new SuperTeamPage(getPage());
    }

    @Step("Check 'System admin' user role radiobutton")
    public SuperAddUserDialog checkSystemAdminRadiobutton() {
        getByRole(AriaRole.RADIO, "System admin").check();
//        assertThat(getByRole(AriaRole.RADIO, "System admin")).isChecked();

        return this;
    }
}
