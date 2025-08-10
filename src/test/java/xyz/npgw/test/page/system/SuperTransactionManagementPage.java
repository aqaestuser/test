package xyz.npgw.test.page.system;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.header.SuperHeaderMenuTrait;
import xyz.npgw.test.page.component.select.SelectDateRangeTrait;
import xyz.npgw.test.page.component.system.SuperSystemMenuTrait;
import xyz.npgw.test.page.dialog.adjustment.AddAdjustmentDialog;

@Getter
public class SuperTransactionManagementPage extends HeaderPage<SuperTransactionManagementPage>
        implements SuperHeaderMenuTrait<SuperTransactionManagementPage>,
                   SuperSystemMenuTrait,
                   SelectDateRangeTrait<SuperTransactionManagementPage> {

    private final Locator addAdjustmentButton = getByTestId("AddAdjustmentButtonTransactionManagementPage");
    private final Locator transactionsTable = getByLabelExact("transactions table");

    public SuperTransactionManagementPage(Page page) {
        super(page);
    }

    public AddAdjustmentDialog clickAddAdjustmentButton() {
        addAdjustmentButton.click();

        return new AddAdjustmentDialog(getPage());
    }
}
