package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

public class BusinessUnitsTableComponent extends BaseTableComponent<CompaniesAndBusinessUnitsPage> {
    public BusinessUnitsTableComponent(Page page) {
        super(page);
    }

    @Override
    protected CompaniesAndBusinessUnitsPage getCurrentPage() {
        return new CompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Edit Business Unit' button")
    public EditBusinessUnitDialog clickEditBusinessUnitButton(String name) {
        Locator editButton = getRow(name).getByTestId("EditBusinessUnitButton");
        editButton.waitFor();
        editButton.click();

        return new EditBusinessUnitDialog(getPage());
    }

}
