package xyz.npgw.test.page.dialog;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

import java.util.List;

public final class AddCompanyDialog extends CompanyDialog<AddCompanyDialog> {

    @Getter
    private final Locator addCompanyDialogHeader = locator("section header");
    @Getter
    private final Locator createButton = buttonByName("Create");
    private final Locator alertMessage = locator("[role='alert']");
    private final Locator allFieldPlaceholders = locator("[data-slot='input']:not([placeholder='Search...'])");

    public AddCompanyDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddCompanyDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }

    public Locator getAlertMessage() {
        alertMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return alertMessage;
    }

    public List<String> getAllFieldPlaceholders() {
        allFieldPlaceholders.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return allFieldPlaceholders.all().stream().map(l -> l.getAttribute("placeholder")).toList();
    }

    @Step("Click 'Close' button")
    public CompaniesAndBusinessUnitsPage clickCloseButton() {
        getCloseButton().click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click on the 'Create' button")
    public CompaniesAndBusinessUnitsPage clickCreateButton() {
        createButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
