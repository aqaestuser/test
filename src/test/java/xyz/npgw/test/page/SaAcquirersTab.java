package xyz.npgw.test.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import xyz.npgw.test.page.base.BasePageWithHeader;

public class SaAcquirersTab extends BasePageWithHeader {

    private final Locator addAcquirerButton  = locator("svg[data-icon='circle-plus']");
    private final Locator resetFilterButton  = locator("svg[data-icon='xmark']");
    private final Locator applyFilterButton  = locator("svg[data-icon='filter']");
    private final Locator selectAcquirerLabel  = labelExact("Select acquirer");
    private final Locator statusLabel  = labelExact("Status");

    public SaAcquirersTab(Page page) {
        super(page);
    }

    public Locator getAddAcquirerButton() {
        return addAcquirerButton;
    }

    public Locator getResetFilterButton() {
        return resetFilterButton;
    }

    public Locator getApplyFilterButton() {
        return applyFilterButton;
    }

    public Locator getSelectAcquirerLabel() {
        return selectAcquirerLabel;
    }

    public Locator getStatusLabel() {
        return statusLabel;
    }
}
