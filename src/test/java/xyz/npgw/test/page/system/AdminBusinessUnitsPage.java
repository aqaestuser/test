package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.component.header.AdminHeaderMenuTrait;
import xyz.npgw.test.page.component.system.AdminSystemMenuTrait;
import xyz.npgw.test.page.component.table.AdminBusinessUnitsTableTrait;

public class AdminBusinessUnitsPage extends BaseBusinessUnitsPage<AdminBusinessUnitsPage>
        implements AdminHeaderMenuTrait<AdminBusinessUnitsPage>,
        AdminSystemMenuTrait,
        AdminBusinessUnitsTableTrait {

    public AdminBusinessUnitsPage(Page page) {
        super(page);
    }
}
