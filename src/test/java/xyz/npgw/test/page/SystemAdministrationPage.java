package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.BasePageWithHeader;
import xyz.npgw.test.page.component.ContentBlock;

public class SystemAdministrationPage extends BasePageWithHeader {

    private final ContentBlock table;
    private final Locator teamButton = tab("Team");
    private final Locator companiesAndBusinessUnitsTabButton = tab("Companies and business units");
    private final Locator acquirersTabButton = tab("Acquirers");
    private final Locator gatewayTabButton = tab("Gateway");
    private final Locator fraudControlTabButton = tab("Fraud control");
    private final Locator transactionManagementTabButton = tab("Transaction management");

    public SystemAdministrationPage(Page page) {
        super(page);
        table = new ContentBlock(page);
    }

    @Step("Click Acquirers Tab Button")
    public SaAcquirersTab clickAcquirersTabButton() {
        acquirersTabButton.click();

        return new SaAcquirersTab(getPage());
    }

    @Step("Click 'Companies and business units' button")
    public SaCompaniesAndBusinessUnitsTab clickCompaniesAndBusinessUnitsTabButton() {
        companiesAndBusinessUnitsTabButton.click();

        return new SaCompaniesAndBusinessUnitsTab(getPage());
    }
}
