package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.acquirer.ActivateAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.DeactivateAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.EditAcquirerDialog;
import xyz.npgw.test.page.system.AcquirersPage;

@Getter
public class AcquirersTableComponent extends BaseTableComponent<AcquirersPage> {

    private final Locator tableContent = getByLabelExact("merchants table").locator("tbody");

    public AcquirersTableComponent(Page page, AcquirersPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected AcquirersPage getCurrentPage() {
        return new AcquirersPage(getPage());
    }

    public Locator getEditAcquirerButton(String acquirerName) {
        return getRow(acquirerName).getByTestId("EditAcquirerButton");
    }

    public Locator getAcquirerActivityIcon(String acquirerName) {
        return getRow(acquirerName).getByTestId("ChangeAcquirerActivityButton").locator("svg");
    }

    @Step("Click 'Edit' button to edit acquirer")
    public EditAcquirerDialog clickEditAcquirerButton(String acquirerName) {
        getEditAcquirerButton(acquirerName).click();

        return new EditAcquirerDialog(getPage());
    }

    @Step("Click 'Activate acquirer' button")
    public ActivateAcquirerDialog clickActivateButton(String acquirerName) {
        getRow(acquirerName).locator("//*[@data-icon='check']/..").click();

        return new ActivateAcquirerDialog(getPage());
    }

    @Step("Click 'Deactivate acquirer' button")
    public DeactivateAcquirerDialog clickDeactivateButton(String acquirerName) {
        getRow(acquirerName).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateAcquirerDialog(getPage());
    }
}
