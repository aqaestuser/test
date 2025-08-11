package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectBusinessUnitTrait;
import xyz.npgw.test.page.component.select.SelectCompanyTrait;
import xyz.npgw.test.page.component.select.SelectCurrencyTrait;
import xyz.npgw.test.page.component.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.component.table.BusinessUnitAcquirersTableTrait;
import xyz.npgw.test.page.dialog.gateway.AddBusinessUnitAcquirerDialog;

@Getter
public class SuperGatewayPage extends HeaderPage<SuperGatewayPage>
        implements SuperHeaderMenuTrait<SuperGatewayPage>,
                   SuperSystemMenuTrait,
                   SelectCompanyTrait<SuperGatewayPage>,
                   SelectBusinessUnitTrait<SuperGatewayPage>,
                   SelectCurrencyTrait<SuperGatewayPage>,
                   BusinessUnitAcquirersTableTrait {

    public SuperGatewayPage(Page page) {
        super(page);
    }

    @Step("Click 'Add merchant acquirer button'")
    public AddBusinessUnitAcquirerDialog clickAddBusinessUnitAcquirerButton() {
        getByTestId("AddMerchantAcquirerButton").click();

        return new AddBusinessUnitAcquirerDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public SuperGatewayPage clickResetFilterButton() {
        getByTestId("ResetButtonTeamPage").click();

        return this;
    }

    @Step("Click 'Refresh data' button")
    public SuperGatewayPage clickRefreshDataButton() {
        getByTestId("ApplyFilterButtonsMerchantsPage").click();

        return this;
    }
}
