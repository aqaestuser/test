package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.common.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.common.trait.BusinessUnitControlsTableTrait;
import xyz.npgw.test.page.common.trait.ControlsTableTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.dialog.control.AddControlDialog;

public class FraudControlPage extends HeaderPage<FraudControlPage>
        implements SuperHeaderMenuTrait<FraudControlPage>,
                   SuperSystemMenuTrait,
                   SelectCompanyTrait<FraudControlPage>,
                   SelectBusinessUnitTrait<FraudControlPage>,
                   ControlsTableTrait,
                   BusinessUnitControlsTableTrait {

    public FraudControlPage(Page page) {
        super(page);
    }

    @Step("Click 'Add control' button")
    public AddControlDialog clickAddFraudControl() {
        getByTestId("AddControlButton").click();

        return new AddControlDialog(getPage());
    }

    @Step("Click 'Reset filter' button")
    public FraudControlPage clickResetFilterButton() {
        getByTestId("ResetButtonFraudControlPage").click();

        return this;
    }
}
