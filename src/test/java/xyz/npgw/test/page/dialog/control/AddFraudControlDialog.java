package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.system.FraudControlPage;

public class AddFraudControlDialog extends FraudControlDialog<AddFraudControlDialog> implements
        AlertTrait<AddFraudControlDialog> {

    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddFraudControlDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddFraudControlDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }

    @Step("Click on the 'Create' button")
    public FraudControlPage clickCreateButton() {
        createButton.click();

        return getReturnPage();
    }
}
