package xyz.npgw.test.page.dialog.control;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.component.AlertTrait;
import xyz.npgw.test.page.system.SuperFraudControlPage;

@Getter
public class AddControlDialog extends ControlDialog<AddControlDialog> implements
        AlertTrait<AddControlDialog> {

    private final Locator setupButton = getByRole(AriaRole.BUTTON, "Setup");

    public AddControlDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddControlDialog clickCreateButtonAndTriggerError() {
        setupButton.click();

        return this;
    }

    @Step("Click on the 'Create' button")
    public SuperFraudControlPage clickSetupButton() {
        setupButton.click();

        return getReturnPage();
    }
}
