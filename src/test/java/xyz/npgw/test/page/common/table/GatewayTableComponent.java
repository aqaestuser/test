package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.acquirer.DeleteBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.ChangeMerchantAcquirerActivityDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class GatewayTableComponent extends BaseTableComponent<GatewayPage> {

    private final Locator firstRowChangeActivityButton;
    private final Locator acquirerColumnHeader;
    private final Locator currenciesColumnHeader;
    private final Locator priorityColumnHeader;
    private final Locator statusColumnHeader;

    public GatewayTableComponent(Page page) {
        super(page);

        this.firstRowChangeActivityButton = getRoot().getByTestId("ChangeMerchantAcquirerActivityButton").nth(0);
        this.acquirerColumnHeader = getRoot().locator("//th[text()='Acquirer']");
        this.currenciesColumnHeader = getRoot().locator("//th[text()='Currencies']");
        this.priorityColumnHeader = getRoot().locator("//th[text()='Priority']");
        this.statusColumnHeader = getRoot().locator("//th[text()='Status']");
    }

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
        getRow(acquirerDisplayName).locator(getByTestId("DeleteMerchantAcquirerButton")).click();

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
