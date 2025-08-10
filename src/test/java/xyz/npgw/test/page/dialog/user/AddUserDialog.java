package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePage;

import static io.qameta.allure.model.Parameter.Mode.MASKED;

public abstract class AddUserDialog<
        ReturnPageT extends BasePage,
        CurrentDialogT extends AddUserDialog<ReturnPageT, CurrentDialogT>>
        extends UserDialog<ReturnPageT, CurrentDialogT> {

    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddUserDialog(Page page) {
        super(page);
    }

    @Step("Enter user email")
    public CurrentDialogT fillEmailField(String email) {
        getByPlaceholder("Enter user email").fill(email);

        return getCurrentDialog();
    }

    @Step("Enter user password")
    public CurrentDialogT fillPasswordField(@Param(name = "Password", mode = MASKED) String password) {
        getByPlaceholder("Enter user password").fill(password);

        return getCurrentDialog();
    }

    @Step("Click 'Create' button")
    public ReturnPageT clickCreateButton() {
        createButton.click();

        return getReturnPage();
    }

    @Step("Click on the 'Create' button and trigger an error")
    public CurrentDialogT clickCreateButtonAndTriggerError() {
        createButton.click();

        return getCurrentDialog();
    }
}
