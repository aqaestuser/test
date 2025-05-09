package xyz.npgw.test.page.dialog.company;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

@Getter
public final class AddCompanyDialog extends CompanyDialog<AddCompanyDialog> {

    private final Locator createButton = getByRole(AriaRole.BUTTON, "Create");

    public AddCompanyDialog(Page page) {
        super(page);
    }

    @Step("Click on the 'Create' button and trigger an error")
    public AddCompanyDialog clickCreateButtonAndTriggerError() {
        createButton.click();

        return this;
    }

    @Step("Click on the 'Create' button")
    public CompaniesAndBusinessUnitsPage clickCreateButton() {
        createButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
