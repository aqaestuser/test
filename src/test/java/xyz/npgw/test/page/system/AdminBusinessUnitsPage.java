package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.header.AdminHeaderMenuTrait;
import xyz.npgw.test.page.common.system.AdminSystemMenuTrait;
import xyz.npgw.test.page.common.trait.AdminBusinessUnitsTableTrait;

@Getter
public class AdminBusinessUnitsPage extends HeaderPage<AdminBusinessUnitsPage>
        implements AdminHeaderMenuTrait<AdminBusinessUnitsPage>,
                   AdminSystemMenuTrait,
                   AdminBusinessUnitsTableTrait {

    public AdminBusinessUnitsPage(Page page) {
        super(page);
    }
}
