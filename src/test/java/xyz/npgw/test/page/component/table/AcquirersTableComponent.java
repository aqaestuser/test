package xyz.npgw.test.page.component.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.dialog.acquirer.ActivateAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.ActivateGroupGatewayItemsDialog;
import xyz.npgw.test.page.dialog.acquirer.DeactivateAcquirerDialog;
import xyz.npgw.test.page.dialog.acquirer.DeactivateGroupGatewayItemsDialog;
import xyz.npgw.test.page.dialog.acquirer.EditAcquirerMidDialog;
import xyz.npgw.test.page.system.SuperAcquirersPage;

@Getter
public class AcquirersTableComponent extends BaseTableComponent<SuperAcquirersPage> {

    private final Locator tableContent = getByLabelExact("merchants table").locator("tbody");
    private final Locator deactivateGatewayConnectionsButton = getByTextExact("Deactivate gateway connections");
    private final Locator activateGatewayConnectionsButton = getByTextExact("Activate gateway connections");

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

    @Step("Click 'Bulk actions' button")
    public AcquirersTableComponent clickBulkActionsButton(String entityName) {
        Locator bulkButton = getRow(entityName).locator("svg[data-icon='wand-magic-sparkles']");
        bulkButton.hover();
        getPage().waitForTimeout(1500);

        bulkButton.click();
        Locator dropdown = getPage().locator("div[aria-label='Options']");
        dropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
//
//        activateGatewayConnectionsButton.waitFor();
//        deactivateGatewayConnectionsButton.waitFor();

        return this;
    }

    @Step("Select 'Activate gateway connections'")
    public ActivateGroupGatewayItemsDialog selectActivateGatewayConnections() {
        activateGatewayConnectionsButton.click();

        return new ActivateGroupGatewayItemsDialog(getPage());
    }

    @Step("Select 'Deactivate gateway connections'")
    public DeactivateGroupGatewayItemsDialog selectDeactivateGatewayConnections() {
        deactivateGatewayConnectionsButton.click();

        return new DeactivateGroupGatewayItemsDialog(getPage());
    }

    @Step("Hover over 'Edit acquirer MID' icon")
    public SuperAcquirersPage hoverOverEditIcon() {
        getByTestId("EditAcquirerButton").first().hover();

        return getCurrentPage();
    }

    @Step("Hover over 'Deactivate acquirer MID' icon")
    public SuperAcquirersPage hoverOverChangeActivityIcon() {
        getByTestId("ChangeAcquirerActivityButton").first().hover();

        return getCurrentPage();
    }

    @Step("Hover over 'Delete acquirer MID' icon")
    public SuperAcquirersPage hoverOverDeleteIcon() {
        getByTestId("DeleteAcquirerButton").first().hover();

        return getCurrentPage();
    }

    @Step("Hover over 'Bulk actions' icon")
    public SuperAcquirersPage hoverOverBulkActionsIcon() {
        locator("svg[data-icon='wand-magic-sparkles']").first().hover();

        return getCurrentPage();
    }
}
