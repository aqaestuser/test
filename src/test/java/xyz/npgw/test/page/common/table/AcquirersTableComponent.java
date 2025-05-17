package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.acquirer.EditAcquirerDialog;
import xyz.npgw.test.page.system.AcquirersPage;

public class AcquirersTableComponent extends BaseTableComponent<AcquirersPage> {

    private final Locator editAcquirerButton = getByTestId("EditAcquirerButton");

    public AcquirersTableComponent(Page page) {
        super(page);
    }

    @Override
    protected AcquirersPage getCurrentPage() {

        return new AcquirersPage(getPage());
    }

    @Step("Click 'Edit' button to edit acquirer")
    public EditAcquirerDialog clickEditAcquirerButton() {
        editAcquirerButton.click();

        return new EditAcquirerDialog(getPage());
    }

    public Locator getEditAcquirerButton(Locator row) {

        return row.getByTestId("EditAcquirerButton");
    }

    public Locator getChangeAcquirerActivityButton(Locator row) {

        return row.getByTestId("ChangeBusinessUnitActivityButton");
    }
}
