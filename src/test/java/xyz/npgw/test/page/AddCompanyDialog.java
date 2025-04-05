package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.BasePage;
import xyz.npgw.test.page.systemadministration.CompaniesAndBusinessUnitsPage;

import java.util.List;

public class AddCompanyDialog extends BasePage {

    @Getter
    private final Locator addCompanyDialogHeader = locator("section header");
    private final Locator companyNameField = placeholder("Enter company name");
    private final Locator companyTypeField = placeholder("Enter type");
    @Getter
    private final Locator createButton = button("Create");
    private final Locator errorMessage = locator("[role='alert']");
    private final Locator allFieldPlaceholders = locator("[data-slot='input']:not([placeholder='Search...'])");
    private final Locator closeButton = textExact("Close");

    public AddCompanyDialog(Page page) {
        super(page);
    }

    @Step("Fill company name field with the name: {companyName}")
    public AddCompanyDialog fillCompanyNameField(String companyName) {
        companyNameField.fill(companyName);

        return this;
    }

    @Step("Fill company type field with the type: {companyType}")
    public AddCompanyDialog fillCompanyTypeField(String companyType) {
        companyTypeField.fill(companyType);

        return this;
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddCompanyDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }

    public Locator getErrorMessage() {
        errorMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return errorMessage;
    }

    public List<String> getAllFieldPlaceholders() {
        allFieldPlaceholders.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        return allFieldPlaceholders.all().stream().map(l -> l.getAttribute("placeholder")).toList();
    }

    @Step("Click 'Close' button")
    public CompaniesAndBusinessUnitsPage clickCloseButton() {
        closeButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
