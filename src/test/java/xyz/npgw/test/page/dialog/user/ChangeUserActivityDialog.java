package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
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
        buttonByName("Deactivate").click();

        return new TeamPage(getPage());
    }

    @Step("Click 'Activate' button")
    public TeamPage clickActivateButton() {
        buttonByName("Activate").click();

        return new TeamPage(getPage());
    }
}
