package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import xyz.npgw.test.page.base.BasePageWithHeader;

public final class DashboardPage extends BasePageWithHeader {

    @Getter
    private final Locator dashboardButton = labelExact("Dashboard");

    public DashboardPage(Page page) {
        super(page);
    }


}
