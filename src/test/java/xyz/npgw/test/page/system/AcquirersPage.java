package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.AcquirersTableTrait;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectAcquirerTrait;
import xyz.npgw.test.page.dialog.acquirer.AddAcquirerDialog;

@Getter
public class AcquirersPage extends BaseSystemPage<AcquirersPage> implements AcquirersTableTrait,
        SelectAcquirerTrait<AcquirersPage>,
        AlertTrait<AcquirersPage> {

    private final Locator addAcquirerButton = getByTestId("AddAcquirerButton");
    private final Locator addAcquirerDialog = getByRole(AriaRole.DIALOG);
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonAcquirersPage");
    private final Locator refreshDataButton = getByTestId("ApplyFilterButtonAcquirersPage");
    private final Locator statusLabel = getByLabelExact("Status");
    private final Locator statusValue = statusLabel.locator("span");
    private final Locator statusDropdown = locator("div[data-slot='listbox']");
    private final Locator statusOptions = statusDropdown.getByRole(AriaRole.OPTION);

    public AcquirersPage(Page page) {
        super(page);
    }

    @Step("Click Status value")
    public AcquirersPage clickStatusValue() {
        statusValue.click();

        return this;
    }

    @Step("Select Acquirer Status '{status}'")
    public AcquirersPage selectAcquirerStatus(String status) {
        statusOptions.getByText(status, new Locator.GetByTextOptions().setExact(true)).click();
        statusDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));

        return this;
    }

    @Step("Click Add Acquirer")
    public AddAcquirerDialog clickAddAcquirer() {
        addAcquirerButton.click();

        return new AddAcquirerDialog(getPage());
    }
}
