package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.acquirer.ChangeAcquirerActivityDialog;
import xyz.npgw.test.page.dialog.acquirer.EditAcquirerDialog;
import xyz.npgw.test.page.system.AcquirersPage;

public class AcquirersTableComponent extends BaseTableComponent<AcquirersPage> {

    public AcquirersTableComponent(Page page) {
        super(page);
    }

    @Override
    protected AcquirersPage getCurrentPage() {
        return new AcquirersPage(getPage());
    }

    public Locator getEditAcquirerButton(Locator row) {
        return row.getByTestId("EditAcquirerButton");
    }

    public Locator getChangeActivityButton(Locator row) {
        return row.getByTestId("ChangeBusinessUnitActivityButton");
    }

    @Step("Click 'Edit' button to edit acquirer")
    public EditAcquirerDialog clickEditAcquirerButton(Locator row) {
        getEditAcquirerButton(row).click();

        return new EditAcquirerDialog(getPage());
    }

    @Step("Click Activate/Deactivate acquirer button")
    public ChangeAcquirerActivityDialog clickChangeActivityButton(Locator row) {
        getChangeActivityButton(row).click();

        return new ChangeAcquirerActivityDialog(getPage());
    }
}
