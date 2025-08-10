package xyz.npgw.test.page.dialog.user;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePage;

public abstract class EditUserDialog<
        ReturnPageT extends BasePage,
        CurrentDialogT extends EditUserDialog<ReturnPageT, CurrentDialogT>>
        extends UserDialog<ReturnPageT, CurrentDialogT> {

    public EditUserDialog(Page page) {
        super(page);
    }

    @Step("Click 'Save changes' button")
    public ReturnPageT clickSaveChangesButton() {
        getByRole(AriaRole.BUTTON, "Save changes").click();

        return getReturnPage();
    }
}
