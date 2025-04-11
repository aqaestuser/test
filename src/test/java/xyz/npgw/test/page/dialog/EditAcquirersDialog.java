package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class EditAcquirersDialog extends BaseDialog {

    private final Locator selectCountryLabel = dialog().getByLabel("Select country");

    public EditAcquirersDialog(Page page) {
        super(page);
    }

    @Step("Click 'Select Country' dropDownList")
    public EditAcquirersDialog clickSelectCountry(String name) {
        selectCountryLabel.locator("..")
                .locator("svg[role=presentation]")
                .last()
                .click();
        return this;
    }

    @Step("Click 'Close' icon to close form")
    public void clickCloseIcon() {
        getCloseIcon().click();
    }

    @Step("Click 'Close' button to close form")
    public void clickCloseButton() {
        getCloseButton().click();
    }

}
