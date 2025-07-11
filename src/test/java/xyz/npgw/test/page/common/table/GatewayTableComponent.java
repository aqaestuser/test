package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.acquirer.DeleteBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.ChangeMerchantAcquirerActivityDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class GatewayTableComponent extends BaseTableComponent<GatewayPage> {

    public GatewayTableComponent(Page page) {
        super(page);
    }

    private final Locator firstRowChangeActivityButton = getByTestId("ChangeMerchantAcquirerActivityButton").nth(0);
    private final Locator acquirerColumnHeader = locator("//th[text()='Acquirer']");
    private final Locator currenciesColumnHeader = locator("//th[text()='Currencies']");
    private final Locator priorityColumnHeader = locator("//th[text()='Priority']");
    private final Locator statusColumnHeader = locator("//th[text()='Status']");

    @Override
    protected GatewayPage getCurrentPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click on Change merchant acquirer activity button ")
    public ChangeMerchantAcquirerActivityDialog clickFirstRowChangeActivityButton() {
        firstRowChangeActivityButton.click();

        return new ChangeMerchantAcquirerActivityDialog(getPage());
    }

    @Step("Click 'Delete business unit acquirer' for merchant acquirer with name: {acquirerDisplayName}")
    public DeleteBusinessUnitAcquirerDialog clickDeleteBusinessUnitAcquirer(String acquirerDisplayName) {
        getRowByText(acquirerDisplayName).locator(getByTestId("DeleteMerchantAcquirerButton")).click();

        return new DeleteBusinessUnitAcquirerDialog(getPage());
    }

    @Step("Click 'Status' column header")
    public GatewayPage clickStatusColumnHeader() {
        statusColumnHeader.click();

        return new GatewayPage(getPage());
    }

    @Step("Click 'Priority' column header")
    public GatewayPage clickPriorityColumnHeader() {
        priorityColumnHeader.click();

        return new GatewayPage(getPage());
    }

    @Step("Click 'Acquirer' column header")
    public GatewayPage clickAcquirerColumnHeader() {
        acquirerColumnHeader.click();

        return new GatewayPage(getPage());
    }

    @Step("Click 'Currencies' column header")
    public GatewayPage clickCurrenciesColumnHeader() {
        currenciesColumnHeader.click();

        return new GatewayPage(getPage());
    }
}
