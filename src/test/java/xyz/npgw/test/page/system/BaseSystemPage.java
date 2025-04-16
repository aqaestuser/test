package xyz.npgw.test.page.system;

import com.microsoft.playwright.Page;
import xyz.npgw.test.page.common.HeaderPage;

public abstract class BaseSystemPage extends HeaderPage implements MenuTrait {

    public BaseSystemPage(Page page) {
        super(page);
    }
}
