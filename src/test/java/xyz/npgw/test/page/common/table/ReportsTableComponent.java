package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import xyz.npgw.test.page.ReportsPage;

public class ReportsTableComponent extends BaseTableComponent<ReportsPage> {

    public ReportsTableComponent(Page page) {
        super(page);
    }

    private final Locator filenameColumnHeader = getByRole(AriaRole.COLUMNHEADER, "Filename");
    private final Locator sizeColumnHeader = getByRole(AriaRole.COLUMNHEADER, "Size");

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
