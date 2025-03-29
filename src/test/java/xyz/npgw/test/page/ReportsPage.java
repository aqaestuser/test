package xyz.npgw.test.page;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BasePageWithHeader;
import xyz.npgw.test.page.component.ContentBlock;

public class ReportsPage extends BasePageWithHeader {

    private final ContentBlock table;

    public ReportsPage(Page page) {
        super(page);
        table = new ContentBlock(page);
    }
}
