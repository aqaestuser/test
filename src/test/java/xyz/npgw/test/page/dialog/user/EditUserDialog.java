package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.TeamPage;

public class EditUserDialog extends UserDialog<EditUserDialog> {

    public EditUserDialog(Page page) {
        super(page);
    }

    @Step("Click 'Save changes' button")
    public TeamPage clickSaveChangesButton() {
        getByRole(AriaRole.BUTTON, "Save changes").click();

        return new TeamPage(getPage());
    }
}
