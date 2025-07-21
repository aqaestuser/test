package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.system.AcquirersPage;

@Getter
public class AddAcquirerDialog extends AcquirerDialog<AddAcquirerDialog> {

    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddAcquirerDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button")
    public AcquirersPage clickCreateButton() {
        createButton.click();

        return getReturnPage();
    }
}
