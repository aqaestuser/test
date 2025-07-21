package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.system.FraudControlPage;

public class AddControlDialog extends ControlDialog<AddControlDialog> implements
        AlertTrait<AddControlDialog> {

    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddControlDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddControlDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }

    @Step("Click on the 'Create' button")
    public FraudControlPage clickCreateButton() {
        createButton.click();

        return getReturnPage();
    }
}
