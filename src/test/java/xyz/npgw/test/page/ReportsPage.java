package xyz.npgw.test.page;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.HeaderPage;
import xyz.npgw.test.page.common.TableTrait;

public class ReportsPage extends HeaderPage implements TableTrait {

    public ReportsPage(Page page) {
        super(page);
    }
}
