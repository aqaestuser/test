package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import xyz.npgw.test.page.dialog.merchant.GenerateTokenConfirmDialog;
import xyz.npgw.test.page.system.AdminBusinessUnitsPage;

public class AdminBusinessUnitsTableComponent extends BaseTableComponent<AdminBusinessUnitsPage> {

    public AdminBusinessUnitsTableComponent(Page page, AdminBusinessUnitsPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected AdminBusinessUnitsPage getCurrentPage() {
        return new AdminBusinessUnitsPage(getPage());
    }

    @Step("Click 'Copy Business unit ID to clipboard' button")
    public AdminBusinessUnitsPage clickCopyBusinessUnitIdToClipboardButton(String businessUnitName) {
        getRow(businessUnitName).getByTestId("CopyBusinessUnitIDToClipboardButton").click();

        return getCurrentPage();
    }

    @Step("Click 'Generate secret token' button")
    public GenerateTokenConfirmDialog clickGenerateSecretTokenButton(String businessUnitName) {
        getRow(businessUnitName).getByTestId("ViewSecretTokenButton").click();

        return new GenerateTokenConfirmDialog(getPage());
    }
}
