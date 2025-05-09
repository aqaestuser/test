package xyz.npgw.test.page.dialog.company;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

public final class EditCompanyDialog extends CompanyDialog<EditCompanyDialog> {

    private final Locator saveChangesButton = getByRole(AriaRole.BUTTON, "Save changes");

    public EditCompanyDialog(Page page) {
        super(page);
    }

    @Step("Click 'Save changes' button")
    public CompaniesAndBusinessUnitsPage clickSaveChangesButton() {
        saveChangesButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
