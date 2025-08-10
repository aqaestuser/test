package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectBusinessUnitTrait;
import xyz.npgw.test.page.component.select.SelectCompanyTrait;
import xyz.npgw.test.page.component.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.component.table.BusinessUnitControlsTableTrait;
import xyz.npgw.test.page.component.table.ControlsTableTrait;
import xyz.npgw.test.page.dialog.control.AddControlDialog;

public class SuperFraudControlPage extends HeaderPage<SuperFraudControlPage>
        implements SuperHeaderMenuTrait<SuperFraudControlPage>,
                   SuperSystemMenuTrait,
                   SelectCompanyTrait<SuperFraudControlPage>,
                   SelectBusinessUnitTrait<SuperFraudControlPage>,
                   ControlsTableTrait,
                   BusinessUnitControlsTableTrait {

    public SuperFraudControlPage(Page page) {
        super(page);
    }

    @Step("Click 'Add control' button")
    public AddControlDialog clickAddFraudControl() {
        getByTestId("AddControlButton").click();

        return new AddControlDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public SuperFraudControlPage clickResetFilterButton() {
        getByTestId("ResetButtonFraudControlPage").click();

        return this;
    }
}
