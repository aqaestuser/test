package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.BusinessUnitControlsTableTrait;
import xyz.npgw.test.page.common.trait.ControlsTableTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.dialog.control.AddControlDialog;

public class FraudControlPage extends BaseSystemPage<FraudControlPage> implements
        SelectCompanyTrait<FraudControlPage>,
        SelectBusinessUnitTrait<FraudControlPage>,
        ControlsTableTrait,
        BusinessUnitControlsTableTrait,
        AlertTrait<FraudControlPage> {

    public FraudControlPage(Page page) {
        super(page);
    }

    @Step("Click 'Add control' button")
    public AddControlDialog clickAddFraudControl() {
        getByTestId("AddControlButton").click();

        return new AddControlDialog(getPage());
    }
}
