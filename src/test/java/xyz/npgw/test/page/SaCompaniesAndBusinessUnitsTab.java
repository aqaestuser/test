package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePageWithHeader;

public class SaCompaniesAndBusinessUnitsTab extends BasePageWithHeader {

    private final Locator addCompanyButton = locator("svg[data-icon='circle-plus']").first();

    public SaCompaniesAndBusinessUnitsTab(Page page) {
        super(page);
    }

    @Step("Click 'Add company' button")
    public AddCompanyDialog clickAddCompanyButton() {
        addCompanyButton.click();

        return new AddCompanyDialog(getPage());
    }
}
