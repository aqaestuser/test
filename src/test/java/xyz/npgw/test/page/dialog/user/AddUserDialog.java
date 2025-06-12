package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.system.TeamPage;

import static io.qameta.allure.model.Parameter.Mode.MASKED;

public class AddUserDialog extends UserDialog<AddUserDialog> implements AlertTrait<AddUserDialog> {

    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddUserDialog(Page page) {
        super(page);
    }

    @Step("Enter user email")
    public AddUserDialog fillEmailField(String email) {
        getByPlaceholder("Enter user email").fill(email);

        return this;
    }

    @Step("Enter user password")
    public AddUserDialog fillPasswordField(@Param(name = "Password", mode = MASKED) String password) {
        getByPlaceholder("Enter user password").fill(password);

        return this;
    }

    @Step("Click 'Create' button")
    public TeamPage clickCreateButton() {
        createButton.click();

        return new TeamPage(getPage());
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddUserDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }
}
