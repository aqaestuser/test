package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.BaseDialog;
import xyz.npgw.test.page.system.TeamPage;

public class ChangeUserActivityDialog extends BaseDialog<TeamPage, ChangeUserActivityDialog> {

    public ChangeUserActivityDialog(Page page) {
        super(page);
    }

    @Override
    protected TeamPage getReturnPage() {
        return new TeamPage(getPage());
    }

    @Step("Click 'Deactivate' button")
    public TeamPage clickDeactivateButton() {
        getByRole(AriaRole.BUTTON, "Deactivate").click();

        return new TeamPage(getPage());
    }

    @Step("Click 'Activate' button")
    public TeamPage clickActivateButton() {
        getByRole(AriaRole.BUTTON, "Activate").click();

        return new TeamPage(getPage());
    }
}
