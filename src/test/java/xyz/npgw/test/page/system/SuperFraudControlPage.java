package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
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

    private final Locator controlTypeSelector = getByLabelExact("Control type");
    @Getter
    private final Locator controlTypeValue = controlTypeSelector.locator("[data-slot='value']");
    private final Locator controlTypeDropdown = locator("div[data-slot='listbox']");
    @Getter
    private final Locator controlTypeOptions = controlTypeDropdown.getByRole(AriaRole.OPTION);

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

    @Step("Select Control type '{value}' in dropdown")
    public SuperFraudControlPage selectTypeValue(String value) {
        controlTypeOptions
                .getByText(value, new Locator.GetByTextOptions().setExact(true))
                .click();

        return this;
    }

    @Step("Open 'Control type' dropdown")
    public SuperFraudControlPage openControlTypeDropdown() {
        controlTypeValue.click();

        return this;
    }
}
