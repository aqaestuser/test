package xyz.npgw.test.page.systemadministration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.SystemAdministrationBasePage;

public class TeamPage extends SystemAdministrationBasePage {

    private final Locator companiesAndBusinessUnitsTabButton = tab("Companies and business units");
    private final Locator acquirersTabButton = tab("Acquirers");

    public TeamPage(Page page) {
        super(page);
    }

    @Step("Click Acquirers Tab Button")
    public AcquirersPage clickAcquirersTabButton() {
        acquirersTabButton.click();

        return new AcquirersPage(getPage());
    }

    @Step("Click 'Companies and business units' button")
    public CompaniesAndBusinessUnitsPage clickCompaniesAndBusinessUnitsTabButton() {
        companiesAndBusinessUnitsTabButton.click();

        return new CompaniesAndBusinessUnitsPage(getPage());
    }
}
