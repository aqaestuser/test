package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.acquirer.DeleteBusinessUnitAcquirerDialog;
import xyz.npgw.test.page.dialog.gateway.ChangeMerchantAcquirerActivityDialog;
import xyz.npgw.test.page.system.GatewayPage;

public class GatewayTableComponent extends BaseTableComponent<GatewayPage> {

    public GatewayTableComponent(Page page) {
        super(page);
    }

    private final Locator changeMerchantAcquirerActivityButton = getByTestId("ChangeMerchantAcquirerActivityButton");

    @Getter
    private final Locator acquirerStatus = locator("td > div.rounded-full");

    @Override
    protected GatewayPage getCurrentPage() {
        return new GatewayPage(getPage());
    }

    @Step("Click on Change merchant acquirer activity button ")
    public ChangeMerchantAcquirerActivityDialog clickChangeMerchantAcquirerActivityButton() {
        changeMerchantAcquirerActivityButton.click();

        return new ChangeMerchantAcquirerActivityDialog(getPage());
    }

    @Step("Click 'Delete business unit acquirer' for merchant acquirer with name: {acquirerDisplayName}")
    public DeleteBusinessUnitAcquirerDialog clickDeleteBusinessUnitAcquirer(String acquirerDisplayName) {
        getRowByText(acquirerDisplayName).locator(getByTestId("DeleteMerchantAcquirerButton")).click();

        return new DeleteBusinessUnitAcquirerDialog(getPage());
    }
}
