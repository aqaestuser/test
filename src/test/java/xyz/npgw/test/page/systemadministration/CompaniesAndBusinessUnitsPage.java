package xyz.npgw.test.page.systemadministration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.AddCompanyDialog;
import xyz.npgw.test.page.base.SystemAdministrationWithTableBasePage;

public class CompaniesAndBusinessUnitsPage extends SystemAdministrationWithTableBasePage {

    private final Locator addCompanyButton = locator("button[data-testid='AddCompanyButton']");
    @Getter
    private final Locator addCompanyDialog = dialog();

    public CompaniesAndBusinessUnitsPage(Page page) {
        super(page);
    }

    @Step("Click 'Add company' button")
    public AddCompanyDialog clickAddCompanyButton() {
        addCompanyButton.click();

        return new AddCompanyDialog(getPage());
    }

}
