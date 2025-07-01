package xyz.npgw.test.page.common.table;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import xyz.npgw.test.page.system.GatewayPage;

public class GatewayTableComponent extends BaseTableComponent<GatewayPage> {

    public GatewayTableComponent(Page page) {
        super(page);
    }

    @Override
    protected GatewayPage getCurrentPage() {
        return new GatewayPage(getPage());
    }
}
