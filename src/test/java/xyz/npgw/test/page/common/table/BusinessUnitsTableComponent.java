package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.merchant.DeleteBusinessUnitDialog;
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
    public EditBusinessUnitDialog clickEditBusinessUnitButton(String businessUnitName) {
        getRow(businessUnitName).getByTestId("EditBusinessUnitButton").click();

        return new EditBusinessUnitDialog(getPage());
    }

    @Step("Click 'Delete Business Unit' button")
    public DeleteBusinessUnitDialog clickDeleteBusinessUnitButton(String businessUnitName) {
        getRow(businessUnitName).getByTestId("DeleteBusinessUnitButton").click();

        return new DeleteBusinessUnitDialog(getPage());
    }
}
