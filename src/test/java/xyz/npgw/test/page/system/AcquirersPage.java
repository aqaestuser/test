package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.AcquirersTableTrait;
import xyz.npgw.test.page.common.trait.AlertTrait;
import xyz.npgw.test.page.common.trait.SelectAcquirerTrait;
import xyz.npgw.test.page.common.trait.SelectStatusTrait;
import xyz.npgw.test.page.dialog.acquirer.AddAcquirerDialog;

@Getter
public class AcquirersPage extends BaseSystemPage<AcquirersPage> implements AcquirersTableTrait,
        SelectAcquirerTrait<AcquirersPage>,
        AlertTrait<AcquirersPage>,
        SelectStatusTrait<AcquirersPage> {

    private final Locator addAcquirerButton = getByTestId("AddAcquirerButton");
    private final Locator addAcquirerDialog = getByRole(AriaRole.DIALOG);
    private final Locator resetFilterButton = getByTestId("ResetFilterButtonAcquirersPage");
    private final Locator refreshDataButton = getByTestId("ApplyFilterButtonAcquirersPage");

    public AcquirersPage(Page page) {
        super(page);
    }

    @Step("Click Add Acquirer")
    public AddAcquirerDialog clickAddAcquirer() {
        addAcquirerButton.click();

        return new AddAcquirerDialog(getPage());
    }
}
