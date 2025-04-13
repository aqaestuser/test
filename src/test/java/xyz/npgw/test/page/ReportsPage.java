package xyz.npgw.test.page;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BaseHeaderPage;
import xyz.npgw.test.page.base.trait.TableTrait;

public class ReportsPage extends BaseHeaderPage implements TableTrait {

    public ReportsPage(Page page) {
        super(page);
    }
}
