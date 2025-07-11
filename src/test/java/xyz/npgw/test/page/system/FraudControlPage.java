package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.BusinessUnitControlsTrait;
import xyz.npgw.test.page.common.trait.FraudControlsTrait;
import xyz.npgw.test.page.common.trait.SelectBusinessUnitTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;
import xyz.npgw.test.page.dialog.control.AddFraudControlDialog;

public class FraudControlPage extends BaseSystemPage<FraudControlPage> implements
        SelectCompanyTrait<FraudControlPage>,
        SelectBusinessUnitTrait<FraudControlPage>,
        FraudControlsTrait,
        BusinessUnitControlsTrait,
        AlertTrait<FraudControlPage> {

    public FraudControlPage(Page page) {
        super(page);
    }

    @Step("Click 'Add control' button")
    public AddFraudControlDialog clickAddFraudControl() {
        getByTestId("AddControlButton").click();

        return new AddFraudControlDialog(getPage());
    }
}
