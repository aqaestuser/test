package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.common.trait.BusinessUnitsTableTrait;
import xyz.npgw.test.page.common.trait.SelectCompanyTrait;

@Getter
public class AdminBusinessUnitsPage extends AdminSystemPage<AdminBusinessUnitsPage>
        implements SelectCompanyTrait<AdminBusinessUnitsPage>,
                   BusinessUnitsTableTrait {

    public AdminBusinessUnitsPage(Page page) {
        super(page);
    }
}
