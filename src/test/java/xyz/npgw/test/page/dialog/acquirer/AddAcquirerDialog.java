package xyz.npgw.test.page.dialog.acquirer;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.system.AcquirersPage;

@Getter
public class AddAcquirerDialog extends AcquirerDialog<AddAcquirerDialog> {

    private final Locator createButton = buttonByName("Create");

    public AddAcquirerDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button")
    public AcquirersPage clickCreateButton() {
        getPage().waitForCondition(createButton::isEnabled);
        createButton.click();

        return new AcquirersPage(getPage());
    }
}
