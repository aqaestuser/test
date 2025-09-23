package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectBusinessUnitTrait;
import xyz.npgw.test.page.component.select.SelectCompanyTrait;
import xyz.npgw.test.page.component.select.SelectCurrencyTrait;
import xyz.npgw.test.page.component.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.component.table.GatewayTableTrait;
import xyz.npgw.test.page.dialog.gateway.ConectAcquirerMidDialog;

@Getter
public class SuperGatewayPage extends HeaderPage<SuperGatewayPage>
        implements SuperHeaderMenuTrait<SuperGatewayPage>,
                   SuperSystemMenuTrait,
                   SelectCompanyTrait<SuperGatewayPage>,
                   SelectBusinessUnitTrait<SuperGatewayPage>,
                   SelectCurrencyTrait<SuperGatewayPage>,
                   GatewayTableTrait {

    private final Locator connectAcquirerMidButton = getByTestId("AddMerchantAcquirerButton");
    private final Locator resetFilterButton = getByTestId("ResetButtonTeamPage");
    private final Locator refreshDataButton = getByTestId("ApplyFilterButtonsMerchantsPage");

    public SuperGatewayPage(Page page) {
        super(page);
    }

    @Step("Hover over 'Connect acquirer MID' button")
    public SuperGatewayPage hoverOverConnectAcquirerMidButton() {
        connectAcquirerMidButton.hover();

        return this;
    }

    @Step("Click 'Connect acquirer MID' button")
    public ConectAcquirerMidDialog clickConnectAcquirerMidButton() {
        connectAcquirerMidButton.click();

        return new ConectAcquirerMidDialog(getPage());
    }

    @Step("Hover over 'Reset filter' button")
    public SuperGatewayPage hoverOverResetFilterButton() {
        resetFilterButton.hover();

        return this;
    }

    @Step("Click 'Reset filter' button")
    public SuperGatewayPage clickResetFilterButton() {
        resetFilterButton.click();

        return this;
    }

    @Step("Hover over 'Refresh data' button")
    public SuperGatewayPage hoverOverRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }

    @Step("Click 'Refresh data' button")
    public SuperGatewayPage clickRefreshDataButton() {
        refreshDataButton.click();

        return this;
    }
}
