package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.acquirer.ActivateAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.DeactivateAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.EditAcquirerMidDialog;
import xyz.npgw.test.page.system.SuperAcquirersPage;

@Getter
public class AcquirersTableComponent extends BaseTableComponent<SuperAcquirersPage> {

    private final Locator tableContent = getByLabelExact("merchants table").locator("tbody");

    public AcquirersTableComponent(Page page, SuperAcquirersPage currentPage) {
        super(page, currentPage);
    }

    @Override
    protected SuperAcquirersPage getCurrentPage() {
        return new SuperAcquirersPage(getPage());
    }

    public Locator getEditAcquirerMidButton(String acquirerName) {
        return getRow(acquirerName).getByTestId("EditAcquirerButton");
    }

    public Locator getAcquirerActivityIcon(String acquirerName) {
        return getRow(acquirerName).getByTestId("ChangeAcquirerActivityButton").locator("svg");
    }

    @Step("Click 'Edit acquirer MID' button to edit acquirer")
    public EditAcquirerMidDialog clickEditAcquirerMidButton(String acquirerName) {
        getEditAcquirerMidButton(acquirerName).click();

        return new EditAcquirerMidDialog(getPage());
    }

    @Step("Click 'Activate acquirer MID' button")
    public ActivateAcquirerDialog clickActivateAcquirerMidButton(String acquirerName) {
        getRow(acquirerName).locator("//*[@data-icon='check']/..").click();

        return new ActivateAcquirerDialog(getPage());
    }

    @Step("Click 'Deactivate acquirer MID' button")
    public DeactivateAcquirerDialog clickDeactivateAcquirerMidButton(String acquirerName) {
        getRow(acquirerName).locator("//*[@data-icon='ban']/..").click();

        return new DeactivateAcquirerDialog(getPage());
    }
}
