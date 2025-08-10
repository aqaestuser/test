package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.system.SuperAcquirersPage;

@Getter
public class SetupAcquirerMidDialog extends AcquirerDialog<SetupAcquirerMidDialog> {

    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public SetupAcquirerMidDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button")
    public SuperAcquirersPage clickCreateButton() {
        createButton.click();

        return getReturnPage();
    }

    @Step("Click on the 'Create' button and trigger an error")
    public SetupAcquirerMidDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }
}
