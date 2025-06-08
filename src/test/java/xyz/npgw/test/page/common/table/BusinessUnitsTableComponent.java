package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.merchant.DeleteBusinessUnitDialog;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;
import xyz.npgw.test.page.system.CompaniesAndBusinessUnitsPage;

@Getter
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

    @Step("Click 'Delete Business Unit' button")
    public DeleteBusinessUnitDialog clickDeleteBusinessUnitButton(String name) {
        Locator deleteButton = getRow(name).getByTestId("DeleteBusinessUnitButton");
        deleteButton.waitFor();
        deleteButton.click();

        return new DeleteBusinessUnitDialog(getPage());
    }
}
