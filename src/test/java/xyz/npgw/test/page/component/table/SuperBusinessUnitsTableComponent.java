package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.merchant.DeleteBusinessUnitDialog;
import xyz.npgw.test.page.dialog.merchant.EditBusinessUnitDialog;
import xyz.npgw.test.page.system.SuperCompaniesAndBusinessUnitsPage;

public class SuperBusinessUnitsTableComponent extends BaseTableComponent<SuperCompaniesAndBusinessUnitsPage> {
    public SuperBusinessUnitsTableComponent(Page page, SuperCompaniesAndBusinessUnitsPage currentPage) {
        super(page, currentPage);
    }

    public Locator getRowIcon(String businessUnitName) {
        return getRow(businessUnitName).locator("button:enabled");
    }

    public String getIconName(Locator icon) {
        return icon.locator("svg").getAttribute("data-icon");
    }

    @Override
    protected SuperCompaniesAndBusinessUnitsPage getCurrentPage() {
        return new SuperCompaniesAndBusinessUnitsPage(getPage());
    }

    @Step("Click 'Edit business unit' button")
    public EditBusinessUnitDialog clickEditBusinessUnitButton(String businessUnitName) {
        getRow(businessUnitName).getByTestId("EditBusinessUnitButton").click();

        return new EditBusinessUnitDialog(getPage());
    }

    @Step("Click 'Delete business unit' button")
    public DeleteBusinessUnitDialog clickDeleteBusinessUnitButton(String businessUnitName) {
        getRow(businessUnitName).getByTestId("DeleteBusinessUnitButton").click();

        return new DeleteBusinessUnitDialog(getPage());
    }
}
