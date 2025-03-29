package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BasePageWithHeader;

public class SaAcquirersTab extends BasePageWithHeader {

    private final Locator addAcquirerImg  = locator("svg[data-icon='circle-plus']");
    private final Locator resetFilterImg  = locator("svg[data-icon='xmark']");
    private final Locator applyFilterImg  = locator("svg[data-icon='filter']");
    private final Locator selectAcquirerPlaceholder  = labelExact("Select acquirer");
    private final Locator statusPlaceholder  = labelExact("Status");

    public SaAcquirersTab(Page page) {
        super(page);
    }

    public Locator getAddAcquirerImg() {
        return addAcquirerImg;
    }

    public Locator getResetFilterImg() {
        return resetFilterImg;
    }

    public Locator getApplyFilterImg() {
        return applyFilterImg;
    }

    public Locator getSelectAcquirerPlaceholder() {
        return selectAcquirerPlaceholder;
    }

    public Locator getStatusPlaceholder() {
        return statusPlaceholder;
    }
}
