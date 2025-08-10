package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.SuperAcquirersPage;

public class EditAcquirerMidDialog extends AcquirerDialog<EditAcquirerMidDialog> {

    private final Locator saveChangesButton = getByRole(AriaRole.BUTTON, "Save changes");

    public EditAcquirerMidDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Save changes' button")
    public SuperAcquirersPage clickSaveChangesButton() {
        saveChangesButton.click();

        return getReturnPage();
    }
}
