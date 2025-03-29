package xyz.npgw.test.page.component;

import com.microsoft.playwright.Page;

public class FilterBlock extends Element {

    public FilterBlock(Page page) {
        super(page, "div[aria-label='Filter block']");
    }
}
