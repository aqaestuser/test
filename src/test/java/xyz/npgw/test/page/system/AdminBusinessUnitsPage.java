package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.component.header.AdminHeaderMenuTrait;
import xyz.npgw.test.page.component.system.AdminSystemMenuTrait;
import xyz.npgw.test.page.component.table.AdminBusinessUnitsTableTrait;

@Getter
public class AdminBusinessUnitsPage extends HeaderPage<AdminBusinessUnitsPage>
        implements AdminHeaderMenuTrait<AdminBusinessUnitsPage>,
                   AdminSystemMenuTrait,
                   AdminBusinessUnitsTableTrait {

    public AdminBusinessUnitsPage(Page page) {
        super(page);
    }
}
