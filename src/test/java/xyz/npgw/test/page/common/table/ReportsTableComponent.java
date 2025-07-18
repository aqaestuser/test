package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.ReportsPage;

public class ReportsTableComponent extends BaseTableComponent<ReportsPage> {

    private final Locator filenameColumnHeader;
    private final Locator sizeColumnHeader;

    public ReportsTableComponent(Page page) {
        super(page);

        this.filenameColumnHeader = getRoot().getByRole(AriaRole.COLUMNHEADER,
                new Locator.GetByRoleOptions().setName("Filename"));
        this.sizeColumnHeader = getRoot().getByRole(AriaRole.COLUMNHEADER,
                new Locator.GetByRoleOptions().setName("Size"));
    }

    @Override
    protected ReportsPage getCurrentPage() {
        return new ReportsPage(getPage());
    }

    @Step("Click the 'Filename' column header")
    public ReportsPage clickFilenameColumnHeader() {
        filenameColumnHeader.click();

        return new ReportsPage(getPage());
    }

    @Step("Click the 'Size' column header")
    public ReportsPage clickSizeColumnHeader() {
        sizeColumnHeader. click();

        return new ReportsPage(getPage());
    }
}
