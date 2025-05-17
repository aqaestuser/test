package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.HeaderPage;
import xyz.npgw.test.page.common.trait.MenuTrait;

public abstract class BaseSystemPage<SystemPageT extends BaseSystemPage<SystemPageT>> extends HeaderPage
        implements MenuTrait {

    public BaseSystemPage(Page page) {
        super(page);
    }

//    @Step("Click on the 'System administration' button in the Header")
//    public SystemPageT clickSystemAdministrationLink() {
//        getHeader().getSystemAdministrationButton().click();
//        getPage().waitForLoadState(LoadState.NETWORKIDLE);
//
//        return (SystemPageT) this;
//    }
}
